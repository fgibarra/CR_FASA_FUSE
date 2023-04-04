package cl.ahumada.fuse.stockFarmacia.procesor;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.stockFarmacia.api.resources.ConsultaStockResponse;
import cl.ahumada.fuse.stockFarmacia.api.resources.json.Farmacias;


public class GeneraRespuestaWs implements Processor {

	Logger logger = Logger.getLogger(getClass());

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		LinkedHashMap<String,Object> map = (LinkedHashMap<String, Object>) exchange.getIn().getBody();
		List<Farmacias> listaFarmacias = (List<Farmacias>)map.get("farmacias");
		ConsultaStockResponse response = new ConsultaStockResponse(null);
		if (listaFarmacias != null) {
			response = new ConsultaStockResponse(listaFarmacias.toArray(new Farmacias[0]));
		}
		exchange.getIn().setBody(response);
	}

}
