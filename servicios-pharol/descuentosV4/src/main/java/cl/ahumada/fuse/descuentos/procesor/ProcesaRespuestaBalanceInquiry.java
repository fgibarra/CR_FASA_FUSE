package cl.ahumada.fuse.descuentos.procesor;

import java.io.SequenceInputStream;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.descuentos.api.resources.json.BalanceInquiryResponse;
import cl.ahumada.fuse.utils.JSonUtilities;

public class ProcesaRespuestaBalanceInquiry implements Processor {

	protected Logger logger = Logger.getLogger(getClass());

	@SuppressWarnings({ "resource", "unchecked" })
	@Override
	public void process(Exchange exchange) throws Exception {
		Object jsonResponse = exchange.getIn().getBody();
		logger.info(String.format("ProcesaRespuestaBalanceInquiry.process: body %s", jsonResponse));
		BalanceInquiryResponse biResponse = null;
		StringBuffer sb = new StringBuffer();
		
		if (jsonResponse instanceof Response) {
			Response response = (Response)jsonResponse;
			logger.info(String.format("ProcesaRespuestaBalanceInquiry.process: status %d length=%d entity: %s", 
					response.getStatus(),response.getLength(),
					response.getEntity().getClass().getSimpleName()));
			
			java.io.SequenceInputStream instream= (SequenceInputStream) response.getEntity();
			logger.info(String.format("ProcesaRespuestaBalanceInquiry.process: available=%d",instream.available()));
			
			int str;
			while ((str = instream.read()) >= 0)
				sb.append((char)str);
			
			logger.info(String.format("ProcesaRespuestaBalanceInquiry.process: en entity: |%s|", sb.toString()));
			if (sb.length() > 0) {
				biResponse = (BalanceInquiryResponse) JSonUtilities.getInstance().json2java(String.format("{ \"BalanceInquiryResponse\": %s }", sb.toString()),
						BalanceInquiryResponse.class);

				Map<String, Object> responses = (Map<String, Object>) exchange.getIn().getHeader("responses");
				responses.put("balanceInquiryResponse", biResponse);
			}
		}

		exchange.getIn().setBody(sb.toString());
	}

}
