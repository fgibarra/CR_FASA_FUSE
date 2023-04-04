package cl.ahumada.fuse.productos.procesor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.productos.api.resources.ConsultaProductoRequest;

public class PreparaQry implements Processor {

	private Logger logger = Logger.getLogger(getClass());

	@Override
	public void process(Exchange exchange) throws Exception {
		ConsultaProductoRequest request = (ConsultaProductoRequest) exchange.getIn().getBody();
		logger.info(String.format("process: llego clase %s\n%s",
				(request != null ? request.getClass().getSimpleName() : "NULO"),
				(request != null ? request.toString() : "")));
		
		String busqueda = request.busqueda;
		exchange.getIn().setHeader("BUSQUEDA", busqueda);
	}

}
