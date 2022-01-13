package cl.ahumada.fuse.productos.procesor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.productos.api.resources.ConsultaProductoResponse;
import cl.ahumada.fuse.productos.api.resources.json.Producto;
import cl.ahumada.fuse.productos.api.resources.json.Promocion;


public class GeneraRespuestaWs implements Processor {

	Logger logger = Logger.getLogger(getClass());

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		LinkedHashMap<String,Object> map = (LinkedHashMap<String, Object>) exchange.getIn().getBody();
		Map<String,Producto> productos = (Map<String, Producto>) map.get("productos");
		Map<String, List<Promocion>> promociones = (Map<String, List<Promocion>>) map.get("promociones");
		List<Producto> listaProductos = new ArrayList<Producto>();
		
		for(String keyProducto : productos.keySet()) {
			Producto producto = productos.get(keyProducto);
			List<Promocion> promo= promociones.get(keyProducto);
			if (promo != null)
				producto.setPromocion(promo.get(0));
			listaProductos.add(producto);
		}
		
		ConsultaProductoResponse response = new ConsultaProductoResponse(listaProductos.toArray(new Producto[0]));
		exchange.getIn().setBody(response);
	}

}
