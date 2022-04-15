package cl.ahumada.peya.requester.servicios;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pedidosya.reception.sdk.ApiClient;
import com.pedidosya.reception.sdk.models.Attachment;
import com.pedidosya.reception.sdk.models.Detail;
import com.pedidosya.reception.sdk.models.Order;
import com.pedidosya.reception.sdk.models.Product;

import cl.ahumada.esb.dto.pharol.consultastock.ConsultaStockRequest;
import cl.ahumada.esb.dto.pharol.json.Local;
import cl.ahumada.esb.dto.pharolV4.pedidos.PedidosRequest;
import cl.ahumada.esb.utils.json.JSonUtilities;
import cl.ahumada.fuse.requesterPeya.lib.monitorLogistico.MonitorLogisticoRequest;

public class ServiciosdeBus {

	private ApiClient apiClient;
	private InvocaESB invocaESB;
	private Properties integracionProps;

	public static String ORDER_KEY = "order";
	public static String PRODUCTOS_SIN_STOCK_KEY = "productosSinStock";
	public static String PRODUCTOS_STOCK0_KEY = "productosStock0";
	public static String PRODUCTOS_STOCK_PARCIAL_KEY = "productosStockParcial";
	public static String REQUEST_STOCK_KEY = "requestStock";
	public static String REQUEST_PEDIDOS_KEY = "requestPedidos";
	public static String RESULTADO_OPERACION_KEY = "resultadoOperacion";
	public static String STOCK_CRITICO_KEY = "stock_critico";
	public static String ENDPOINT_MANTIENE_STOCK_KEY = "endpoint_ws_mantiene_stock";
	public static String ENDPOINT_INFORMA_DESHABILITA_KEY = "endpoint_ws_informeDeshabilita";
	public static String ENDPOINT_STOCK_KEY = "stockEndPoint";
	public static String ENDPOINT_STOCK2_KEY = "stockEndPoint2";
	public static String ENDPOINT_PEDIDOS = "pedidosEndPoint";
	public static String ENDPOINT_MONITOR_LOGISTICO = "monitorLogisticoEndPoint";
	public static String NUMERO_ORDEN = "numeroOrden";
	public static String CONFIRMA = "confirma";


	public ServiciosdeBus(ApiClient apiClient) throws IOException {
		super();
		this.apiClient = apiClient;
		integracionProps = new Properties();
		try (final java.io.InputStream stream =
		           this.getClass().getClassLoader().getResourceAsStream("integracion.properties")){
					if (stream == null) throw new RuntimeException("stream es nulo");
					integracionProps.load(stream);
				}
		logger.info(String.format("stock_critico=%s descuentosAplicables=%s endpoint_ws_mantiene_stock=%s "+
								"endpoint_ws_informeDeshabilita=%s stockEndPoint=%s stockEndPoint2=%s pedidosEndPoint=%s",
				integracionProps.get(STOCK_CRITICO_KEY),
				integracionProps.get("descuentosAplicables"),
				integracionProps.get(ENDPOINT_MANTIENE_STOCK_KEY),
				integracionProps.get(ENDPOINT_INFORMA_DESHABILITA_KEY),
				integracionProps.get(ENDPOINT_STOCK_KEY),
				integracionProps.get(ENDPOINT_STOCK2_KEY),
				integracionProps.get(ENDPOINT_PEDIDOS),
				integracionProps.get(ENDPOINT_MONITOR_LOGISTICO)));
		invocaESB = new InvocaESB(integracionProps);
	}

	private ObjectFactory factory = new ObjectFactory();
	private Logger logger = Logger.getLogger(getClass());
	/**
	 * 1.- arma request a restapi ConsultaStock
	 * 2.- invoca al api de stock
	 * 3.- si no hay stock, retorna false
	 * 4.- arma request a restapi de pedidosV4
	 * 5.- invoca api de pedidos
	 * 6.- retorna true si pudo realizar el pedido
	 * @param map
	 * key:
	 * 		order, com.pedidosya.reception.sdk.models.Order - in
	 * 		requestStock, ConsultaStockRequest - out (usado para actualizaHabilitacionProductos)
	 * 		productosSinStock, List<Long>      - out (usado para deshabilitaProductos)
	 * @return
	 */
	public boolean confirmaStock(Map<String, Object> map) {
		Order order = (Order) map.get(ORDER_KEY);
		Long restaurantID = order.getRestaurant().getId();
		map.put("restaurantID", restaurantID);
		map.put("integracionProps", integracionProps);
		boolean confirma = false;
		try {
			Local local = factory.factoryLocal(order);

			ConsultaStockRequest requestStock = new ConsultaStockRequest(new Local[] {local});
			String json = "no pudo";
			try {
				json = JSonUtilities.getInstance().java2json(requestStock);
			} catch (JsonProcessingException e) {
				logger.error("", e);
			}
			logger.info(String.format("request Stock: %s", json));
			map.put(REQUEST_STOCK_KEY, requestStock);

			// invocar servicio consulta stock
			Boolean hayStock = Boolean.FALSE;

			try {
				hayStock = invocaESB.hayStock(map);
			} catch (Exception e1) {
				;
			}
			if (!hayStock) {
				// 20220124: el rechazo puede ser parcial. en ServiciosdeBus.PRODUCTOS_SIN_STOCK_KEY esta la lista de
				// Stock.pedido.codigoProducto sin stock
				return confirma;
			}

			// si hay stock
			confirma = true;
		} catch (Exception e) {
			logger.error("confirma", e);
		}
		return confirma;
	}
	
