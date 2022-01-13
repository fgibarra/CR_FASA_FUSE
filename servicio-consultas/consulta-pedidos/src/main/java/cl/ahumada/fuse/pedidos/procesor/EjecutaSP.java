package cl.ahumada.fuse.pedidos.procesor;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.PropertyInject;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.pedidos.api.resources.json.Cliente;
import cl.ahumada.fuse.pedidos.api.resources.json.MedioPago;
import cl.ahumada.fuse.pedidos.api.resources.json.Pedido;
import cl.ahumada.fuse.pedidos.api.resources.json.Producto;
import cl.ahumada.fuse.pedidos.api.resources.json.Trackings;
import oracle.jdbc.OracleTypes;

public class EjecutaSP implements Processor {

	private Logger logger = Logger.getLogger(getClass());

    private javax.sql.DataSource datasource;
    @PropertyInject(value = "storeProcedureCall", defaultValue="interpretecorp_own.SP_CONSULTA_PEDIDOS(?,?,?,?,?,?,?)")
    private String qryTemplate;
    private String qry;
	protected Set<Statement> hashStatements = new HashSet<Statement>();
	protected List<ResultSet> res;
	Connection conn = null;

	@Override
	public void process(Exchange exchange) throws Exception {
		logger.info(String.format("recupera ds %s qry=%s", datasource!=null?"SI":"NO", qryTemplate));
		qry = String.format("{ call %s}", qryTemplate);
		String rut = (String) exchange.getMessage().getHeader("RUT").toString();
		String mail = (String) exchange.getMessage().getHeader("MAIL").toString();
		String numeroPedido = (String) exchange.getMessage().getHeader("NUMERO_PEDIDO").toString();
		String codigoComercio = (String) exchange.getMessage().getHeader("CODIGO_COMERCIO").toString();
		
		res = getDatos(rut, mail, numeroPedido, codigoComercio);
		
		Map<String,Pedido> pedidos = getPedidos(res.get(0)); // recupera mapa de pedidos
		logger.info(String.format("EjecutaSP: pedidos.isEmpty %b size=%d", pedidos.isEmpty(), pedidos.size()));
		Map<String,List<Trackings>> tracking = getTracking(res.get(1)); // recupera mapa de tracking
		logger.info(String.format("EjecutaSP: tracking.isEmpty %b size=%d", tracking.isEmpty(), tracking.size()));
		Cliente cliente = getClientes(res.get(2));
		logger.info(String.format("cliente %s", cliente == null?"ES NULO" : cliente.toString()));
		closeAllStatement();
		
		// arma el resultado
		LinkedHashMap<String,Object> map = new LinkedHashMap<String, Object>();
		map.put("pedidos", pedidos);
		map.put("tracking", tracking);
		map.put("cliente", cliente);
		
		exchange.getIn().setBody(map);
	}

	private List<ResultSet> getDatos(String rut, String mail, String numeroPedido, String codigoComercio) {
        CallableStatement stmt = null;
        ResultSet rsPedidos = null;
        ResultSet rsTracking = null;
        ResultSet rsCliente = null;
        List<ResultSet>lista = new ArrayList<ResultSet>();
		try {

			conn = datasource.getConnection();
			logger.info(String.format("EjecutaSP. Acepto la conexion: parametros: rut=%s mail=%s numeroPedido=%s codigoComercio=%s",
					rut,mail,numeroPedido,codigoComercio));


            stmt = conn.prepareCall(String.format("{ call %s}", qry));

            stmt.setString(1, rut);
            stmt.setString(2, mail);
            stmt.setString(3, numeroPedido);
            stmt.setString(4, codigoComercio);
            stmt.registerOutParameter(5, OracleTypes.CURSOR);
            stmt.registerOutParameter(6, OracleTypes.CURSOR);
            stmt.registerOutParameter(7, OracleTypes.CURSOR);

			logger.info("EjecutaSP a ejecutar SP");
            stmt.execute();

            rsPedidos = (ResultSet)stmt.getObject(5);
            rsTracking = (ResultSet)stmt.getObject(6);
            rsCliente = (ResultSet)stmt.getObject(7);

            hashStatements.add(stmt);
            lista.add(rsPedidos);
            lista.add(rsTracking);
            lista.add(rsCliente);
            
		} catch (Exception e) {
			logger.error("process",e);
		} finally {
		}
        return lista;
	}

