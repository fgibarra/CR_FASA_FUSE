package cl.ahumada.fuse.stock.procesor;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.PropertyInject;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.utils.Constantes;
import oracle.jdbc.OracleTypes;

public class ProcesaProductosPorLocal implements Processor {
	private Logger logger = Logger.getLogger(getClass());

    private javax.sql.DataSource datasource;
    @PropertyInject(value = "storeProcedureCall", defaultValue="pkg_esb_servicio2.STOCK_TIENDA_BUSCAR(?,?,?)")
    private String qry;
	protected Hashtable<ResultSet,Statement> hashStatements = new Hashtable<ResultSet,Statement>();
	Connection conn = null;
	
    
	public String getQry() {
		return qry;
	}

	public void setQry(String qry) {
		this.qry = qry;
	}

	public javax.sql.DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(javax.sql.DataSource datasource) {
		this.datasource = datasource;
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		
		logger.info(String.format("recupera ds %s qry=%s", datasource!=null?"SI":"NO", qry));
		
		String locales = (String) exchange.getMessage().getHeader("locales").toString();
		String productos = (String) exchange.getMessage().getHeader("productos").toString();
		
        logger.debug(String.format("locales: %s productos: %s", locales, productos));
        ResultSet rs = getDatos(locales, productos);

        logger.info("Vuelve con rs");
		List<List<Object>> resultado = new ArrayList<List<Object>>();
		if (rs != null) {
	        int colCount = 0;
		    try {
				colCount = rs.getMetaData().getColumnCount();
			} catch (Exception e) {
				logger.error("procesaListaLocalesProductos: recuperando colcount",e);
			}
		    logger.info("procesaListaLocalesProductos: colCount="+colCount);
	
		    while( rs.next() ) {
		    	logger.debug("procesaListaLocalesProductos: despues del rs.next");
		       List<Object> fila = new ArrayList<Object>();
		       for (int i=1; i <= colCount; i++) {
		    	   Object data = Constantes.toString(rs.getObject(i));
		    	   fila.add(data);
		    	   logger.info("procesaListaLocalesProductos: posicion="+(i-1)+" "+rs.getMetaData().getColumnName(i)+"=|"+data+
		    			   "| clase devuelta "+rs.getMetaData().getColumnClassName(i));
	
		       }
		       resultado.add(fila);
		       logger.debug("procesaListaLocalesProductos: fila="+resultado.size()+" columnas fila="+fila.size());
		    }
		    
		    closeAllStatement();
		}
		// arma el resultado

		logger.info(String.format("resultado: %d elementos",resultado.size()));
        exchange.getIn().setBody(resultado);		
	}

	private ResultSet getDatos(String locales, String productos) {
        CallableStatement stmt = null;
        ResultSet rs = null;
		try {

			conn = datasource.getConnection();
			logger.info("procesaListaLocalesProductos. Acepto la conexion");


            stmt = conn.prepareCall(String.format("{ call %s}", qry));

            stmt.setString(1, locales);
            stmt.setString(2, productos);
            stmt.registerOutParameter(3, OracleTypes.CURSOR);

			logger.info("procesaListaLocalesProductos:Va a ejecutar SP");
            stmt.execute();

            rs = (ResultSet)stmt.getObject(3);

            hashStatements.put(rs, stmt);
            
		} catch (Exception e) {
			logger.error("process",e);
		} finally {
		}
        return rs;
	}

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
				logger.debug("procesaListaLocalesProductos: cerrando conexion");
			} catch (SQLException e) {
				logger.error("procesaListaLocalesProductos: cerrando conn",e);
			}
			conn = null;
		}
    }
}
