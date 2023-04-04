package cl.ahumada.fuse.descuentos.procesor;

import java.io.SequenceInputStream;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.descuentos.api.resources.json.BalanceInquiryRequest;
import cl.ahumada.fuse.descuentos.api.resources.json.CartEntryResponse;
import cl.ahumada.fuse.utils.JSonUtilities;

public class ProcesaRespuestaCartEntry implements Processor {

	protected Logger logger = Logger.getLogger(getClass());

	@Override
	public void process(Exchange exchange) throws Exception {
		CartEntryResponse cartEntryResponse = null;
		// En el Body viene la respuesta de la invocacion a cartEntry
		Object jsonResponse = exchange.getIn().getBody();
		logger.info(String.format("ProcesaRespuestaCartEntry.process: body %s", jsonResponse));
		StringBuffer sb = new StringBuffer();

		if (jsonResponse instanceof Response) {
			Response response = (Response)jsonResponse;
			logger.info(String.format("ProcesaRespuestaCartEntry.process: status %d length=%d entity: %s", 
					response.getStatus(),response.getLength(),
					response.getEntity().getClass().getSimpleName()));
			
			java.io.SequenceInputStream instream= (SequenceInputStream) response.getEntity();
			logger.info(String.format("ProcesaRespuestaCartEntry.process: available=%d",instream.available()));
			
			int str;
			while ((str = instream.read()) >= 0)
				sb.append((char)str);
			
			logger.info(String.format("ProcesaRespuestaCartEntry.process: en entity: |%s|", sb.toString()));
			if (sb.length() > 0) {
				cartEntryResponse = (CartEntryResponse) JSonUtilities.getInstance().json2java(String.format("{ \"CartEntryResponse\": %s }", sb.toString()),
					CartEntryResponse.class);

				Map<String, Object> responses = (Map<String, Object>) exchange.getIn().getHeader("responses");
				responses.put("cartEntryResponse", cartEntryResponse);
			}
		}

		exchange.getIn().setBody(sb.toString());
		/*
		Map<String, Object> servicios = (Map<String, Object>) exchange.getIn().getHeader("request");
		BalanceInquiryRequest balanceInquiryReq = (BalanceInquiryRequest) servicios.get("BalanceInquiryRequest");
		
		exchange.getIn().setBody(JSonUtilities.getInstance().java2json(balanceInquiryReq));
		*/
	}

}
