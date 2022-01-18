package cl.ahumada.fuse.coberturaPeyaPos.service.procesor;

import java.io.SequenceInputStream;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.coberturaPeyaPos.service.json.gmaps.GMapsResponse;
import cl.ahumada.fuse.coberturaPeyaPos.service.utils.JacksonFunctions;

public class ProcesaRespuestaGMaps extends JacksonFunctions implements Processor {

	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	public void process(Exchange exchange) throws Exception {
		GMapsResponse gMapsResponse;
		
		Object jsonResponse = exchange.getIn().getBody();
		logger.info(String.format("ProcesaRespuestaGMaps.process: body %s", jsonResponse));
		StringBuffer sb = new StringBuffer();

		if (jsonResponse instanceof Response) {
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
			if (sb.length() > 0) {
				gMapsResponse = (GMapsResponse) json2java(sb.toString(),
						GMapsResponse.class, false);

				Map<String, Object> responses = (Map<String, Object>) exchange.getIn().getHeader("responses");
				responses.put("gMapsResponse", gMapsResponse);
			}
		}
	}

}
