package cl.ahumada.fuse.stock.procesor;

import java.util.HashSet;
import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.json.stock.ConsultaStockRequest;
import cl.ahumada.fuse.json.stock.Local;
import cl.ahumada.fuse.json.stock.Stock;

public class PreparaQry implements Processor {

	private Logger logger = Logger.getLogger(getClass());

	@Override
	public void process(Exchange exchange) throws Exception {

		ConsultaStockRequest request = (ConsultaStockRequest) exchange.getIn().getBody();
		logger.info(String.format("process: llego clase %s\n%s",
				(request != null ? request.getClass().getSimpleName() : "NULO"),
				(request != null ? request.toString() : "")));
		StringBuffer locales = new StringBuffer();
		StringBuffer productos = new StringBuffer();

		Set<String> setProductos = new HashSet<String>();

		for (Local local : request.local) {
			String numeroLocal = String.format("%d", local.numeroLocal);
			locales.append(numeroLocal).append('|');
			for (Stock stock : local.stock) {
				String codigoProducto = String.format("%d", stock.codigoProducto);
				if (!setProductos.contains(codigoProducto))
					setProductos.add(codigoProducto);
			}
		}
		for (String valor : setProductos) {
			productos.append(valor).append('|');
		}
		logger.info(String.format("locales: {%s} productos: {%s}", locales, productos));
		exchange.getIn().setHeader("locales", locales);
		exchange.getIn().setHeader("productos", productos);
		exchange.getIn().setHeader("inStockPedido", request);

		logger.info("saliendo");
	}
}
