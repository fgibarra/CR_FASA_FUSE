package cl.ahumada.fuse.excedentes.procesor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.PropertyInject;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.excedentes.api.resources.json.C02Request;
import cl.ahumada.fuse.utils.Constantes;

public class PreparaSocketMessageC02 implements Processor {

	private Logger logger = Logger.getLogger(getClass());

    @PropertyInject(value = "PatternReqProxyC02", defaultValue="")
    private String qryTemplate;

	@Override
	public void process(Exchange exchange) throws Exception {
		C02Request request = (C02Request)exchange.getIn().getBody();
		logger.info(String.format("PreparaSocketMessage: request:|%s|", request));
		String buffer = qryTemplate.replace("$request.codigoConvenio", request.getCodigoConvenio());
		buffer = buffer.replace("$request.codigoConvenio", request.getCodigoConvenio());
		buffer = buffer.replace("$request.rut", request.getRut());
		buffer = buffer.replace("$request.fecha", request.getFecha());
		buffer = buffer.replace("$request.montoExcedente", String.format("%s",request.getMontoExcedente()));
		buffer = buffer.replace("$request.codigoAutorizadorServicio", request.getCodigoAutorizadorServicio());
		buffer = buffer.replace("$request.numeroPedido", request.getNumeroPedido());
		buffer = buffer.replace('^', '\u001C');
		buffer = String.format("%s\r\n\r\n",buffer);

		logger.info(String.format("PreparaSocketMessage:buffer\n%s", Constantes.dumpHexadecimal(buffer.getBytes())));
		
		exchange.getIn().setBody(buffer);
	}

}
