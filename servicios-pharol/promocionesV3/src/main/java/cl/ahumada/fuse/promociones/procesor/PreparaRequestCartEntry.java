package cl.ahumada.fuse.promociones.procesor;

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

import cl.ahumada.fuse.promociones.api.resources.PromocionesRequest;
import cl.ahumada.fuse.promociones.api.resources.json.CartEntryRequest;
import cl.ahumada.fuse.promociones.api.resources.json.ItemReq;
import cl.ahumada.fuse.promociones.api.resources.json.PharolError;
import cl.ahumada.fuse.promociones.api.resources.json.Producto;
import cl.ahumada.fuse.utils.Constantes;

public class PreparaRequestCartEntry implements Processor {

	protected Logger logger = Logger.getLogger(getClass());
	protected final String retCode = "retCode";
	DateFormat CARTENTRY_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * Toma el request leido por el servicio PromocionesV3RestService lo coloca en el map que transporta datos
	 * 
	 * Crea el request para invocar al CartEntry y lo deja en el Body
	 */
	@Override
	public void process(Exchange exchange) throws Exception {
		
		PromocionesRequest promocionesRequest = (PromocionesRequest) exchange.getIn().getBody();
		logger.info(String.format("process: inObj=%s", promocionesRequest));
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("promocionesRequest", promocionesRequest);
		CartEntryRequest cartEntryRequest = null;
		//String jsonCartEntry;
		try {
			// armar el request para cartEntry con datos recividos desde PedidosV3
			cartEntryRequest = factoryCartEntryRequest(promocionesRequest);
			/*
			jsonCartEntry = JSonUtilities.getInstance().java2json(cartEntryRequest);
			logger.info(String.format("process: cartEntryRequest=%s", jsonCartEntry));
			exchange.getIn().setBody(jsonCartEntry, String.class);
			*/
			exchange.getIn().setBody(cartEntryRequest, CartEntryRequest.class);
		} catch (Exception e) {
			logger.error("process", e);
			map.put(retCode, getJsonError(e.getMessage()));
		}
		exchange.getIn().setHeader("map", map);
	}

	private CartEntryRequest factoryCartEntryRequest(PromocionesRequest promocionesRequest) {
		Integer store = promocionesRequest.getNumeroLocal();
		String trxStartTime = CARTENTRY_FORMATTER.format(new Date());
		String trxNumber = String.format("%d",new Date().getTime());

		return new CartEntryRequest(store, trxStartTime, trxNumber, factoryItems(promocionesRequest.getProducto()));
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

	protected PharolError getJsonError (String errorMsg) {
		PharolError jsonError = new PharolError("ERROR APLICACION", errorMsg);
		return jsonError;
	}
}
