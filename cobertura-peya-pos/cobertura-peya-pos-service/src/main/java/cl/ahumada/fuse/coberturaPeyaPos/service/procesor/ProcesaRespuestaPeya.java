package cl.ahumada.fuse.coberturaPeyaPos.service.procesor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.PropertyInject;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.coberturaPeyaPos.lib.CoberturaPeyaResponse;

/**
 * @author fernando
 * 
 * Toma del header la listas de distancias farmacia-cliente desde el header y arma las respuestas con las que cumplen
 * la regla que la distancia sea menor que la distancia maxima
 *
 */
public class ProcesaRespuestaPeya implements Processor {

	private Logger logger = Logger.getLogger(getClass());
	
    @PropertyInject(value = "peyaCobertura.distanciaMaxima", defaultValue="7000")
	private String distanciaMaximaProp;
	private Long distanciaMaxima = 7000l;
	
	public String getDistanciaMaximaProp() {
		return distanciaMaximaProp;
	}

	public void setDistanciaMaximaProp(String distanciaMaximaProp) {
		this.distanciaMaximaProp = distanciaMaximaProp;
		if (distanciaMaximaProp != null)
			this.distanciaMaxima = Long.valueOf(distanciaMaximaProp);
	}

	public Long getDistanciaMaxima() {
		return distanciaMaxima;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		Message message = exchange.getIn();
		Map<String, Object> data = (Map<String,Object>) message.getHeader("responses");
		Map<String, Object> ubicacionFarmacias = (Map<String, Object>) data.get("ubicacionFarmacias");
		List<String> cumplen = new ArrayList<String>();
		
		for (String local : ubicacionFarmacias.keySet()) {
			Long distancia = (Long) ubicacionFarmacias.get(local);
			logger.info(String.format("ProcesaRespuestaPeya: local: [%s] distancia %d", local, distancia));
			if (distancia < distanciaMaxima)
				cumplen.add(local);
		}
		
		
		CoberturaPeyaResponse response = new CoberturaPeyaResponse(0, "OK", cumplen.toArray(new String[0]));
		exchange.getIn().setBody(response);
	}

}
