package cl.ahumada.fuse.pedidos.procesor;

import java.util.LinkedHashMap;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.pedidos.api.resources.json.PedidosResponse;
import cl.ahumada.fuse.pedidos.api.resources.json.PharolError;
import cl.ahumada.fuse.utils.Constantes;
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
			int indx = resultadoSP.indexOf('|');
			String[] datos = new String[2];
			if (indx > 0){
				datos[0] = resultadoSP.substring(0, indx);
				datos[1] = resultadoSP.substring(indx+1);
			} else
				datos[1] = "-1";

			logger.info(String.format("resultado = %s datos.len=%d d[0]=%s d[1]=%s", resultadoSP, datos.length, datos[0],datos[1]));
			if ("ER".equals(datos[0])) {
				response = JSonUtilities.getInstance().java2json(new PharolError("ERROR_BB_DD", "Error al generar el pedido", datos[1]));
				logger.info(String.format("process: responde: %s",response));
			} else {
				response = JSonUtilities.getInstance().java2json(new PedidosResponse(Constantes.toLong(datos[1])));
				logger.info(String.format("process: responde: %s",response));
			}
		}
		exchange.getIn().setBody(response);
	}

}
