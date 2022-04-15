package cl.ahumada.peya.requester.peyasdk;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.pedidosya.reception.sdk.ApiClient;
import com.pedidosya.reception.sdk.clients.EventsClient;
import com.pedidosya.reception.sdk.exceptions.ApiException;
import com.pedidosya.reception.sdk.models.Order;
import com.pedidosya.reception.sdk.queue.OnError;
import com.pedidosya.reception.sdk.queue.OnReceivedOrder;

import cl.ahumada.peya.requester.peyasdk.threads.ConfirmaCallback;
import cl.ahumada.peya.requester.peyasdk.threads.ProcesaOrden;

public class Requester {

	private ApiClient apiClient;
	private Logger logger = Logger.getLogger(getClass());

	public Requester(ApiClient apiClient) throws IOException {

		this.apiClient = apiClient;
	}

	public void getOrders() throws ApiException {

		final EventsClient eventClient = apiClient.getEventClient();

		try {
			logger.info("waiting for new information ...");

			apiClient.getOrdersClient().getAll(new OnReceivedOrder() {

				@Override

				public boolean call(Order order) {

					try {

						logger.info("***********************************************************");
						logger.info("                    Order received");
						logger.info(String.format("ID: %d state: %s local: %s restaurantID=%d",
								order.getId(), order.getState(), order.getRestaurant().getIntegrationCode(),
								order.getRestaurant().getId()));
						logger.info("***********************************************************");

						if (("REJECTED".equals(order.getState()) || ("CONFIRMED".equals(order.getState())))) {
							logger.info("***********************************************************");
							logger.info("                    Order Update");
							logger.info(order.getId() + " - " + order.getState());
							logger.info("***********************************************************");
							logger.info("waiting for new information ...");
						}

						if ("PENDING".equals(order.getState())) {
							eventClient.reception(order);
							logger.info("***********************************************************");
							logger.info(order);
							logger.info("***********************************************************");

							eventClient.acknowledgement(order);
							
							// procesa la orden 
							// OJO!!! podria necesitarse esperar a que se proceso la orden
							final ProcesaOrden procesaOrder = new ProcesaOrden(order);
							procesaOrder.procesa(new ConfirmaCallback() {
								@Override
								public void onConfirmaEnd(Map<String, Object> map, Integer confirma) {
									// Invocado despues que se recibe respuesta desde el monitor logistico
									// Si confirma == 0, ==> stock 0 para todos los productos de la orden
									// confirma == 1, ==> se puedeconfirmar la orden sin modificarla
									// confirma == 2, ==> hay que reconciliar la orden
									if (confirma == 0) {
										procesaOrder.rechazaOrden(map);
									} else if (confirma == 1) {
										procesaOrder.confirmaOrden(map);
									} else if (confirma == 2) {
										procesaOrder.reconciliaOrden(map);
									}
								}
							});
						}
						// action.getReject(order);
					} catch (Exception ex) {
						logger.error("call", ex);
					}
					return true;
				}

			}, new OnError() {
				@Override
				public void call(ApiException ex) {
					logger.error("OnError", ex);
				};
			});
		} catch (Exception ex) {
			logger.error("Error", ex);
		}

	}

}
