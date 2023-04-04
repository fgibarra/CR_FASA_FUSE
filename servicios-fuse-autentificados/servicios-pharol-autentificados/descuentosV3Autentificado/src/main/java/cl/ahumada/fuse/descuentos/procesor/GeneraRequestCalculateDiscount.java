package cl.ahumada.fuse.descuentos.procesor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.PropertyInject;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.descuentos.api.resources.DescuentosRequest;
import cl.ahumada.fuse.descuentos.api.resources.json.BalanceInquiryRequest;
import cl.ahumada.fuse.descuentos.api.resources.json.BasketItem;
import cl.ahumada.fuse.descuentos.api.resources.json.CalculateDiscountRequest;
import cl.ahumada.fuse.descuentos.api.resources.json.CartEntryResponse;
import cl.ahumada.fuse.descuentos.api.resources.json.CommonRequest;
import cl.ahumada.fuse.descuentos.api.resources.json.Coupon;
import cl.ahumada.fuse.descuentos.api.resources.json.Descuento;
import cl.ahumada.fuse.descuentos.api.resources.json.Discount;
import cl.ahumada.fuse.descuentos.api.resources.json.ItemResp;
import cl.ahumada.fuse.descuentos.api.resources.json.Producto;
import cl.ahumada.fuse.descuentos.api.resources.json.Reward;
import cl.ahumada.fuse.utils.JSonUtilities;

public class GeneraRequestCalculateDiscount implements Processor {

    @PropertyInject(value = "storeID", defaultValue="778")
	protected String storeID;
    @PropertyInject(value = "cashierId", defaultValue="7777")
	protected long cashierId=7777;
    @PropertyInject(value = "terminalId", defaultValue="001")
	protected long terminalId=1;
    @PropertyInject(value = "posType", defaultValue="V1.0")
	protected String posType="V1.0";
    @PropertyInject(value = "abfCode", defaultValue="ABF_1")
	protected String abfCode="ABF_1"; // prop
    @PropertyInject(value = "discountType", defaultValue="DP")
	protected String discountType; //prop
    @PropertyInject(value = "discountsRestricted", defaultValue="false")
	protected Boolean discountsRestricted = false;
    @PropertyInject(value = "restricted", defaultValue="false")
	protected Boolean restricted = false;
    @PropertyInject(value = "redeemed", defaultValue="false")
	protected Boolean redeemed = false;
    @PropertyInject(value = "couponUsageStatus", defaultValue="U")
    protected String couponUsageStatus = "U";
    
	protected Long transactionCode=null;

	private Logger logger = Logger.getLogger(getClass());
	
	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		Map<String, Object> responses = (Map<String, Object>) exchange.getIn().getHeader("responses");
		Map<String, Object> request = (Map<String, Object>) exchange.getIn().getHeader("request");
		CartEntryResponse cartEntryResponse = (CartEntryResponse)responses.get("cartEntryResponse");
		DescuentosRequest descuentosRequest = (DescuentosRequest)request.get("DescuentosRequest");
		String valor = (String)request.get("BalanceInquiryRequest");
		BalanceInquiryRequest balanceInquiryRequest = null;
		
		if (valor != null) {
			balanceInquiryRequest = (BalanceInquiryRequest)JSonUtilities.getInstance().json2java(String.format("{\"BalanceInquiryRequest\":%s}",valor), BalanceInquiryRequest.class);
			transactionCode = balanceInquiryRequest.commonRequest.transactionCode;
		}
		logger.info(String.format("getJsonToSend: para armar el request usa CartEntryResponse\n%s\nDescuentosRequest:\n%s\ntransactionCode=%d",
				cartEntryResponse, descuentosRequest,transactionCode));

		String loyaltyIdentifierNo = descuentosRequest!=null?descuentosRequest.rut:"";
		if (loyaltyIdentifierNo == null || loyaltyIdentifierNo.isEmpty())
			return;
		
		SimpleDateFormat sdfOut = new SimpleDateFormat("yyyyMMddHHmmss");
		String transactionDate = sdfOut.format(new Date());
		if (transactionCode == null) {
			logger.error("GeneraRequestCalculateDiscountAction: NO recupero transactionCode desde el meessage");
			SimpleDateFormat sdfOut1 = new SimpleDateFormat("yyMMddHHmmssSSS");
			transactionCode = Long.valueOf(sdfOut1.format(new Date()));
		}
		CommonRequest commonRequest = new CommonRequest(storeID, transactionCode, loyaltyIdentifierNo,
				cashierId, transactionDate, terminalId, posType);
		BasketItem[] basketItems;
		List<Coupon> cupones = new ArrayList<Coupon>();
		Coupon[] coupons;
		
