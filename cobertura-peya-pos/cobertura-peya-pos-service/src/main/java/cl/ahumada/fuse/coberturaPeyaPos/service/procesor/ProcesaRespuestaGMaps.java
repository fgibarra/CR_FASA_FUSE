package cl.ahumada.fuse.coberturaPeyaPos.service.procesor;

import java.io.IOException;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.coberturaPeyaPos.service.json.gmaps.GMapsResponse;
import cl.ahumada.fuse.coberturaPeyaPos.service.json.gmaps.Location;
import cl.ahumada.fuse.coberturaPeyaPos.service.json.peya.Waypoints;
import cl.ahumada.fuse.coberturaPeyaPos.service.utils.JacksonFunctions;

/**
 * @author fernando
 * 
 * Recibe en el body un javax.ws.rs.core.Response resultado de la invocacion al API cxfrs:bean:rsClientGMaps
 * 
 * Devuelve en [header.responses.gMapsResponse] un Waypoints para la cobertura del cliente o
 * un String con el mensaje de error.
 *
 */
public class ProcesaRespuestaGMaps extends JacksonFunctions implements Processor {

	private Logger logger = Logger.getLogger(getClass());
	
	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		Message message = exchange.getIn();
		GMapsResponse gMapsResponse;
		
		Object jsonResponse = message.getBody();
		logger.info(String.format("ProcesaRespuestaGMaps.process: body %s", jsonResponse));
		StringBuffer sb = new StringBuffer();

		if (jsonResponse instanceof Response) {
			@SuppressWarnings("resource")
			Response response = (Response)jsonResponse;
			logger.info(String.format("ProcesaRespuestaGMaps.process: status %d length=%d entity: %s", 
					response.getStatus(),response.getLength(),
					response.getEntity().getClass().getSimpleName()));
			
			try {
				java.io.SequenceInputStream instream= (SequenceInputStream) response.getEntity();
				logger.info(String.format("ProcesaRespuestaGMaps.process: available=%d",instream.available()));
				
				int str;
				while ((str = instream.read()) >= 0)
					sb.append((char)str);
			} catch (IOException e) {
				logger.error("ProcesaRespuestaGMaps.process", e);
			} finally {
				response.close();
			}
			
			logger.info(String.format("ProcesaRespuestaGMaps.process: en entity: |%s|", sb.toString()));
			Object resp = "PROBLEMA EN SERVICIO DE GOOGLE";
			if (sb.length() > 0) {
				 
				gMapsResponse = (GMapsResponse) json2java(sb.toString(),
						GMapsResponse.class, false);
				// Interesa solo la location
				if (gMapsResponse != null && "OK".equalsIgnoreCase(gMapsResponse.getStatus())) {
					Location location = gMapsResponse.getResults()[0].getGeometry().getLocation();
					String formatedAdd = gMapsResponse.getResults()[0].getFormattedAddress();
					logger.info(String.format("ProcesaRespuestaGMaps.process: formatedAdd: %s", formatedAdd));
					if (formatedAdd != null) {
						String partes[] = formatedAdd.split(",");
						boolean paraAdd = true;
						StringBuffer sbA = new StringBuffer();
						StringBuffer sbB = new StringBuffer();
						for (int i=0; i<partes.length; i++) {
							logger.info(String.format("ProcesaRespuestaGMaps.process: partes[%d]: %s", i, partes[i]));
							if (partes[i].indexOf("Regi") >= 0)
								paraAdd = false;
							if (paraAdd) {
								if (sbA.length()>0) sbA.append(", ");
								sbA.append(partes[i]);
							} else {
								if (sbB.length()>0) sbB.append(", ");
								sbB.append(partes[i]);
							}
						}
						String street = sbA.toString();
						if (sbB.length() == 0) {
							if (partes.length > 3)
								sbB.append(partes[partes.length - 3]);
							else
								sbB.append(partes[partes.length - 2]);
						}
						String comuna = sbB.toString();
						Waypoints waypoints = new Waypoints("DROP_OFF",
													location.getLat(), 
													location.getLng(), 
													street, 
													comuna,
													comuna, //city
													getNombre(street, comuna), // name
													Integer.valueOf(1));
						resp = waypoints;
						logger.info(String.format("ProcesaRespuestaGMaps.process: agrega %s", waypoints));
					}
				}
			}
			Map<String, Object> responses = (Map<String, Object>) message.getHeader("responses");
			responses.put("gMapsResponse", resp);
			List<Long> distancias = new ArrayList<Long>();
			message.setHeader("distancias", distancias);
		}
	}

	protected String getNombre(String street, String comuna) {
		int n1 = (int) (new java.util.Date().getTime() % 100);
		String p1 = street != null && street.length() > 8 ? street.substring(0, 8) : "Santiago";
		String p2 = comuna != null && comuna.length() > 8 ? comuna.substring(0, 8) : "Region Metro";
		return String.format("B-%d-%s/%s", n1, p1, p2);
	}

}
