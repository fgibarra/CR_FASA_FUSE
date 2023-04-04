package cl.ahumada.fuse.descuentos.procesor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.descuentos.api.resources.DescuentosRequest;
import cl.ahumada.fuse.descuentos.api.resources.DescuentosResponse;
import cl.ahumada.fuse.descuentos.api.resources.json.BalanceInquiryRequest;
import cl.ahumada.fuse.descuentos.api.resources.json.BasketItem;
import cl.ahumada.fuse.descuentos.api.resources.json.CalculateDiscountResponse;
import cl.ahumada.fuse.descuentos.api.resources.json.CartEntryResponse;
import cl.ahumada.fuse.descuentos.api.resources.json.CommonRequest;
import cl.ahumada.fuse.descuentos.api.resources.json.Descuento;
import cl.ahumada.fuse.descuentos.api.resources.json.Discount;
import cl.ahumada.fuse.descuentos.api.resources.json.ItemResp;
import cl.ahumada.fuse.descuentos.api.resources.json.Producto;
import cl.ahumada.fuse.descuentos.api.resources.json.Reward;
import cl.ahumada.fuse.utils.Constantes;
import cl.ahumada.fuse.utils.JSonUtilities;

public class GeneraRespuesta implements Processor {

	CartEntryResponse cartEntryResponse = null;
	CalculateDiscountResponse calculateDiscountResponse = null;
	DescuentosRequest descuentosRequest = null;
	BalanceInquiryRequest balanceInquiry = null;
	CommonRequest commonRequest = null;
	protected Map<Object, Producto> mapProductosRequest = new HashMap<Object, Producto>();
	protected Logger logger = Logger.getLogger(getClass());
	
	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		balanceInquiry = null;
		Map<String, Object> responses = (Map<String, Object>) exchange.getIn().getHeader("responses");
		Map<String, Object> request = (Map<String, Object>) exchange.getIn().getHeader("request");
		cartEntryResponse = (CartEntryResponse)responses.get("cartEntryResponse");
		calculateDiscountResponse = (CalculateDiscountResponse)responses.get("calculateDiscountResponse");
		descuentosRequest = (DescuentosRequest)request.get("DescuentosRequest");
		String balanceInquiryStr = (String) request.get("BalanceInquiryRequest");
		if (balanceInquiryStr != null)
			balanceInquiry = (BalanceInquiryRequest)JSonUtilities.getInstance().
					json2java(String.format("{\"BalanceInquiryRequest\": %s}", 
							balanceInquiryStr), 
							BalanceInquiryRequest.class);
		if (balanceInquiry != null)
			commonRequest = balanceInquiry.commonRequest;
		DescuentosResponse descuentosResponse = null;
		
		pueblaMapProductosRequest(descuentosRequest);
		if (hayBalanceInquiry()) {
			logger.info("GeneraRespuesta: con BalanceInquiry");
			try {
				CalculateDiscountResponse response = calculateDiscountResponse;
				long transactionCode = commonRequest.transactionCode;
				Producto[] producto = new Producto[response.responseBasketItems.length];
				for (int i = 0; i < response.responseBasketItems.length; i++) {
					BasketItem item = response.responseBasketItems[i];
					//logger.debug(String.format("ITEM %S", item.toString()));
					Object codigoProducto = Constantes.toLong(item.code);
					long cantidad = getCantidad(item.code);
					long precioUnitario = getPrecioUnitario(item.code);
					long total = getTotal(item.code);
					List<Descuento> descuentosList = new ArrayList<Descuento>();
					if (item.discounts != null) {
						for (Discount discount : item.discounts) {
							String type = discount.type;
							long valorDescuento = discount.value!=null?discount.value.longValue() : 0;
							boolean aplicar = discount.apply;
							/*
							logger.debug(String.format("Genera descuento SALIDA producto=%d con: type=%s valorDescuento=%d, codigoDescuento=%s, descripcionDescuento=%s, aplicar=%b",
									codigoProducto, type, valorDescuento, discount.code, discount.description, aplicar));
							*/
							Descuento descuentos = new Descuento(type, valorDescuento, discount.code, discount.description, aplicar);
							descuentosList.add(descuentos);
						}
					}

					Producto prod = new Producto(Constantes.toLong(codigoProducto), cantidad, precioUnitario, total, (Descuento[])descuentosList.toArray(new Descuento[0]));
					producto[i] = prod;
				}

				descuentosResponse = new DescuentosResponse(transactionCode, producto, response.posMessages);

			} catch (Exception e) {
				logger.error("process", e);
			}
		} else {
			logger.info("GeneraRespuesta: sin BalanceInquiry");
			// no se invoco a CalculateDiscount (no rut)
			// armar respuesta con request y cartEntry
				descuentosResponse = generarDescuentosConCartEntry();
		}
		logger.info(String.format("GeneraRespuesta: JSON que devuelve:\n%s", JSonUtilities.getInstance().java2json(descuentosResponse)));
		exchange.getIn().setBody(JSonUtilities.getInstance().java2json(descuentosResponse));
		
