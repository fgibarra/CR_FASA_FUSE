package cl.ahumada.fuse.coberturaPeyaPos.service.procesor;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.coberturaPeyaPos.lib.CoberturaPeyaResponse;
import cl.ahumada.fuse.coberturaPeyaPos.service.json.peya.Waypoints;

public class GeneraRequestPeya implements Processor {

	private Logger logger = Logger.getLogger(getClass());
	
	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		CoberturaPeyaResponse response = null;
		Map<String, Object> data = (Map<String,Object>) exchange.getIn().getHeader("responses");
		Object ubicacionFarmacias = data.get("ubicacionFarmacias");
		Object gMapsResponse = data.get("gMapsResponse");
		String msgError = null;
		
		if (ubicacionFarmacias == null || ubicacionFarmacias instanceof String) {
			// error
			msgError = (String) ubicacionFarmacias;
		} 
		
		if (gMapsResponse == null || gMapsResponse instanceof String) {
			// error
			if (msgError != null)
				msgError = String.format("%s | %s", msgError, gMapsResponse);
			else
				msgError = (String) gMapsResponse;
		}
		if (msgError != null) {
			response = new CoberturaPeyaResponse(Integer.valueOf(-1), msgError, null);
			exchange.getIn().setBody(response);
			logger.info(String.format("GeneraRequestPeya: responde body:[%s]", data));
			exchange.getIn().setHeader("pideCobertura", "NO");
			return;
		}
		
		// genera el request para peya
		Waypoints ubicacionCliente = (Waypoints) gMapsResponse;
		Map<String, Object> map = (Map<String, Object>) ubicacionFarmacias;
		exchange.getIn().setBody(getRequest(ubicacionCliente, map));
		exchange.getIn().setHeader("pideCobertura", "SI");
	}

	private Object getRequest(Waypoints ubicacionCliente, Map<String, Object> map) {
		logger.info(String.format("GeneraRequestPeya: ubicacion cliente: %s", ubicacionCliente));
		for (String local : map.keySet()) {
			logger.info(String.format("GeneraRequestPeya: ubicacion local %s [%s]", local, map.get(local)));
		}
		
		//TODO
		
		return "Hay que preparar el request";
	}

}
