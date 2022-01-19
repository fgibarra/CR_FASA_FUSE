package cl.ahumada.fuse.coberturaPeyaPos.service.procesor;

import java.io.SequenceInputStream;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.coberturaPeyaPos.service.json.gmaps.GMapsResponse;
import cl.ahumada.fuse.coberturaPeyaPos.service.json.gmaps.Location;
import cl.ahumada.fuse.coberturaPeyaPos.service.json.peya.Waypoints;
import cl.ahumada.fuse.coberturaPeyaPos.service.utils.JacksonFunctions;

public class ProcesaRespuestaGMaps extends JacksonFunctions implements Processor {

	private Logger logger = Logger.getLogger(getClass());
	
	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		GMapsResponse gMapsResponse;
		
		Object jsonResponse = exchange.getIn().getBody();
		logger.info(String.format("ProcesaRespuestaGMaps.process: body %s", jsonResponse));
		StringBuffer sb = new StringBuffer();

		if (jsonResponse instanceof Response) {
			@SuppressWarnings("resource")
			Response response = (Response)jsonResponse;
			logger.info(String.format("ProcesaRespuestaGMaps.process: status %d length=%d entity: %s", 
					response.getStatus(),response.getLength(),
					response.getEntity().getClass().getSimpleName()));
			
			java.io.SequenceInputStream instream= (SequenceInputStream) response.getEntity();
			logger.info(String.format("ProcesaRespuestaGMaps.process: available=%d",instream.available()));
			
			int str;
			while ((str = instream.read()) >= 0)
				sb.append((char)str);
			
			logger.info(String.format("ProcesaRespuestaGMaps.process: en entity: |%s|", sb.toString()));
			Object resp = "PROBLEMA EN SERVICIO DE GOOGLE";
			if (sb.length() > 0) {
				 
				gMapsResponse = (GMapsResponse) json2java(sb.toString(),
						GMapsResponse.class, false);
				// Interesa solo la location
				if (gMapsResponse != null && "OK".equalsIgnoreCase(gMapsResponse.getStatus())) {
					Location location = gMapsResponse.getResults()[0].getGeometry().getLocation();
					String formatedAdd = gMapsResponse.getResults()[0].getFormattedAddress();
					if (formatedAdd != null) {
						String partes[] = formatedAdd.split(",");
						boolean paraAdd = true;
						StringBuffer sbA = new StringBuffer();
						StringBuffer sbB = new StringBuffer();
						for (int i=0; i<partes.length; i++) {
							if (partes[i].startsWith("Regi"))
								paraAdd = false;
							if (paraAdd) {
								if (sbA.length()>0) sbA.append(", ");
								sbA.append(partes[i]);
							} else {
								if (sbB.length()>0) sbB.append(", ");
								sbB.append(partes[i]);
							}
						}
						Waypoints waypoints = new Waypoints("DROP_OFF",
													location.getLat(), 
													location.getLng(), 
													sbA.toString(), 
													sbB.toString());
						resp = waypoints;
						logger.info(String.format("ProcesaRespuestaGMaps.process: agrega %s", waypoints));
					}
				}
			}
			Map<String, Object> responses = (Map<String, Object>) exchange.getIn().getHeader("responses");
			responses.put("gMapsResponse", resp);
		}
	}

}