		if (cartEntryResponse.getItems() != null) {
			logger.info("USA PromocionesResponse para armar el request");
			basketItems = new BasketItem[cartEntryResponse.getItems().length];
			for (int i = 0; i < cartEntryResponse.getItems().length; i++) {
				ItemResp prod = cartEntryResponse.getItems()[i];
				String code = prod.itemCode;
				int lineItemId = i+1;
				int quantity = (int) prod.quantitySold;
				Float productValue = findPrecioUnitarioProducto(code, descuentosRequest.producto);
				Float totalValue = (float) (quantity * productValue);
				Float valuePaidWithPoints = 0f;
				long pointsValue = 0;
				List<Discount> discountsArray = getDescuentos(Integer.valueOf(code), cartEntryResponse.rewards);
				Discount[] discounts = null;
				if (discountsArray.size() > 0)
					discounts = discountsArray.toArray(new Discount[0]);

				BasketItem item = new BasketItem(code, lineItemId, quantity, productValue, totalValue,
						discountsRestricted, restricted, redeemed, valuePaidWithPoints, pointsValue, discounts);
				basketItems[i] = item;
				logger.info(String.format("ARMA request CalculateDiscount: producto=%s discountsArray.size()=%d",
						code, discountsArray.size()));
			}
			coupons = cupones.toArray(new Coupon[0]);
		} else {
			// No hay CartEntryResponse
			logger.info("USA descuentosRequest para armar el request");
			basketItems = new BasketItem[descuentosRequest.producto.length];
			for (int i = 0; i < descuentosRequest.producto.length; i++) {
				Producto prod = descuentosRequest.producto[i];
				String code = null;
				if (prod.codigoProducto instanceof Double)
					code = String.format("%d", ((Double)prod.codigoProducto).longValue());
				else
					code = String.format("%d", Double.valueOf(prod.codigoProducto.toString()).longValue());
				int lineItemId = i+1;
				int quantity = (int) prod.cantidad;
				Float productValue = (float) prod.precioUnitario;
				Float totalValue = (float) prod.total;
				Float valuePaidWithPoints = 0f;
				long pointsValue = 0;
				List<Discount> discountsArray = new ArrayList<Discount>();
				if (prod.descuentos != null) {
					for (int j=0; j < prod.descuentos.length; j++) {
						Descuento descuento = prod.descuentos[j];
						if (descuento.valorDescuento != 0) {
							if (descuento.valorDescuento < 0)
								descuento.valorDescuento = -descuento.valorDescuento;
							Discount discount = new Discount(discountType, (float)descuento.valorDescuento, true, descuento.codigoDescuento, descuento.descripcionDescuento);
							discountsArray.add(discount);
						}
					}
				}
				Discount[] discounts = null;
				if (discountsArray.size() > 0)
					discounts = discountsArray.toArray(new Discount[0]);

				BasketItem item = new BasketItem(code, lineItemId, quantity, productValue, totalValue,
						discountsRestricted, restricted, redeemed, valuePaidWithPoints, pointsValue, discounts);
				basketItems[i] = item;
				logger.info(String.format("ARMA request CalculateDiscount: producto=%s prod.descuentos.length=%d discountsArray.size()=%d",
						code, (prod.descuentos!=null?prod.descuentos.length:0), discountsArray.size()));
			}
			coupons = cupones.toArray(new Coupon[0]);
		}

		CalculateDiscountRequest calculateDiscountRequest = new CalculateDiscountRequest(commonRequest, abfCode, basketItems, coupons);
		logger.info(String.format("JSON invocar CalculateDiscount\n%s", calculateDiscountRequest));
		request.put("calculateDiscountRequest", calculateDiscountRequest);
		
		exchange.getIn().setBody(calculateDiscountRequest.toString());
	}

	private List<Discount> getDescuentos(Integer entryId, Reward[] rewards) {
		List<Discount> lista = new ArrayList<Discount>();
		for (Reward rew : rewards) {
			if (entryId == rew.getEntryID()) {
				String type = rew.getRewardType();
				Float valorDescuento = (float) Math.abs(rew.getExtendedRewardAmount());
				Object codigoDescuento = rew.getPromotionCode();
				String descripcionDescuento = rew.getPromotionDescription();

				lista.add(new Discount(type, valorDescuento, true, codigoDescuento, descripcionDescuento));
			}
		}
		return lista;
	}

	private Float findPrecioUnitarioProducto(String codigoProducto, Producto[] productosReq) {
		for (Producto prod : productosReq) {
			if (prod.codigoProducto != null && prod.getCodigoProducto().equals(Double.valueOf((String) codigoProducto))) {
				return (float) prod.getPrecioUnitario();
			}
		}
		return (float) 0;
	}

}
