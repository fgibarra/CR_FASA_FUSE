package cl.ahumada.fuse.pedidos.procesor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.pedidos.api.resources.v4.PedidosRequest;

public class PreparaQuery implements Processor {

	private Logger logger = Logger.getLogger(getClass());

	@Override
	public void process(Exchange exchange) throws Exception {
		PedidosRequest request = (PedidosRequest)exchange.getIn().getBody();
		String oCabecera = request.toPipechar();
		String oCarro = request.carroCompras.toPipechar();
		String oTotalVta = request.carroCompras.totalVentatoPipechar();
		String oMedioPago = request.medioPagotoPipechar();
		String oDatosCliente = request.cliente.toPipechar();
		String oDatosEntrega = request.datosEntrega.toPipechar();
		String oDatosFactura = "";
		String oMessages = request.posMessagestoPipechar();
		
		logger.info(String.format("PreparaQuery: oCabecera{%s} oCarro{%s} oTotalVta{%s} oMedioPago{%s} oDatosCliente{%s} oDatosEntrega{%s} oMessages{%s}",
				oCabecera, oCarro, oTotalVta, oMedioPago, oDatosCliente, oDatosEntrega, oMessages));
		exchange.getIn().setHeader("oCabecera", oCabecera);
		exchange.getIn().setHeader("oCarro", oCarro);
		exchange.getIn().setHeader("oTotalVta", oTotalVta);
		exchange.getIn().setHeader("oMedioPago", oMedioPago);
		exchange.getIn().setHeader("oDatosCliente", oDatosCliente);
		exchange.getIn().setHeader("oDatosEntrega", oDatosEntrega);
		exchange.getIn().setHeader("oDatosFactura", oDatosFactura);
		exchange.getIn().setHeader("oMessages", oMessages);
	}

}