	private Map<String, Pedido> getPedidos(ResultSet resultSet) {
		Map<String,Pedido> mapPedido = new HashMap<String,Pedido>();
		Map<String,List<Producto>> mapProd = new HashMap<String,List<Producto>>();
		try {
			while (resultSet.next()) {
				String numeroPedido = resultSet.getString(1); // Numero_pedido_ahumada; key para todo
				String codigoProducto = resultSet.getString(7); // Codigo
				logger.info(String.format("numeroPedido: %s codigo producto=%s", numeroPedido, codigoProducto));

				MedioPago medioPago = null;
				Producto producto = new Producto(
						codigoProducto, //Codigo
						resultSet.getString(8), //Nombre
						resultSet.getString(9), //Imagen
						resultSet.getLong(10), //canrtitad
						resultSet.getLong(11), // preciuo unitario
						resultSet.getLong(12), // descxuento
						resultSet.getLong(13)); // totasl
				List<Producto> listaProd = mapProd.get(numeroPedido);
				logger.info(String.format("listaProd: %s existe", listaProd == null?"NO":"SI"));
				if (listaProd == null) {
					listaProd = new ArrayList<Producto>();
					mapProd.put(numeroPedido, listaProd);
				}
				listaProd.add(producto);

				// toma el primer medio de pago de la secuencia
				if (!mapPedido.containsKey(numeroPedido)) {
					medioPago = new MedioPago(
							resultSet.getInt(14), //Forma_pago
							resultSet.getLong(15), //Tipo_pago
							resultSet.getLong(16), //Monto
							resultSet.getLong(17), //Codigo_autorizacion
							resultSet.getString(18), //Codigo_comercio_tbk
							resultSet.getString(19), //Obj_unico_trx
							resultSet.getString(20), //Forma_pago_desc
							resultSet.getString(21) //Tipo_pago_desc
							);
				}
				
				// toma los datos del pedido del primero de la secuencia
				if (!mapPedido.containsKey(numeroPedido)) {
					Pedido pedido = new Pedido(
							numeroPedido, //Numero_pedido_ahumada
							resultSet.getString(2), //Numero_pedido_comercio
							resultSet.getString(3), //Codigo_comercio
							resultSet.getInt(4), //Tipo_entrega
							resultSet.getString(5), //fecha_entrega
							resultSet.getString(6),  //Estado_pedido
							resultSet.getString(22), //Tipo_entrega_desc
							resultSet.getString(23), //Estado_pedido_desc
							resultSet.getString(24), //Fecha_pedido
							listaProd.toArray(new Producto[0]),
							null, // tracking
							new MedioPago[] {medioPago}
							);
					mapPedido.put(numeroPedido, pedido);
				} else {
					Pedido pedido = mapPedido.get(numeroPedido);
					pedido.setProducto(listaProd.toArray(new Producto[0]));
				}
					
			}
		} catch (SQLException e) {
			logger.error("getPedidos", e);
		}
		return mapPedido;
	}

	private Map<String, List<Trackings>> getTracking(ResultSet resultSet) {
		Map<String,List<Trackings>> map = new HashMap<String, List<Trackings>>();
		try {
			while (resultSet.next()) {
				Trackings trackings = new Trackings(
						resultSet.getString(1), //id
						resultSet.getString(2), //descripcion
						resultSet.getString(3), //fecha
						resultSet.getString(4) //estado
						);
				String idPedido = trackings.id;
				if (map.containsKey(idPedido)) {
					((List<Trackings>)map.get(idPedido)).add(trackings);
				} else {
					List<Trackings> tracks = new ArrayList<Trackings>();
					tracks.add(trackings);
					map.put(idPedido, tracks);
				}
			}
		} catch (SQLException e) {
			logger.error("getTracking", e);;
		}
		return map;
	}

	private Cliente getClientes(ResultSet resultSet) {
		Cliente cliente = null;
		try {
			while (resultSet.next()) {
				cliente = new Cliente(
						resultSet.getString(3), //Rut
						resultSet.getString(4), //Mail
						resultSet.getString(5), //Celular
						resultSet.getString(1), //Nombres
						resultSet.getString(2) //Apellidos
						);
			}
		} catch (SQLException e) {
			logger.error("getTracking", e);;
		}
		return cliente;
	}

	//---------------------------------------------------------------------------------------------------
	
	protected Object dump(List<Object> lista) {
		StringBuffer sb = new StringBuffer();
		for (Object obj : lista) {
			sb.append(obj).append('|');
		}
		return sb.toString();
	}

	/**
     * Cierra todos los ResultSet usados
     */
    public void closeAllStatement() {
    	
    	for (ResultSet rs : res) {
    		try {
				rs.close();
			} catch (SQLException e) {
			}
    	}
        Iterator<Statement> en = hashStatements.iterator();

        while (en.hasNext()) {
            try {
                Statement stmt = (Statement)en.next();

                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException sql) {
            }
        }
    }

    public String getQryTemplate() {
		return qryTemplate;
	}

	public void setQryTemplate(String qry) {
		this.qryTemplate = qry;
	}

	public javax.sql.DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(javax.sql.DataSource datasource) {
		this.datasource = datasource;
	}

}
