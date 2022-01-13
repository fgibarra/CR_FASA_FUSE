package cl.ahumada.fuse.stockFarmacia.procesor;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.PropertyInject;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.stockFarmacia.api.resources.json.Farmacias;
import oracle.jdbc.OracleTypes;

public class EjecutaSP implements Processor {

	private Logger logger = Logger.getLogger(getClass());

    private javax.sql.DataSource datasource;
    @PropertyInject(value = "storeProcedureCall", defaultValue="{ call interpretecorp_own.STOCK_LOCAL_COMUNA_BUSCAR(?,?,?,?)}")
    private String qryTemplate;
    private String qry;
    protected Hashtable<ResultSet,Statement> hashStatements = new Hashtable<ResultSet,Statement>();
	Connection conn = null;

	@Override
	public void process(Exchange exchange) throws Exception {
		logger.info(String.format("EjecutaSP:recupera ds %s qry=%s", datasource!=null?"SI":"NO", qryTemplate));
		qry = String.format("{ call %s}", qryTemplate);
		Object producto = exchange.getMessage().getHeader("PRODUCTO");
		Object comuna = exchange.getMessage().getHeader("COMUNA");
		Object abierto = exchange.getMessage().getHeader("ABIERTO");
		
		ResultSet rs = getDatos(producto, comuna, abierto);
		
		List<Farmacias> farmacias = getFarmacias(rs);
		closeAllStatement();
		
		// arma el resultado
		LinkedHashMap<String,Object> map = new LinkedHashMap<String, Object>();
		map.put("farmacias", farmacias);

		exchange.getIn().setBody(map);
	}

	private ResultSet getDatos(Object producto, Object comuna, Object abierto) {
        CallableStatement stmt = null;
        ResultSet rs = null;
		try {

			conn = datasource.getConnection();
			logger.info("EjecutaSP.getDatos Acepto la conexion");


            stmt = conn.prepareCall(qry);

            stmt.setObject(1, producto);
            stmt.setObject(2, comuna);
            stmt.setObject(3, abierto);
            stmt.registerOutParameter(4, OracleTypes.CURSOR);

			logger.info(String.format("EjecutaSP.getDatos:Va a ejecutar SP %s(%s,%s,%s,OracleTypes.CURSOR)",
					qry,producto,comuna,abierto));
            stmt.execute();

            rs = (ResultSet)stmt.getObject(4);
    		logger.info(String.format("EjecutaSP.getDatos: rs.colcount=%d", rs.getMetaData().getColumnCount()));

            hashStatements.put(rs, stmt);
            
		} catch (Exception e) {
			logger.error("process",e);
		} finally {
		}
        return rs;
	}

	private List<Farmacias> getFarmacias(ResultSet resultSet) {
		List<Farmacias> listaFarmacias = new ArrayList<Farmacias>();
		try {
			logger.info(String.format("EjecutaSP.getFarmacias: recupera %d columnas, resultSet.isClosed()=%b", 
					resultSet.getMetaData().getColumnCount(),
					resultSet.isClosed()));
		} catch (SQLException e1) {
			logger.error("EjecutaSP.getFarmacias: no pudo recuperar el numero de columnas", e1);
			return null;
		}
		try {
			while (resultSet.next()) {
				logger.info(String.format("resultSet.getRow()=%d", resultSet.getRow()));
				Farmacias farmacia = new Farmacias(
						resultSet.getString(1), //Codigo
						resultSet.getString(2), //Direccion
						resultSet.getString(3), //Region
						resultSet.getString(4), //Ciudad
						resultSet.getString(5), //Comuna
						resultSet.getString(6), //Latitud
						resultSet.getString(7), //Longitud
						resultSet.getString(8), //Horario
						resultSet.getString(9), //Farmacia24H
						resultSet.getString(10), //SAFE
						resultSet.getString(11), //Estacionamiento
						//resultSet.getString(12) // nombreQF
						null);
				listaFarmacias.add(farmacia);
			}
		} catch (SQLException e) {
			logger.error("getFarmacias", e);
		}

		return listaFarmacias;
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
    	
        Enumeration<Statement> en = hashStatements.elements();

        while (en.hasMoreElements()) {
            try {
                Statement stmt = (Statement)en.nextElement();

                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException sql) {
            }
        }

        hashStatements = new Hashtable<ResultSet, Statement>();
		if (conn != null) {
			try {
				conn.close();
				logger.debug("EjecutaSP: cerrando conexion");
			} catch (SQLException e) {
				logger.error("EjecutaSP: cerrando conn",e);
			}
			conn = null;
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
