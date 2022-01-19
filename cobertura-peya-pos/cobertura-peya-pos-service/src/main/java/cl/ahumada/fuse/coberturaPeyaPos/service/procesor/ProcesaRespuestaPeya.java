package cl.ahumada.fuse.coberturaPeyaPos.service.procesor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.coberturaPeyaPos.lib.CoberturaPeyaResponse;

public class ProcesaRespuestaPeya implements Processor {

	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		logger.info(String.format("ProcesaRespuestaPeya: entra: [%s]", exchange.getIn().getBody()));
		
		
		CoberturaPeyaResponse response = new CoberturaPeyaResponse(0, "OK", new String[] {"0001","0002","0072"});
		exchange.getIn().setBody(response);
	}

}
