package cl.ahumada.fuse.coberturaPeyaPos.service.procesor;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.coberturaPeyaPos.service.json.peya.CoberturaRequest;
import cl.ahumada.fuse.coberturaPeyaPos.service.json.peya.TokenResponse;

/**
 * @author fernando
 * 
 * Saca el primero de la lista [header.listaTuplasCobertura] y lo coloca en el body,
 * saca el token y prepara el http header.
 *
 */
public class ProcesaTupla implements Processor {

	private Logger logger = Logger.getLogger(getClass());
	
	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		Message message = exchange.getIn();
		Map<String,Object> responses = (Map<String, Object>) message.getHeader("responses");

		TokenResponse tokenResponse = (TokenResponse) responses.get("TokenResponse");
		logger.info(String.format("ProcesaTupla: tokenResponse: %s",tokenResponse));
		
		String token = tokenResponse.getAccessToken();
		
		Map<String, CoberturaRequest> lista = (Map<String, CoberturaRequest>) message.getHeader("listaTuplasCobertura");
		
		if (lista != null && lista.size() > 0) {
			String local = (String) lista.keySet().toArray()[0];
			logger.info(String.format("ProcesaTupla: procesando local %s", local));
			CoberturaRequest request = lista.remove(local);
			logger.info(String.format("ProcesaTupla: request: %s", request));
			message.setBody(request);
			message.setHeader("procesandoLocal", local);
			message.setHeader("Authorization", token);
		} else
			throw new RuntimeException ("ProcesaTupla List<CoberturaRequest> nula o vacia");
	}

}
