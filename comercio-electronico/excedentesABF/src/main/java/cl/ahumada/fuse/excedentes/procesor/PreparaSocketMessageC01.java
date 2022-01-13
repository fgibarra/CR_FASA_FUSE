package cl.ahumada.fuse.excedentes.procesor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.PropertyInject;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.excedentes.api.resources.json.C01Request;
import cl.ahumada.fuse.utils.Constantes;

public class PreparaSocketMessageC01 implements Processor {

	private Logger logger = Logger.getLogger(getClass());

    @PropertyInject(value = "PatternReqProxyC01", defaultValue="")
    private String qryTemplate;

	@Override
	public void process(Exchange exchange) throws Exception {
		C01Request request = (C01Request)exchange.getIn().getBody();
		logger.info(String.format("PreparaSocketMessage: request:|%s|", request));
		String buffer = qryTemplate.replace("$request.codigoConvenio", request.getCodigoConvenio());
		buffer = buffer.replace("$request.rut", request.getRut()!=null?request.getRut():"");
		buffer = buffer.replace("$request.fecha", request.getFecha()!=null?request.getFecha():"");
		buffer = buffer.replace("$request.montoExcedente", String.format("%s",request.getMontoExcedente()!=null?request.getMontoExcedente():""));
		buffer = buffer.replace("$request.codigoAutorizadorServicio", request.getCodigoAutorizadorServicio()!=null?request.getCodigoAutorizadorServicio():"");
		buffer = buffer.replace("$request.numeroPedido", request.getNumeroPedido()!=null?request.getNumeroPedido():"");
		buffer = buffer.replace('^', '\u001C');
		buffer = String.format("%s\r\n\r\n",buffer);

		logger.info(String.format("PreparaSocketMessage:buffer\n%s", Constantes.dumpHexadecimal(buffer.getBytes())));
		
		exchange.getIn().setBody(buffer);
	}

}
