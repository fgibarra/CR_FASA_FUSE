package cl.ahumada.fuse.coberturaPeyaPos.service.procesor;

import java.io.IOException;
import java.io.SequenceInputStream;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.coberturaPeyaPos.service.json.peya.CoberturaResponse;
import cl.ahumada.fuse.coberturaPeyaPos.service.utils.JacksonFunctions;

/**
 * @author fernando
 * 
 * Saca un request de la lista de requests para calculo de distancias, invoca al api y deja en la lista de respuestas
 * la distancia devuelta por el api.
 *
 */
public class CalculaDistancia extends JacksonFunctions implements Processor {

	private Logger logger = Logger.getLogger(getClass());

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		Message message = exchange.getIn();
		String local = (String) message.getHeader("procesandoLocal");
		Long distancia = null;
		Map<String, Object> data = (Map<String,Object>) message.getHeader("responses");
		Map<String, Object> ubicacionFarmacias = (Map<String, Object>) data.get("ubicacionFarmacias");
		Object jsonResponse = message.getBody();
		logger.info(String.format("CalculaDistancia.process: local=%s body %s", local, jsonResponse));
		StringBuffer sb = new StringBuffer();

		if (jsonResponse instanceof Response) {
			Response response = (Response)jsonResponse;
			logger.info(String.format("CalculaDistancia.process: status %d length=%d entity: %s", 
					response.getStatus(),response.getLength(),
					response.getEntity().getClass().getSimpleName()));
			try {
				
				java.io.SequenceInputStream instream= (SequenceInputStream) response.getEntity();
				logger.info(String.format("CalculaDistancia.process: available=%d",instream.available()));
				
				int str;
				while ((str = instream.read()) >= 0)
					sb.append((char)str);
				
				logger.info(String.format("CalculaDistancia.process: en entity: |%s|", sb.toString()));

				if (response.getStatus() == 400) {
					// esta fuera del rango de distancia
					distancia = 9000l;
				} else if (response.getStatus() >= 200 && response.getStatus() < 300) {
					distancia = getDistancia(sb.toString());
				}
				ubicacionFarmacias.put(local, distancia);
				logger.info(String.format("CalculaDistancia.process: local: %s distancia: %d", local, distancia));
			} catch (IOException e) {
				logger.error("CalculaDistancia.process", e);
			} finally {
				response.close();
			}
		}
	}

	private Long getDistancia(String jsonString) {
		try {
			CoberturaResponse coberturaResponse = (CoberturaResponse) json2java(jsonString, CoberturaResponse.class, Boolean.FALSE);
			Long distancia = coberturaResponse.getDistancia();
			logger.info(String.format("getDistancia: distancia = %d", distancia));
			return distancia;
		} catch (Exception e) {
			logger.error("", e);
		}
		return 9000l;
	}

}
