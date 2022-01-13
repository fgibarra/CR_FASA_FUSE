package cl.ahumada.fuse.actualizacionEstados.procesor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import cl.ahumada.fuse.actualizacionEstados.api.resources.v1.ActualizaEstadosRequest;

public class PreparaQuery implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		ActualizaEstadosRequest request = (ActualizaEstadosRequest)exchange.getIn().getBody();
		exchange.getIn().setHeader("ID_ORDER", request.getOrderLine());
		exchange.getIn().setHeader("FECHA_ESTIMADA", request.getEstimatedDeliveryDate());
		exchange.getIn().setHeader("CODIGO_ESTADO", request.getEstatusCode());
		exchange.getIn().setHeader("COMENTARIO", request.getComment());
	}

}
