package cl.ahumada.fuse.coberturaPeyaPos.service.procesor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.coberturaPeyaPos.lib.CoberturaPeyaResponse;

public class GeneraMock implements Processor {

	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	public void process(Exchange exchange) throws Exception {
		
		String data = (String) exchange.getIn().getBody();
		logger.info(String.format("body:[%s]", data));

		CoberturaPeyaResponse response = new CoberturaPeyaResponse(Integer.valueOf(0), "OK", new String[] {"0001","0002"});
		exchange.getIn().setBody(response);
	}

}
