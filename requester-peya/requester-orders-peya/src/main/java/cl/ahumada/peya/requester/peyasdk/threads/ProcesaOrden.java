package cl.ahumada.peya.requester.peyasdk.threads;

import java.io.IOException;
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
import cl.ahumada.peya.requester.servicios.ObjectFactory;
import cl.ahumada.peya.requester.servicios.Semaforo;
import cl.ahumada.peya.requester.servicios.ServiciosdeBus;
import cl.ahumada.peya.requester.servicios.dto.ReconciliaDTO;

/**
 * @author fernando
 *
 *         Ejecuta en el mismo thread del Requester
 */
public class ProcesaOrden {

	private ApiClient apiClient;
	private Logger logger = Logger.getLogger(getClass());
	private ServiciosdeBus serviciosdeBus;
	private Order order;
	private Actions action;
	private Semaforo semaforo;
	private ObjectFactory objectFactory;
	
	public ProcesaOrden(Order order) throws IOException {
		super();
		this.order = order;
		this.apiClient = PeyaController.getInstance().getApiClient();
		this.action = new Actions(apiClient);
		this.semaforo = new Semaforo(0);
		this.serviciosdeBus = new ServiciosdeBus(apiClient);
		this.objectFactory = new ObjectFactory();
	}

	/**
	 * 1.- Determinar stock para cada producto en la orden 
	 * 2.- Si el stock es 0 para
	 * todos los productos de la orden, se rechaza y retorna para el fin del proceso
	 * 3.- Invoca al monitor logistico y se suspende esperando su respuesta 4.-
	 * Cuando se recibe la respuesta se reactiva y se invoca al callback 4.1.- si el
	 * monitor acepta la orden la confirma 4.2.- si hay productos sin stock o stock
	 * insuficiente se reconcilia la orden
	 * 
	 * @param callback
	 */
	public void procesa(ConfirmaCallback callback) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("order", order);
		Integer confirma = 0;

		if (serviciosdeBus.confirmaStock(map)) {
			logger.info(String.format("se CONFIRMA orden: %d", order.getId()));
			confirma = suplirTotalParcial(map);
			logger.info(String.format("se CONFIRMA se debe reconciliar %s", confirma==2?"SI":"NO"));
		} else {
			logger.info(String.format("se RECHAZA orden: %d", order.getId()));
		}
		// Si hay stock para alguno de los productos de la orden, invoca al monitor
		if (confirma > 0) {
			Integer confirmaMonitor = serviciosdeBus.invocaMonitorLogistico(map, semaforo);
			// queda suspendido hasta que el monitor responda
			if (confirmaMonitor == null) {
				// no se pudo invocar al monitor logistico. Rechazar la orden
				confirma = 0;
			} else if (confirmaMonitor == 0) {
				// si el monitor logistico rechaza la orden, se rechaza, en caso contrario 
				// se procesa de acuerdo al stock
				confirma = confirmaMonitor;
			}
		}

