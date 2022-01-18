package cl.ahumada.fuse.coberturaPeyaPos.service.procesor;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UbicacionFarmacias implements Processor {

	protected Logger logger = Logger.getLogger(getClass());

	@Override
	public void process(Exchange exchange) throws Exception {
		Element data = (Element) exchange.getIn().getBody();
		
		NodeList lista = data.getChildNodes();
		String valor[] = new String[0];
		for (int i = 0; i < lista.getLength(); i++) {
			Node child = lista.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				valor = child.getTextContent().split("\n");
				logger.info(String.format("UbicacionFarmacias: valor[%d]",valor.length));
			}
		}
		int i = 0;
		List<String> datos = new ArrayList<String>();
		for (String p : valor) {
			p = p.trim();
			logger.info(String.format("UbicacionFarmacias: valor[%d] = [%s] - %d", i++, p, p.length()));
			if (p!=null && !p.isEmpty())
				datos.add(p);
		}
		exchange.getIn().setBody(datos);
	}

}