	public boolean realizarPedido(Map<String, Object> map) {
		boolean confirma = true;
		// invocar servicio pedidos
		PedidosRequest requestPedidos;
		try {
			requestPedidos = factory.factoryPedidosRequest(map);
			if (requestPedidos.carroCompras != null) {
				map.put(REQUEST_PEDIDOS_KEY, requestPedidos);
				String json = "no pudo";
				try {
					json = JSonUtilities.getInstance().java2json(requestPedidos);
				} catch (JsonProcessingException e) {
					logger.error("", e);
				}
				logger.info(String.format("request Pedido: %s", json));

				confirma = invocaESB.realizarPedido(map);
			}
		} catch (IOException e1) {
			confirma = false;
			logger.error("realizarPedido", e1 );
		}
		 return confirma;
	}
	//**************************************************************************************************
	/**
	 * Invocada por ProcesaOrden.procesa
	 * @param map
	 */
	public void actualizaHabilitacionProductos(Map<String, Object> map) {
		// reprocesar la consulta de stock para reinicializar los productos sin stock
		map.remove(PRODUCTOS_SIN_STOCK_KEY);
		// consultar stock que quedo despues del pedido
		// verificar el stock contra stock critico
		Boolean hay = invocaESB.hayStockConReserva(map);

		// si hay producto bajo stock critico deshabilitarlo
		if (!hay)
			deshabilitaProductos(map);
	}

	//**************************************************************************************************
	/**
	 * Invocada por ProcesaOrden.procesa
	 * @param map
	 */
	public void deshabilitaProductos(Map<String, Object> map) {
		@SuppressWarnings("unchecked")
		List<Long> productosSinStock = (List<Long>) map.get(PRODUCTOS_SIN_STOCK_KEY);
		Long restaurantID = (Long) map.get("restaurantID");
		Order order = (Order)map.get(ORDER_KEY);
		
		if (productosSinStock != null && productosSinStock.size() > 0) {
			for (Long codigoProducto : productosSinStock) {
				String integrationCode = String.format("%d", codigoProducto);
				try {
					// buscar el producto en la orden para sacar la seccion
					Product producto = findProducto(integrationCode, order.getDetails());
					logger.debug(String.format("deshabilitaProductos: integrationCode (%s) producto %s", 
							integrationCode, producto!=null?String.format("seccion: %s nombre: %s", 
									                       producto.getSection().getIntegrationCode(), producto.getName()):"NULO"));
					if (producto != null) {
						producto.setEnabled(false);
						apiClient.getProductsClient().update(producto, restaurantID);
					}
					
				} catch (Exception e) {
					logger.error(String.format("deshabilitaProductos: integrationCode: %s",integrationCode), e);
				}
			}
		}
	}

	public Product findProducto(String integrationCode, List<Detail> details) {
		for (Detail detail : details) {
			Product product = detail.getProduct();
			logger.debug(String.format("findProducto: integrationCode: %s codigo: %s", integrationCode, detail.getProduct().getIntegrationCode().toString().trim()));
			if (integrationCode.equals(detail.getProduct().getIntegrationCode().toString().trim()))
				return product;
		}
		// NOT FOUND
		return null;
	}

	//**************************************************************************************************
	/**
	 * Invocada por ProcesaOrden.procesa
	 * @param map
	 */
	public void informeRechazo(Map<String, Object> map) {
		invocaESB.informeRechazo(map);
	}

	//**************************************************************************************************
	/**
	 * Invocada por ProcesaOrden.stockMenorCritico
	 * @param map
	 */
	public Integer getStockCritico() {
		return invocaESB.getStockCritico();
	}

	//**************************************************************************************************
	/**
	 * Invocada por ProcesaOrden.procesa
	 * @param map
	 * 
	 * retorn 
	 */
	public Integer invocaMonitorLogistico(Map<String, Object> map, Semaforo semaforo) {
		Order order = (Order) map.get(ORDER_KEY);
		String numeroOrden = String.format("%d", order.getId());
		mapProcesoOrden.put(numeroOrden, semaforo);

		apiSendOrdenMonitor(map); //
//		apiSendOrdenMonitor(order); 
		
		semaforo.espere();
		
		return mapConfirmaOrden.remove(numeroOrden);
	}
	
