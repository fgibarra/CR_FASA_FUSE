package cl.ahumada.fuse.pedidos.procesor;

import java.io.InputStream;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import cl.ahumada.fuse.pedidos.api.resources.MiMultipartFormDataInput;
import cl.ahumada.fuse.pedidos.api.resources.v3.PedidosRequest;
import cl.ahumada.fuse.utils.json.JSonUtilities;

public class PreparaQry implements Processor {

	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	public void process(Exchange exchange) throws Exception {
		MiMultipartFormDataInput mpfdi = (MiMultipartFormDataInput)exchange.getIn().getBody();
		InputPart inputPart = mpfdi.getFormDataPart("attachment", null);
		InputStream inputStream = inputPart.getBody(InputStream.class, null);
        String dataPDF = new String(IOUtils.toByteArray(inputStream));
        
        //logger.info(String.format("PreparaQry: dataPDF %s", dataPDF));
        
        inputPart = mpfdi.getFormDataPart("json", null);
        String jsonInput = String.format("{\"PedidosRequest\":%s}",inputPart.getBodyAsString());
        
        PedidosRequest request = (PedidosRequest) JSonUtilities.getInstance().json2java(jsonInput, PedidosRequest.class);

		String oCabecera = request.toPipechar();
		String oCarro = request.carroCompras.toPipechar();
		String oTotalVta = request.carroCompras.totalVentatoPipechar();
		String oMedioPago = request.medioPagotoPipechar();
		String oDatosCliente = request.cliente.toPipechar();
		String oDatosEntrega = request.datosEntrega.toPipechar();
		String oDatosFactura = "";

		logger.info(String.format("PreparaQuery: oCabecera{%s} oCarro{%s} oTotalVta{%s} oMedioPago{%s} oDatosCliente{%s} oDatosEntrega{%s} dataPDF{%d}",
				oCabecera, oCarro, oTotalVta, oMedioPago, oDatosCliente, oDatosEntrega, dataPDF.length()));
		
		exchange.getIn().setHeader("oCabecera", oCabecera);
		exchange.getIn().setHeader("oCarro", oCarro);
		exchange.getIn().setHeader("oTotalVta", oTotalVta);
		exchange.getIn().setHeader("oMedioPago", oMedioPago);
		exchange.getIn().setHeader("oDatosCliente", oDatosCliente);
		exchange.getIn().setHeader("oDatosEntrega", oDatosEntrega);
		exchange.getIn().setHeader("oDatosFactura", oDatosFactura);
		exchange.getIn().setHeader("dataPDF", dataPDF);
	}

}
