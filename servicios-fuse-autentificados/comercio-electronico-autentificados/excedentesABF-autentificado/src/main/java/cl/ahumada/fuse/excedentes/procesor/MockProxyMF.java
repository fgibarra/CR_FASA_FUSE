package cl.ahumada.fuse.excedentes.procesor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.PropertyInject;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.utils.Constantes;

public class MockProxyMF implements Processor {

	private Logger logger = Logger.getLogger(getClass());
    @PropertyInject(value = "PatternResProxy", defaultValue="^{codigo}^{monto}^{autorizador}^")
    private String resTemplate;

	@Override
	public void process(Exchange exchange) throws Exception {
		String proxyRequest = (String)exchange.getIn().getBody();
		logger.info(String.format("\n%s", Constantes.dumpHexadecimal(proxyRequest.getBytes())));
		
		String buffer = resTemplate.replace("{codigo}", "123456");
		buffer = buffer.replace("{monto}", String.format("%s","900"));
		buffer = buffer.replace("{autorizador}", String.format("%s","A23457"));
		buffer = buffer.replace('^', '\u001C');
		buffer = String.format("%s\r\n",buffer);
		logger.info(String.format("\n%s", Constantes.dumpHexadecimal(buffer.getBytes())));
		
		exchange.getIn().setBody(buffer);
		
	}

}
