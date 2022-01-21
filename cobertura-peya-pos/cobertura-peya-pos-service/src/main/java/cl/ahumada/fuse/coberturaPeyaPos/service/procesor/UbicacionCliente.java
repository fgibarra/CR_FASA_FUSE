package cl.ahumada.fuse.coberturaPeyaPos.service.procesor;

import java.net.URLEncoder;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.PropertyInject;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author fernando
 * 
 * Recupera un org.w3c.dom.Element desde el body, que contiene un NodeList con comuna y direccion del cliente
 * recibidas del POS
 *
 * Devuelve en [header.Exchange.DESTINATION_OVERRIDE_URL] url para la invocacion a Google Maps para invocar
 * al API que obteniene la ubicacion del cliente.
 */
public class UbicacionCliente implements Processor {

	protected Logger logger = Logger.getLogger(getClass());

    @PropertyInject(value = "gmaps.url", defaultValue="https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyDkdKNsIJhc8TYudWuFw8KfjsO9g4bsCpM&address=%s")
    private String gmapsUrl;

	@SuppressWarnings("deprecation")
	@Override
	public void process(Exchange exchange) throws Exception {
		Element data = (Element) exchange.getIn().getBody();
		NodeList lista = data.getChildNodes();
		String comuna = null;
		String direccion = null;
		for (int i = 0; i < lista.getLength(); i++) {
			Node child = lista.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				String at = child.getNodeName();
				if ("comuna".equalsIgnoreCase(at)) {
					comuna = child.getTextContent();
				} else if ("direccion".equalsIgnoreCase(at)) {
					direccion = child.getTextContent();
				}
			}
		}
		String valor = String.format("%s,%s", direccion, comuna);
		logger.info(String.format("UbicacionCliente: valor [%s]", valor));
		exchange.getIn().setBody(valor);
		
		// preparar la url para gmaps
		String url = String.format(gmapsUrl, URLEncoder.encode(valor));
		logger.info(String.format("UbicacionCliente: url [%s]", url));
		exchange.getIn().setHeader(Exchange.DESTINATION_OVERRIDE_URL, url);
	}

}
