package cl.ahumada.fuse.coberturaPeyaPos.service.procesor;

import java.io.SequenceInputStream;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.coberturaPeyaPos.service.json.peya.TokenResponse;
import cl.ahumada.fuse.coberturaPeyaPos.service.utils.JacksonFunctions;

/**
 * @author fernando
 *
 * Recibe en el body un javax.ws.rs.core.Response resultado de la invocacion al API cxfrs:bean:rsPeyaToken
 * 
 * Devuelve en [header.responses.TokenResponse] un TokenResponse con el token o
 * un String con el mensaje de error.
 */
public class ProcesaRespuestaToken extends JacksonFunctions implements Processor {

	private Logger logger = Logger.getLogger(getClass());
	
	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		TokenResponse token;
		
		Object jsonResponse = exchange.getIn().getBody();
		logger.info(String.format("ProcesaRespuestaToken.process: body %s", jsonResponse));
		StringBuffer sb = new StringBuffer();

		if (jsonResponse instanceof Response) {
			@SuppressWarnings("resource")
			Response response = (Response)jsonResponse;
			logger.info(String.format("ProcesaRespuestaToken.process: status %d length=%d entity: %s", 
					response.getStatus(),response.getLength(),
					response.getEntity().getClass().getSimpleName()));
			
			java.io.SequenceInputStream instream= (SequenceInputStream) response.getEntity();
			logger.info(String.format("ProcesaRespuestaToken.process: available=%d",instream.available()));
			
			int str;
			while ((str = instream.read()) >= 0)
				sb.append((char)str);
			
			logger.info(String.format("ProcesaRespuestaToken.process: en entity: |%s|", sb.toString()));
			Object resp = "PROBLEMAS EN SERVICIO PEDIDOS YA";
			if (sb.length() > 0) {
				 
				token = (TokenResponse) json2java(sb.toString(),
						TokenResponse.class, false);
				// Interesa solo la location
				if (token != null) {
						resp = token;
						logger.info(String.format("ProcesaRespuestaToken.process: agrega %s", token));
					
				}
			}
			Map<String, Object> responses = (Map<String, Object>) exchange.getIn().getHeader("responses");
			responses.put("TokenResponse", resp);
		}
	}
}
