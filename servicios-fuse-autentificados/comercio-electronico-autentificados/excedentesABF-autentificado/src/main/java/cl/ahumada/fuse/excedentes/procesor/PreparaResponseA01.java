package cl.ahumada.fuse.excedentes.procesor;

import java.util.StringTokenizer;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.excedentes.api.resources.json.A01Response;
import cl.ahumada.fuse.utils.Constantes;

public class PreparaResponseA01 implements Processor {

	private Logger logger = Logger.getLogger(getClass());

	@Override
	public void process(Exchange exchange) throws Exception {
		String proxyResponse = (String)exchange.getIn().getBody();
		A01Response response = null;
		if (proxyResponse != null && proxyResponse.length() > 0) {
			logger.info(String.format("PreparaResponseA01:\n%s", Constantes.dumpHexadecimal(proxyResponse.getBytes())));
			
			StringTokenizer st = new StringTokenizer(proxyResponse, "\u001C");
			String codigo = null;
			String valor = null;
			Long monto = 0l;
			String autorizador = null;
			
			if (st.hasMoreTokens()) {
				valor = st.nextToken();
				if (valor != null) {
					int indx = valor.indexOf(' ');
					if (indx >= 0)
						codigo = valor.substring(indx+1);
				}
			}
			// saltarse el siguiente
			st.nextToken();
			
			if (st.hasMoreTokens()) {
				valor = st.nextToken();
				if (valor != null)
					monto = Long.valueOf(valor);
			}
			if (st.hasMoreTokens())
				autorizador = st.nextToken();
			
			response = new A01Response(codigo, monto, autorizador);
			logger.info(String.format("PreparaResponse: response %s", response.toString()));
		} else {
			logger.info("PreparaResponseA01: del proxy viene null");
			response = new A01Response("N", 0l, "0");
		}
		exchange.getIn().setBody(response);
	}

}
