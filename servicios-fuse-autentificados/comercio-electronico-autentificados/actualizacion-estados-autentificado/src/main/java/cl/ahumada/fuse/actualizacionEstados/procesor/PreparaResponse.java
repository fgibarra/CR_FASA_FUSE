package cl.ahumada.fuse.actualizacionEstados.procesor;

import java.util.LinkedHashMap;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.actualizacionEstados.api.resources.v1.ActualizaEstadosResponse;
import cl.ahumada.fuse.utils.json.JSonUtilities;

public class PreparaResponse implements Processor {

	private Logger logger = Logger.getLogger(getClass());

	@Override
	public void process(Exchange exchange) throws Exception {
		String keyResultadoSP = "resultadoSP";
		@SuppressWarnings("unchecked")
		LinkedHashMap<String,String> map = (LinkedHashMap<String, String>) exchange.getIn().getBody();
		String resultadoSP = map.get(keyResultadoSP);
		String response = "{}";
		
		logger.info(String.format("PreparaResponse: resultadoSP=%s body{%s}", resultadoSP, exchange.getIn().getBody()));
		if (resultadoSP != null) {
			ActualizaEstadosResponse res = null;
			if ("0".equals(resultadoSP))
				res = new ActualizaEstadosResponse("OK");
			else
				res = new ActualizaEstadosResponse("NOK");
			response = JSonUtilities.getInstance().java2json(res);
		}
		
		exchange.getIn().setBody(response);
	}

}
