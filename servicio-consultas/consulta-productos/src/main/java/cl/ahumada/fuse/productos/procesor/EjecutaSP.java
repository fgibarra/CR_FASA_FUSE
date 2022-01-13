package cl.ahumada.fuse.productos.procesor;

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

import cl.ahumada.fuse.productos.api.resources.json.Producto;
import cl.ahumada.fuse.productos.api.resources.json.Promocion;
import oracle.jdbc.OracleTypes;

public class EjecutaSP implements Processor {

	private Logger logger = Logger.getLogger(getClass());

    private javax.sql.DataSource datasource;
    @PropertyInject(value = "storeProcedureCall", defaultValue="{ call interpretecorp_own.SP_CONSULTA_PRODUCTOS(?,?,?)}")
    private String qryTemplate;
    private String qry;
	protected Set<Statement> hashStatements = new HashSet<Statement>();
	protected List<ResultSet> res;
	Connection conn = null;

	@Override
	public void process(Exchange exchange) throws Exception {
		logger.info(String.format("recupera ds %s qry=%s", datasource!=null?"SI":"NO", qryTemplate));
		qry = String.format("{ call %s}", qryTemplate);
		String busqueda = (String) exchange.getMessage().getHeader("BUSQUEDA").toString();

		res = getDatos(busqueda);
		
		Map<String,Producto> productos = getProductos(res.get(0)); // recupera mapa de productos
		Map<String,List<Promocion>> promociones = getPromociones(res.get(1)); // recupera mapa de promociones
		
		closeAllStatement();
		
		// arma el resultado
		LinkedHashMap<String,Object> map = new LinkedHashMap<String, Object>();
		map.put("productos", productos);
		map.put("promociones", promociones);
		
		exchange.getIn().setBody(map);
	}

	private List<ResultSet> getDatos(String busqueda) {
        CallableStatement stmt = null;
        ResultSet rsProductos = null;
        ResultSet rsPromociones = null;
        List<ResultSet>lista = new ArrayList<ResultSet>();
		try {

			conn = datasource.getConnection();
			logger.info("EjecutaSP. Acepto la conexion");


            stmt = conn.prepareCall(String.format("{ call %s}", qry));

            stmt.setString(1, busqueda);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.registerOutParameter(3, OracleTypes.CURSOR);

			logger.info("EjecutaSP a ejecutar SP");
            stmt.execute();

            rsProductos = (ResultSet)stmt.getObject(2);
            rsPromociones = (ResultSet)stmt.getObject(3);

            hashStatements.add(stmt);
            lista.add(rsProductos);
            lista.add(rsPromociones);
            
            logger.info(String.format("EjecutaSP: rsProductos.colcount=%d rsPromociones.colcount=%d", 
            		rsProductos.getMetaData().getColumnCount(), 
            		rsPromociones.getMetaData().getColumnCount()));
		} catch (Exception e) {
			logger.error("process",e);
		} finally {
		}
        return lista;
	}

	private Map<String, Producto> getProductos(ResultSet resultSet) {
		Map<String,Producto> map = new HashMap<String,Producto>();
		try {
			while (resultSet.next()) {
				Producto producto = new Producto(
						resultSet.getString(1), //Codigo
						resultSet.getString(2), //Nombre
						resultSet.getString(3), //descripcion
						resultSet.getString(4), //imagen
						resultSet.getLong(5), //precio
						null);
				map.put(producto.codigo, producto);
			}
		} catch (SQLException e) {
			logger.error("getProductos", e);
		}
		return map;
	}

	private Map<String, List<Promocion>> getPromociones(ResultSet resultSet) {
		Map<String,List<Promocion>> map = new HashMap<String, List<Promocion>>();
		try {
			while (resultSet.next()) {
				String codigoProducto = resultSet.getString(1);
				Promocion promocion = new Promocion(
						resultSet.getLong(2), //precioOferta
						resultSet.getString(3), //mecanicaDescripcion
						resultSet.getString(4) //mecanicaVigencia
						);
				if (map.containsKey(codigoProducto)) {
					((List<Promocion>)map.get(codigoProducto)).add(promocion);
				} else {
					List<Promocion> tracks = new ArrayList<Promocion>();
					tracks.add(promocion);
					map.put(codigoProducto, tracks);
				}
			}
		} catch (SQLException e) {
			logger.error("getPromociones", e);;
		}
		return map;
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