		logger.info(String.format("GeneraRespuesta: CamelSplitComplete: %b CamelSplitIndex=%d",
				exchange.getProperty("CamelSplitComplete", Boolean.class), 
				exchange.getProperty("CamelSplitIndex", Integer.class)));
	}

	private boolean hayBalanceInquiry() {
		return balanceInquiry != null;
	}

	private DescuentosResponse generarDescuentosConCartEntry() {
		DescuentosResponse descuentosResponse = null;
		try {
			//jsonPromociones = String.format("{\"PromocionesResponse\":%s}", jsonPromociones);
			if (cartEntryResponse != null && cartEntryResponse.items != null) {
				List<Producto> listaProductos = new ArrayList<Producto>();
				for (ItemResp item : cartEntryResponse.items) {
					String codigoProducto = item.itemCode;
					Long cantidad = getCantidad(item.itemCode);
					Long precioUnitario = getPrecioUnitario(item.itemCode);
					Long total = getTotal(item.itemCode);
					Descuento[] descuentos = getDescuentos(Integer.valueOf(codigoProducto), cartEntryResponse.rewards);
					listaProductos.add( new Producto(codigoProducto, cantidad, precioUnitario, total, descuentos));
				}
				descuentosResponse = new DescuentosResponse(0l, listaProductos.toArray(new Producto[0]), null);
			} else {
				descuentosResponse = new DescuentosResponse(1l, null, null);
			}
		} catch (Exception e) {
			logger.error("process", e);
		}
		return descuentosResponse;
	}

	protected void pueblaMapProductosRequest(DescuentosRequest wsRequest) {
		mapProductosRequest.clear();
		if (wsRequest.producto != null)
			for (Producto p : wsRequest.producto) {
				mapProductosRequest.put(p.codigoProducto.toString(), p);
				//logger.info(String.format("pueblaMapProductosRequest: codigo: %s", p.codigoProducto.toString()));
			}
	}

	protected long getCantidad(String code) {
		//logger.info(String.format("getCantidad: codigo: %s", code));
		Producto p = mapProductosRequest.get(code);
		return p.cantidad;
	}

	protected long getPrecioUnitario(String code) {
		//logger.info(String.format("getPrecioUnitario: codigo: %s", code));
		Producto p = mapProductosRequest.get(code);
		return p.precioUnitario;
	}

	protected long getTotal(String code) {
		//logger.info(String.format("getTotal: codigo: %s", code));
		Producto p = mapProductosRequest.get(code);
		return p.total;
	}

	private Descuento[] getDescuentos(Integer entryId, Reward[] rewards) {
		List<Descuento> lista = new ArrayList<Descuento>();
		for (Reward rew : rewards) {
			if (entryId == rew.getEntryID()) {
				String type = rew.getRewardType();
				long valorDescuento = Math.abs(rew.getExtendedRewardAmount());
				Object codigoDescuento = rew.getPromotionCode();
				String descripcionDescuento = rew.getPromotionDescription();

				lista.add(new Descuento(type, valorDescuento, codigoDescuento, descripcionDescuento, true));
			}
		}
		return lista.toArray(new Descuento[0]);
	}
}
