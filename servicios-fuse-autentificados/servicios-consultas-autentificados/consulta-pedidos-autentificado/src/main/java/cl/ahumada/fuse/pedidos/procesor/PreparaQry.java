package cl.ahumada.fuse.pedidos.procesor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.pedidos.api.resources.ConsultaPedidoRequest;

public class PreparaQry implements Processor {

	private Logger logger = Logger.getLogger(getClass());

	@Override
	public void process(Exchange exchange) throws Exception {
		ConsultaPedidoRequest request = (ConsultaPedidoRequest) exchange.getIn().getBody();
		logger.info(String.format("process: llego clase %s\n%s",
				(request != null ? request.getClass().getSimpleName() : "NULO"),
				(request != null ? request.toString() : "")));
		
		String rut = request.cliente.rut;
		String mail = request.cliente.mail;
		String numeroPedido = request.pedido.numeroPedidoAhumada;
		String codigoComercio = request.pedido.codigoComercio;
		
		exchange.getIn().setHeader("RUT", rut);
		exchange.getIn().setHeader("MAIL", mail);
		exchange.getIn().setHeader("NUMERO_PEDIDO", numeroPedido);
		exchange.getIn().setHeader("CODIGO_COMERCIO", codigoComercio);
	}

}