	/**
	 * debe generar un POST a http://servicio:8181/cxf/ESB/monitorLogistico/procesar con json
	 * La URL la debe sacar de ENDPOINT_MONITOR_LOGISTICO
	 * { "numeroOrden": 12345 }
	 * @param numeroOrden
	 */
	private boolean apiSendOrdenMonitor(Map<String, Object> map) {
		boolean resultado = false;
		MonitorLogisticoRequest request = factory.factoryMonitorLogisticoRequest(map);
		if (request == null)
			return resultado;
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod((String) integracionProps.get(ENDPOINT_MONITOR_LOGISTICO));
		postMethod.setRequestHeader("Content-type", "application/json");
		String jsonInput = request.toString();
		logger.debug(String.format("apiSendOrdenMonitor: json |%s|", jsonInput));
		if (jsonInput.startsWith("No pudo deserializar clase"))
			return resultado;
		InputStream stream = new ByteArrayInputStream(jsonInput.getBytes());
		RequestEntity requestEntity = new InputStreamRequestEntity(stream);
		postMethod.setRequestEntity(requestEntity);
		try {
			logger.debug(String.format("apiSendOrdenMonitor: request.getURI()=%s",
										(postMethod.getURI()!=null?postMethod.getURI().toString(): "NULA")));
			int status = httpClient.executeMethod(postMethod);
			if (status >= 400) {
				logger.error(""+postMethod.getStatusCode()+"\t"+postMethod.getStatusText()+"\t"+postMethod.getStatusLine());

			} else {
			
				resultado = true;
			}
		} catch (Exception e) {
			logger.error("apiSendOrdenMonitor",e);
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				;
			}
			postMethod.releaseConnection();
			
		}
		return resultado;
	}
/*
	private boolean apiSendOrdenMonitor(Order order) {
		// TODO: falta definir lo que se envia al Monitor logistico
		Long numeroOrden = order.getId();
		String urlReceta = null;
		if (order.getAttachments() != null && order.getAttachments().size() > 0) {
			Attachment attachment = order.getAttachments().get(0);
			urlReceta = attachment.getUrl();
		}
		boolean resultado = false;
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod((String) integracionProps.get(ENDPOINT_MONITOR_LOGISTICO));
		postMethod.setRequestHeader("Content-type", "application/json");
		MonitorLogisticoRequest request = new MonitorLogisticoRequest(numeroOrden,
				urlReceta);
		String jsonInput = request.toString();
		logger.debug(String.format("apiSendOrdenMonitor: json |%s|", jsonInput));
		if (jsonInput.startsWith("No pudo deserializar clase"))
			return resultado;
		InputStream stream = new ByteArrayInputStream(jsonInput.getBytes());
		RequestEntity requestEntity = new InputStreamRequestEntity(stream);
		postMethod.setRequestEntity(requestEntity);
		try {
			logger.debug(String.format("apiSendOrdenMonitor: request.getURI()=%s",
										(postMethod.getURI()!=null?postMethod.getURI().toString(): "NULA")));
			int status = httpClient.executeMethod(postMethod);
			if (status >= 400) {
				logger.error(""+postMethod.getStatusCode()+"\t"+postMethod.getStatusText()+"\t"+postMethod.getStatusLine());

			} else {
			
				resultado = true;
			}
		} catch (Exception e) {
			logger.error("apiSendOrdenMonitor",e);
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				;
			}
			postMethod.releaseConnection();
			
		}
		return resultado;
	}

 */
	public static Map<String, Semaforo> mapProcesoOrden = new HashMap<String, Semaforo>();
	public static Map<String, Integer> mapConfirmaOrden = new HashMap<String, Integer>();
	/**
	 * Invocado por ReactiveFullDuplexServer.callbackPeya
	 * @param respuestaMonitor
	 * Coloca la respuesta enviada por el monitor logistico en mapConfirmaOrden
	 * y reactiva al Thread que estaba suspendido.
	 */
	public static void callbackPeya(Properties respuestaMonitor) {
		
		Semaforo sem = mapProcesoOrden.remove(respuestaMonitor.get("numeroOrden"));
		mapConfirmaOrden.put((String) respuestaMonitor.get("numeroOrden"), Integer.valueOf((String) respuestaMonitor.get("confirma")));
		sem.continuar();
	}

	public void undoPedido(Map<String, Object> map) {
		// TODO Auto-generated method stub
		
	}

//******************************************************************************************************
	// getters y setters
	//******************************************************************************************************
	
	public ApiClient getApiClient() {
		return apiClient;
	}

}
