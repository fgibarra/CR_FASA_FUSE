package cl.ahumada.fuse.descuentos.procesor;

import java.io.SequenceInputStream;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.descuentos.api.resources.json.CalculateDiscountResponse;
import cl.ahumada.fuse.utils.JSonUtilities;

public class ProcesaRespuestaCalculateDiscount implements Processor {

	protected Logger logger = Logger.getLogger(getClass());

	@SuppressWarnings("resource")
	@Override
	public void process(Exchange exchange) throws Exception {
		Object jsonResponse = exchange.getIn().getBody();
		logger.info(String.format("ProcesaRespuestaCalculateDiscount.process: body %s", jsonResponse));
		CalculateDiscountResponse cdResponse = null;
		StringBuffer sb = new StringBuffer();
		
		if (jsonResponse instanceof Response) {
			Response response = (Response)jsonResponse;
			logger.info(String.format("ProcesaRespuestaCalculateDiscount.process: status %d length=%d entity: %s", 
					response.getStatus(),response.getLength(),
					response.getEntity().getClass().getSimpleName()));
			
			java.io.SequenceInputStream instream= (SequenceInputStream) response.getEntity();
			logger.info(String.format("ProcesaRespuestaCalculateDiscount.process: available=%d",instream.available()));
			
			int str;
			while ((str = instream.read()) >= 0)
				sb.append((char)str);
			
			logger.info(String.format("ProcesaRespuestaCalculateDiscount.process: en entity: |%s|", sb.toString()));
			if (sb.length() > 0) {
				cdResponse = (CalculateDiscountResponse) JSonUtilities.getInstance().json2java(String.format("{ \"CalculateDiscountResponse\": %s }", sb.toString()),
						CalculateDiscountResponse.class);

				@SuppressWarnings("unchecked")
				Map<String, Object> responses = (Map<String, Object>) exchange.getIn().getHeader("responses");
				responses.put("calculateDiscountResponse", cdResponse);
			}
		}

		exchange.getIn().setBody(sb.toString());
	}

}
