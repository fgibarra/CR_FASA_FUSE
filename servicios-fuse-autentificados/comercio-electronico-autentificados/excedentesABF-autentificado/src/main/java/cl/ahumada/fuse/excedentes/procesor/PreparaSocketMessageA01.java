package cl.ahumada.fuse.excedentes.procesor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.PropertyInject;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.excedentes.api.resources.json.A01Request;
import cl.ahumada.fuse.utils.Constantes;

public class PreparaSocketMessageA01 implements Processor {

	private Logger logger = Logger.getLogger(getClass());

    @PropertyInject(value = "PatternReqProxyA01", defaultValue="")
    private String qryTemplate;

	@Override
	public void process(Exchange exchange) throws Exception {
		A01Request request = (A01Request)exchange.getIn().getBody();
		logger.info(String.format("PreparaSocketMessage: request:|%s|", request));
		String buffer = qryTemplate.replace("$request.rut", request.getRut());
		buffer = buffer.replace("$request.convenio", request.getCodigoConvenio());
		buffer = buffer.replace("$request.excedente", String.format("%s",request.getMontoExcedente()));
		buffer = buffer.replace("$request.numeroLocal", String.format("%d",request.getNumeroLocal()));
		buffer = buffer.replace("$request.cantidadProductos", String.format("%d",request.getCantidadProductos()));
		buffer = buffer.replace("$request.productos.toPipe", String.format("%s",request.productos2Pipe()));
		buffer = buffer.replace('^', '\u001C');
		buffer = String.format("%s\r\n\r\n",buffer);
		logger.info(String.format("PreparaSocketMessage:buffer\n%s", Constantes.dumpHexadecimal(buffer.getBytes())));
		
		exchange.getIn().setBody(buffer);
	}

}
