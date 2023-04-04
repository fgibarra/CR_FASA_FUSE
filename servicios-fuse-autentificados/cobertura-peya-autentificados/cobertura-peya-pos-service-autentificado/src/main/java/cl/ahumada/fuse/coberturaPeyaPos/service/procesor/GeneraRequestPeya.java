package cl.ahumada.fuse.coberturaPeyaPos.service.procesor;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.coberturaPeyaPos.lib.CoberturaPeyaResponse;
import cl.ahumada.fuse.coberturaPeyaPos.service.json.peya.CoberturaRequest;
import cl.ahumada.fuse.coberturaPeyaPos.service.json.peya.Waypoints;

/**
 * @author fernando
 *
 * Verifica que esten las respuestas de los tres splits sin error. Si no cumple deja en [header.pideCobertura] = NO
 * Deja en [header.listaTuplasCobertura] los request para consultar la cobertura para todas las farmacias/cliente
 * 
 */
public class GeneraRequestPeya implements Processor {

	private Logger logger = Logger.getLogger(getClass());
	
	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		CoberturaPeyaResponse response = null;
		Map<String, Object> data = (Map<String,Object>) exchange.getIn().getHeader("responses");
		Object ubicacionFarmacias = data.get("ubicacionFarmacias");
		Object gMapsResponse = data.get("gMapsResponse");
		Object tokenResponse = data.get("TokenResponse");
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
		if (tokenResponse == null || tokenResponse instanceof String) {
			// error
			if (msgError != null)
				msgError = String.format("%s | %s", msgError, tokenResponse);
			else
				msgError = (String) tokenResponse;
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
		exchange.getIn().setHeader("listaTuplasCobertura", getRequest(ubicacionCliente, map));
		exchange.getIn().setHeader("pideCobertura", "SI");
	}

	private Map<String, CoberturaRequest> getRequest(Waypoints ubicacionCliente, Map<String, Object> map) {
		logger.info(String.format("GeneraRequestPeya: ubicacion cliente: %s", ubicacionCliente));
		Map<String, CoberturaRequest> requests = new HashMap<String, CoberturaRequest>();
		
		for (String local : map.keySet()) {
			logger.info(String.format("GeneraRequestPeya: ubicacion local %s [%s]", local, map.get(local)));
			Waypoints[] waypoints = new Waypoints[] {
					ubicacionCliente, (Waypoints)map.get(local)
			};
			CoberturaRequest r = new CoberturaRequest(waypoints);
			logger.info(String.format("getRequest: %s", r));
			requests.put(local,  r );
		}
		
		return requests;
	}

}
