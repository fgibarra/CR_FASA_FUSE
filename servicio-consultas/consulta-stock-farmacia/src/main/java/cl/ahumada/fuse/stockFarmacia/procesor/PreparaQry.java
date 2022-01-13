package cl.ahumada.fuse.stockFarmacia.procesor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.stockFarmacia.api.resources.ConsultaStockRequest;

public class PreparaQry implements Processor {

	private Logger logger = Logger.getLogger(getClass());

	@Override
	public void process(Exchange exchange) throws Exception {
		ConsultaStockRequest request = (ConsultaStockRequest) exchange.getIn().getBody();
		logger.info(String.format("process: llego clase %s\n%s",
				(request != null ? request.getClass().getSimpleName() : "NULO"),
				(request != null ? request.toString() : "")));

		Object producto = request.producto.codigo;
		exchange.getIn().setHeader("PRODUCTO", producto);
		Object comuna = request.ubicacion.comuna;
		exchange.getIn().setHeader("COMUNA", comuna);
		Object abierto = request.abiertas;
		exchange.getIn().setHeader("ABIERTO", abierto);
	}

}
