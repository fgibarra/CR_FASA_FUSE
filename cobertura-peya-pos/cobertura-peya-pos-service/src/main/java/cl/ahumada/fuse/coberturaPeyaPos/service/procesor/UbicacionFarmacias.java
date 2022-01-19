package cl.ahumada.fuse.coberturaPeyaPos.service.procesor;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.PropertyInject;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cl.ahumada.fuse.coberturaPeyaPos.service.json.peya.Waypoints;
import oracle.jdbc.OracleTypes;

public class UbicacionFarmacias implements Processor {

	protected Logger logger = Logger.getLogger(getClass());
    private javax.sql.DataSource datasource;
    @PropertyInject(value = "ubicacionFarmacias.sp", defaultValue="ubicacion")
    private String storeProcedure;
    @PropertyInject(value = "ubicacionFarmacias.schema", defaultValue="ventadomicilio")
    private String schema;
    @PropertyInject(value = "ubicacionFarmacias.adicionalDefault", defaultValue="Región Metropolitana")
    private String adicionalDefault;
    
    
	public javax.sql.DataSource getDatasource() {
		return datasource;
	}


	public void setDatasource(javax.sql.DataSource datasource) {
		this.datasource = datasource;
	}


	public String getStoreProcedure() {
		return storeProcedure;
	}


	public void setStoreProcedure(String storeProcedure) {
		this.storeProcedure = storeProcedure;
	}


	public String getSchema() {
		return schema;
	}


	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getAdicionalDefault() {
		return adicionalDefault;
	}


	public void setAdicionalDefault(String adicionalDefault) {
		this.adicionalDefault = adicionalDefault;
	}



	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		Element data = (Element) exchange.getIn().getBody();
		
		NodeList lista = data.getChildNodes();
		String valor[] = new String[0];
		for (int i = 0; i < lista.getLength(); i++) {
			Node child = lista.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				valor = child.getTextContent().split("\n");
			}
		}
		int i = 0;
		StringBuffer listaFarmacias = new StringBuffer();
		for (String p : valor) {
			p = p.trim();
			logger.info(String.format("UbicacionFarmacias: valor[%d] = [%s] - %d", i++, p, p.length()));
			if (p!=null && !p.isEmpty()) {
				if (listaFarmacias.length() > 0) listaFarmacias.append(',');
				listaFarmacias.append(p);
			}
		}
		
		// invocar al SP que recupera las ubicaciones
		Object resultado = getUbicacionesFarmacias(listaFarmacias.toString());
		
		// colocar el resultado en el header
		Map<String, Object> responses = (Map<String, Object>) exchange.getIn().getHeader("responses");
		responses.put("ubicacionFarmacias", resultado);
	}


	private Object getUbicacionesFarmacias(String farmacias) {
		String qry = String.format("{ call %s.%s(?,?) }", schema, storeProcedure);
		Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        Object response;
		try {

			conn = datasource.getConnection();
			logger.info("getUbicacionesFarmacias. Acepto la conexion");


            stmt = conn.prepareCall(String.format("{ call %s}", qry));

            stmt.setString(1, farmacias);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);

			logger.info(String.format("getUbicacionesFarmacias a ejecutar SP: %s",qry));
            stmt.execute();

            rs = (ResultSet)stmt.getObject(2);

            // procesar la respuesta
            Map<String, Object> map = new HashMap<String, Object>();
            response = map;
            
            while (rs.next()) {
            	String numeroFarmacia = rs.getString(1);
            	String direccion = rs.getString(2);
            	String comuna = rs.getString(3);
            	Float latitud = toFloat(rs.getObject(4));
            	Float longitud = toFloat(rs.getObject(5));
            	Waypoints waypoints = new Waypoints("PICK_UP",
            			latitud, 
            			longitud, 
            			String.format("%s, %s", direccion, comuna), 
            			getAdicionalDefault());
            	map.put(numeroFarmacia, waypoints);
            	logger.info(String.format("getUbicacionesFarmacias: agrega %s [%s]", numeroFarmacia, waypoints));
            }
		} catch (Exception e) {
			logger.error("getUbicacionesFarmacias",e);
			response = new String("PROBLEMA DE CONEXION A BASE DE DATOS");
		} finally {
			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				logger.error("getUbicacionesFarmacias: cerrando conn",e);
			}
		}
		return response;
	}


	private Float toFloat(Object dato) {
		Float valor = null;
		if (dato != null) {
			try {
				if (dato instanceof BigDecimal)
					valor = ((BigDecimal) dato).floatValue();
				else if (dato instanceof String)
					valor = Float.valueOf((String) dato);
				else
					valor = Float.valueOf(dato.toString());
			} catch (Exception e) {
				logger.error(String.format("Float: No pudo convertir %s a Long", dato), e);
			}
		}
		return valor;
	}

}
