package cl.ahumada.fuse.excedentes.procesor;

import java.util.StringTokenizer;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.excedentes.api.resources.json.C02Response;
import cl.ahumada.fuse.utils.Constantes;

public class PreparaResponseC02 implements Processor {

	private Logger logger = Logger.getLogger(getClass());

	@Override
	public void process(Exchange exchange) throws Exception {
		String proxyResponse = (String)exchange.getIn().getBody();
		C02Response response = null;
		if (proxyResponse != null) {
			logger.info(String.format("PreparaResponseC02\n%s", Constantes.dumpHexadecimal(proxyResponse.getBytes())));
			
			StringTokenizer st = new StringTokenizer(proxyResponse, "\u001C");
			String codigo = "NOK";
			String valor = null;
	
			if (st.hasMoreTokens()) {
				valor = st.nextToken();
				if (valor != null) {
					int indx = valor.indexOf(' ');
					if (indx >= 0)
						codigo = valor.substring(indx+1);
				}
			}
			response = new C02Response(codigo);
			logger.info(String.format("PreparaResponse: response %s", response.toString()));
		} else {
			logger.info("PreparaResponseC02: del proxy viene null");
			response = new C02Response("ERROR");
		}
		exchange.getIn().setBody(response);
	}

}
