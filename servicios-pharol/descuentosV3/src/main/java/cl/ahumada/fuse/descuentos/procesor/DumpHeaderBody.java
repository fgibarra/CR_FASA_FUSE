package cl.ahumada.fuse.descuentos.procesor;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

public class DumpHeaderBody implements Processor {

	protected Logger logger = Logger.getLogger(getClass());

	@Override
	public void process(Exchange exchange) throws Exception {
		Object body = exchange.getIn().getBody();
		
		Map<String,Object> headers = exchange.getIn().getHeaders();
		StringBuffer sb = new StringBuffer();
		sb.append(String.format("clase body: %s\n%s\nHEADERS:\n",
				body.getClass(), body.toString()));
		for (String key : headers.keySet()) {
			Object value = headers.get(key);
			sb.append(key).append(':').append(String.format("[%s]=%s", value.getClass(), value)).append('\n');
		}
		logger.info(String.format("DumpHeaderBody: %s", sb.toString()));
	}

}
