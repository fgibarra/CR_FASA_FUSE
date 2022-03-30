package cl.ahumada.peya.requester.peyasdk.threads;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.pedidosya.reception.sdk.ApiClient;
import com.pedidosya.reception.sdk.exceptions.ApiException;
import com.pedidosya.reception.sdk.models.Order;
import com.pedidosya.reception.sdk.models.Product;
import com.pedidosya.reception.sdk.models.Reconciliation;
import com.pedidosya.reception.sdk.models.Section;

import cl.ahumada.esb.dto.pharol.consultastock.ConsultaStockResponse;
import cl.ahumada.esb.dto.pharol.json.Stock;
import cl.ahumada.peya.requester.main.PeyaController;
import cl.ahumada.peya.requester.peyasdk.Actions;
import cl.ahumada.peya.requester.servicios.ServiciosdeBus;

/**
 * @author fernando
 *
 * Ejecuta en el mismo thread del Requester
 */
public class ProcesaOrden {

	private ApiClient apiClient;
	private Logger logger = Logger.getLogger(getClass());
	private ServiciosdeBus serviciosdeBus;
	private Order order;
	private Actions action;
	
	public ProcesaOrden(Order order) {
		super();
		this.order = order;
		this.apiClient = PeyaController.getInstance().getApiClient();
		this.action = new Actions(apiClient);
	}


	/**
	 * @param callback
	 * 
	 * - Consulta stock para surtir la orden
	 * - Si el stock es 0 para todos los productos se rechaza la orden
	 * - Si se puede surtir total o parcialmente la orden:
	 * 		- Se envia la orden al monitor loigistico
	 * 		- se supende el thread hasta que llegue la confirmacion/rechazo desde el monitor logistico
	 * 		- se invoca al callback con la confirmacion/rechazo enviada desde el monitor logistico
	 */
	public void procesa(ConfirmaCallback callback) {
		
		
		Integer confirma = 0;
		
		callback.onConfirmaEnd(order, confirma);
	}

	@SuppressWarnings("unchecked")
	public boolean stockMenorCritico(Map<String, Object> map) {
		// si lo pedido menos el stock es mayor que el critico -> true
		List<Long> productosSinStock = (List<Long>) map.get(ServiciosdeBus.PRODUCTOS_SIN_STOCK_KEY);
		ConsultaStockResponse stockResponse = (ConsultaStockResponse)map.get("StockResponse");

		Integer stockCritico = serviciosdeBus.getStockCritico();
		List<Long> listaSacar = new ArrayList<Long>();

		for (Long sku : productosSinStock) {
			// busca lo que hay, si esto es mayor que el critico se saca de la lista de deshabilitar
			Long cantidadEnStock = buscaCantidadEnStock(sku, stockResponse);
			for (Stock pedido : stockResponse.local[0].stock) {
				long cpPedido = pedido.codigoProducto;
				if (sku == cpPedido) {
					//Long quedarian = pedido.cantidad - cantidadEnStock;
					if (/*cantidadEnStock == 0 ||*/ cantidadEnStock >= stockCritico && cantidadEnStock > 0) {
						// se deshabilita si lo que tengo es menor que el critico o no tengo nada
						logger.debug(String.format("stockMenorCritico: sku: %d cantidad pedida %d cantidad en stock %d stockCritico: %d NO se deshabilita",
								sku, pedido.cantidad, cantidadEnStock, stockCritico));
						listaSacar.add(sku);
					} else {
						logger.debug(String.format("stockMenorCritico: sku: %d cantidad pedida %d cantidad en stock %d stockCritico: %d se deshabilita",
								sku, pedido.cantidad, cantidadEnStock, stockCritico));
					}
					break;
				}
			}
		}
		for (Long skuSacar : listaSacar)
			productosSinStock.remove(skuSacar);

		if (productosSinStock.size() > 0) {
			StringBuffer sb = new StringBuffer();
			sb.append("VA a deshabilitar los siguientes sku:");
			for (Long sku : productosSinStock) {
				sb.append(String.format(" %d,", sku));
			}
			logger.debug(sb.toString());
		}
		return !productosSinStock.isEmpty();
	}

	public static Long buscaCantidadEnStock(Long sku, ConsultaStockResponse stockResponse) {
		for(Stock stock : stockResponse.local[0].stock) {

			if ( sku == stock.codigoProducto)
				return stock.cantidad;
		}
		return 0l;
	}
	
	public void disableProduct(long restaurantId, String sku, String name, String sectionCode) throws ApiException {

        Product product = new Product();
        Section section = new Section();
        
        section.setIntegrationCode(sectionCode); // order->product-> section->integrationCode este es el sku de la sección
        product.setIntegrationCode(sku); // order->product->integrationCode  este es el sku del producto
      
       
        product.setName(name); //order->product->name
        product.setEnabled(false);
        product.setSection(section);
   
        try {
            apiClient.getProductsClient().update(product, restaurantId);
            System.out.println("Create Product: " + name);
        } catch (ApiException e) {
            System.out.println("Error Product: " + name + ": " + e.getMessage());
        }

    }

	public void doReconciliation() throws ApiException {
		        Reconciliation reconciliation = new Reconciliation();
        reconciliation.setOrderId(141101492);
        reconciliation.setTotalGross(100);
        reconciliation.RemovalModificationProductBuilder("7795306"); // para este este necesitas: solamente del sku del producto a remover, SKU = order->details->product-->integrationCode
        reconciliation.ChangeModificationProductBuilder("762230086495", 1, 35); // para este este necesitas: sku, cantidad (quantity) y el unirtPrice del producto.
        
        apiClient.getOrdersClient().reconcile(reconciliation, 101248); // este es el metodo que aplica la reconciliación, recibe el objeto reconciliation armado previamente y el restarantID que es el identificador de la sucursal
        // se encuentra en:  order.getRestaurant().getId()
	}
}
