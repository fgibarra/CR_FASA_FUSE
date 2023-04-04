package cl.ahumada.fuse.descuentos.procesor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.descuentos.api.resources.DescuentosRequest;
import cl.ahumada.fuse.descuentos.api.resources.dto.Servicios;
import cl.ahumada.fuse.descuentos.api.resources.json.BalanceInquiryRequest;
import cl.ahumada.fuse.descuentos.api.resources.json.CartEntryRequest;
import cl.ahumada.fuse.descuentos.api.resources.json.CommonRequest;
import cl.ahumada.fuse.descuentos.api.resources.json.ItemReq;
import cl.ahumada.fuse.descuentos.api.resources.json.Producto;
import cl.ahumada.fuse.utils.Constantes;
import cl.ahumada.fuse.utils.JSonUtilities;

public class GeneraAllRequest implements Processor {

	protected Logger logger = Logger.getLogger(getClass());
	protected Long transactionCode=778190903140154546l;
	protected String storeID;
	protected Long cashierId=777l;
	protected Long terminalId=1l;
	protected String posType="V1.0";
	DateFormat CARTENTRY_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void process(Exchange exchange) throws Exception {
		logger.info(String.format("GeneraAllRequest.process: request class %s", exchange.getIn().getBody().getClass().getSimpleName()));
		DescuentosRequest request = (DescuentosRequest)exchange.getIn().getBody();
		/*
		// arma todos los request y los coloca en el header 
		// deja en el body el String del CartEntryRequest para invocar al Rest
		
		BalanceInquiry
		CartEntryRequest
		*/
		
		BalanceInquiryRequest balanceInquiryReq = factoryBalanceInquiry(request);
		CartEntryRequest cartEntryReq = factoryCartEntry(request);
		
		logger.info(String.format("GeneraAllRequest.process: balanceInquiryReq: %s", balanceInquiryReq));
		logger.info(String.format("GeneraAllRequest.process: cartEntryReq: %s", cartEntryReq));
		Map<String,Object> servicios = new HashMap<String,Object>();
		Map<String,Object> responses = new HashMap<String,Object>();
		
		if (balanceInquiryReq != null)
			servicios.put("BalanceInquiryRequest", JSonUtilities.getInstance().java2json(balanceInquiryReq));
		servicios.put("CartEntryRequest", JSonUtilities.getInstance().java2json(cartEntryReq));
		servicios.put("DescuentosRequest", request);

		exchange.getMessage().setHeader("request", servicios);
		exchange.getMessage().setHeader("responses", responses);
		
		
		// colocar el XML de servicios en el body para el split
		Servicios apis = new Servicios();
		
		if (balanceInquiryReq != null)
			apis.addServicio("BalanceInquiryRequest", JSonUtilities.getInstance().java2json(balanceInquiryReq));
		apis.addServicio("CartEntryRequest", JSonUtilities.getInstance().java2json(cartEntryReq));
		String xml = JSonUtilities.getInstance().java2Xml(apis);
		logger.info(String.format("GeneraAllRequest.process:XML:%s", xml));
		exchange.getMessage().setBody(xml);
	}

	private BalanceInquiryRequest factoryBalanceInquiry(DescuentosRequest request) {
		BalanceInquiryRequest biRequest = null;
		if (request != null) {
			String loyaltyIdentifierNo=request.rut;
			if (loyaltyIdentifierNo == null || loyaltyIdentifierNo.isEmpty())
				return null;
			SimpleDateFormat sdfOut = new SimpleDateFormat("yyyyMMddHHmmss");
			String transactionDate=sdfOut.format(new Date());
			SimpleDateFormat sdfOut1 = new SimpleDateFormat("yyMMddHHmmssSSS");
			transactionCode = Long.valueOf(sdfOut1.format(new Date()));
			CommonRequest commonRequest = new CommonRequest(storeID,transactionCode,loyaltyIdentifierNo,cashierId,transactionDate,terminalId,posType);
			biRequest = new BalanceInquiryRequest(commonRequest);
		}
		return biRequest;
	}

	private CartEntryRequest factoryCartEntry(DescuentosRequest request) {
		CartEntryRequest prRequest = null;
		if (request != null) {
			Integer store = Integer.valueOf(request.getNumeroLocal());
			String trxStartTime = CARTENTRY_FORMATTER.format(new Date());
			String trxNumber = String.format("%d",new Date().getTime());
			prRequest = new CartEntryRequest(store, trxStartTime, trxNumber, factoryItems(request.getProducto()));
			
		}
		return prRequest;
	}
	
	private ItemReq[] factoryItems(Producto[] productos) {
		if (productos == null)
			return null;
		List<ItemReq> lista = new ArrayList<ItemReq>();
		for (Producto prod : productos) {
			String itemCode = String.format("%d", Constantes.toLong(prod.getCodigoProducto()));
			Integer quantitySold = (int) prod.getCantidad();
			Integer extendedPrice = (int) (prod.getPrecioUnitario() * prod.getCantidad());

			lista.add(new ItemReq(itemCode, quantitySold, extendedPrice));
		}
		return lista.toArray(new ItemReq[0]);
	}

}
