package cl.ahumada.fuse.promociones.procesor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.promociones.api.resources.PromocionesRequest;
import cl.ahumada.fuse.promociones.api.resources.PromocionesResponse;
import cl.ahumada.fuse.promociones.api.resources.json.CartEntryResponse;
import cl.ahumada.fuse.promociones.api.resources.json.Descuento;
import cl.ahumada.fuse.promociones.api.resources.json.ItemResp;
import cl.ahumada.fuse.promociones.api.resources.json.Producto;
import cl.ahumada.fuse.promociones.api.resources.json.Reward;
import cl.ahumada.fuse.utils.Constantes;
import cl.ahumada.fuse.utils.json.JSonUtilities;

/**
 * @author fernando
 * 
 * Recupera la respuesta desde el Body y con el PromocionesRequest recuperado desde el Header arma la respuesta y la deja en el Body
 * 
 */
public class GeneraRespuestaPromocionesV3 implements Processor {

	protected Logger logger = Logger.getLogger(getClass());

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		
		CartEntryResponse cartEntryResponse = (CartEntryResponse) exchange.getIn().getBody();
		Map<String, Object> map = (Map<String, Object>) exchange.getIn().getHeader("map");
		PromocionesRequest promocionesRequest = (PromocionesRequest)map.get("promocionesRequest");
		logger.info(String.format("PromocionesRequest:\n%s", promocionesRequest));
		
		String jsonResponse = factoryResponsePromociones(cartEntryResponse, promocionesRequest.getProducto());
		exchange.getIn().setBody(jsonResponse);
	}

	private String factoryResponsePromociones(CartEntryResponse cartEntryResponse, Producto[] productosReq) throws Exception {
		List<Producto> productos = new ArrayList<Producto>();
		if (cartEntryResponse.getStatus() != null && "OK".equalsIgnoreCase(cartEntryResponse.getStatus())) {
			logger.debug(String.format("CartEntryResponse:\n%s", prettyJson(cartEntryResponse)));
			for (ItemResp item : cartEntryResponse.getItems()) {
				Object codigoProducto = item.getItemCode();
				long cantidad = item.getQuantitySold();
				long precioUnitario = findProducto(codigoProducto, productosReq);
				long total = cantidad * precioUnitario;
				Integer entryId = item.getEntryID();
				Descuento[] descuentos = getDescuentos(entryId, cartEntryResponse.getRewards());

				Producto producto = new Producto(String.format("%d", Constantes.toLong(codigoProducto)), cantidad, precioUnitario, total, descuentos);
				productos.add(producto);
			}
			PromocionesResponse pr = new PromocionesResponse(productos.toArray(new Producto[0]));
			logger.info(String.format("factoryResponsePromociones: PromocionesResponse:\n%s", prettyJson(pr)));
			return prettyJson(pr);
		} else {
			return "No viene respuesta";
		}
	}

	private String prettyJson(Object data) {
		
		try {
			return JSonUtilities.getInstance().java2json(data);
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	private long findProducto(Object codigoProducto, Producto[] productosReq) {
		for (Producto prod : productosReq) {
			if (prod.codigoProducto != null && prod.getCodigoProducto().equals(Double.valueOf((String) codigoProducto))) {
				return prod.getPrecioUnitario();
			}
		}
		return 0;
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