		callback.onConfirmaEnd(map, confirma);
	}

	/**
	 * @param map
	 * @return
	 * 
	 * la funcion 'confirma' coloca en map PRODUCTOS_STOCK_PARCIAL_KEY valor.
	 */
	private Integer suplirTotalParcial(Map<String, Object> map) {
		return (Boolean) map.get(ServiciosdeBus.PRODUCTOS_STOCK_PARCIAL_KEY) ? Integer.valueOf(1) : Integer.valueOf(2);
	}

	/**
	 * Invocado desde el callback cuando se debe rechazar una orden
	 * 
	 * @param map
	 */
	public void rechazaOrden(Map<String, Object> map) {
		try {
			action.getReject(order);
		} catch (ApiException e) {
			logger.error(String.format("rechazaOrden: orden %d", order.getId()), e);
		}
		serviciosdeBus.informeRechazo(map);
		if (stockMenorCritico(map)) {
			serviciosdeBus.deshabilitaProductos(map);
		}
	}

	/**
	 * Invocado desde el callback cuando se puede suplir total o parcialmente la
	 * orden
	 * 
	 * @param map
	 */
	public void confirmaOrden(Map<String, Object> map) {
		try {
			//
			action.getConfirm(order);
			// hacer el pedido
			//serviciosdeBus.realizarPedido(map);
		} catch (ApiException e) {
			logger.error(String.format("confirmaOrden: orden %d", order.getId()), e);
			serviciosdeBus.undoPedido(map);
			return;
		}
		serviciosdeBus.actualizaHabilitacionProductos(map);
	}

	/**
	 * Invocado desde el callback cuando hay que reconciliar la orden
	 * 
	 * @param map
	 * @throws ApiException
	 */
	public void reconciliaOrden(Map<String, Object> map) {

		try {
			List<String> remueve = objectFactory.generaListaRemueve(map);
			List<ReconciliaDTO> modifica = objectFactory.generaListaModifica(map);
			Double totalOrder = objectFactory.calculaNuevoTotal(((Order) map.get(ServiciosdeBus.ORDER_KEY)).getDetails(), remueve, modifica);
			action.getConfirm(order);
			doReconciliation(order, totalOrder, remueve, modifica);

			// hacer el pedido
			//serviciosdeBus.realizarPedido(map);
		} catch (Exception e) {
			logger.error("reconciliaOrden", e);
			serviciosdeBus.undoPedido(map);
		}
	}

	@SuppressWarnings("unchecked")
	public boolean stockMenorCritico(Map<String, Object> map) {
		// si lo pedido menos el stock es mayor que el critico -> true
		List<Long> productosSinStock = (List<Long>) map.get(ServiciosdeBus.PRODUCTOS_SIN_STOCK_KEY);
		ConsultaStockResponse stockResponse = (ConsultaStockResponse) map.get("StockResponse");

		Integer stockCritico = serviciosdeBus.getStockCritico();
		List<Long> listaSacar = new ArrayList<Long>();

		if (productosSinStock != null) {
			for (Long sku : productosSinStock) {
				// busca lo que hay, si esto es mayor que el critico se saca de la lista de
				// deshabilitar
				Long cantidadEnStock = buscaCantidadEnStock(sku, stockResponse);
				for (Stock pedido : stockResponse.local[0].stock) {
					long cpPedido = pedido.codigoProducto;
					if (sku == cpPedido) {
						// Long quedarian = pedido.cantidad - cantidadEnStock;
						if (/* cantidadEnStock == 0 || */ cantidadEnStock >= stockCritico && cantidadEnStock > 0) {
							// se deshabilita si lo que tengo es menor que el critico o no tengo nada
							logger.debug(String.format(
									"stockMenorCritico: sku: %d cantidad pedida %d cantidad en stock %d stockCritico: %d NO se deshabilita",
									sku, pedido.cantidad, cantidadEnStock, stockCritico));
							listaSacar.add(sku);
						} else {
							logger.debug(String.format(
									"stockMenorCritico: sku: %d cantidad pedida %d cantidad en stock %d stockCritico: %d se deshabilita",
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
		} else
			return false;
	}

	public static Long buscaCantidadEnStock(Long sku, ConsultaStockResponse stockResponse) {
		for (Stock stock : stockResponse.local[0].stock) {

			if (sku == stock.codigoProducto)
				return stock.cantidad;
		}
		return 0l;
	}

	public void disableProduct(long restaurantId, String sku, String name, String sectionCode) throws ApiException {

		Product product = new Product();
		Section section = new Section();

		section.setIntegrationCode(sectionCode); // order->product-> section->integrationCode este es el sku de la
													// sección
		product.setIntegrationCode(sku); // order->product->integrationCode este es el sku del producto

		product.setName(name); // order->product->name
		product.setEnabled(false);
		product.setSection(section);

		try {
			apiClient.getProductsClient().update(product, restaurantId);
			System.out.println("Create Product: " + name);
		} catch (ApiException e) {
			System.out.println("Error Product: " + name + ": " + e.getMessage());
		}

	}

	/**
	 * Recibe los datos ya procesados para reconciliar la orden
	 * 
	 * @param order
	 * @param totalOrder
	 * @param remueve
	 * @param reconcilia
	 * @throws ApiException
	 */
	public void doReconciliation(Order order, Double totalOrder, List<String> remueve, List<ReconciliaDTO> reconcilia)
			throws ApiException {
		Reconciliation reconciliation = new Reconciliation();
		reconciliation.setOrderId(order.getId().intValue());
		reconciliation.setTotalGross(totalOrder);
		if (remueve != null)
			for (String codigoRemueve : remueve) {
				reconciliation.RemovalModificationProductBuilder(codigoRemueve); // para este este necesitas: solamente
																					// del sku del producto a remover,
																					// SKU =
																					// order->details->product-->integrationCode
			}
		if (reconcilia != null)
			for (ReconciliaDTO modifica : reconcilia) {
				reconciliation.ChangeModificationProductBuilder(modifica.getCodigoProducto(), modifica.getCantidad(),
						modifica.getPrecioUnitario()); // para este este necesitas: sku, cantidad (quantity) y el unirtPrice
													// del producto.
			}

		// este es el metodo que aplica la reconciliación,
		// recibe el objeto reconciliation armado previamente y el restarantID que es el identificador de la
		// local, se encuentra en: order.getRestaurant().getId()
		apiClient.getOrdersClient().reconcile(reconciliation, order.getRestaurant().getId()); 
																								// 
																								// 
	}
}
