package cl.ahumada.fuse.requesterPeya.monitorLogistico.process;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import cl.ahumada.fuse.requesterPeya.lib.monitorLogistico.MonitorLogisticoRequest;

public class GeneraUrl implements Processor {

	private final String urlTemplate="http://localhost:8180/peyaRequester?numeroOrden=%d&confirma=%d";
	@Override
	public void process(Exchange exchange) throws Exception {
		MonitorLogisticoRequest request = (MonitorLogisticoRequest)exchange.getIn().getBody();
		

		exchange.getIn().setHeader(Exchange.DESTINATION_OVERRIDE_URL, String.format(urlTemplate, 
				request.getNumeroOrden(), 1));
		exchange.getIn().setHeader("CamelHttpMethod", "GET");
	}

}
