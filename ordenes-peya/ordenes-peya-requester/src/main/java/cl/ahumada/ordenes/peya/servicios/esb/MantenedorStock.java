package cl.ahumada.ordenes.peya.servicios.esb;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.pedidosya.reception.sdk.ApiClient;

import cl.ahumada.esb.dto.pedidosYa.consultaStock.json.Items;

public class MantenedorStock extends Thread {


	private ApiClient apiClient;
	private Logger logger = Logger.getLogger(getClass());
	private ServiciosdeBus serviciosdeBus;

	int count = 0;

	public MantenedorStock(ApiClient apiClient) throws IOException {
		this.apiClient = apiClient;
		serviciosdeBus = new ServiciosdeBus(apiClient);
	}

	public void actualizaStock() {

		// corre
		final TimerTask task;
		synchronized (this) {
			task = new TimerTask() {
				@Override
				public void run() {
					count++;
					logger.info(String.format("actualizaStock: parte %s count:%d", getHoraPartida(), count));
					try {
						// pide los sku para deshabilitar
						Items[] deshabilitar = serviciosdeBus.getItemsDeshabilitar();
						if (deshabilitar != null && deshabilitar.length > 0)
							for (Items item : deshabilitar) {
								String integrationCode = String.format("%d", item.getSku());
								try {
									logger.debug(String.format(
											"deshabilitaProductos: invoca apiClient.getMenusClient().getProductsClient().disable(%s, %s)",
											integrationCode, item.getLocal()));
									apiClient.getMenusClient().getProductsClient().disable(integrationCode,
											item.getLocal());
								} catch (Exception e) {
									logger.error(
											String.format("deshabilitaProductos: integrationCode: %s local: %s", integrationCode, item.getLocal()),
											e);
									if (esErrorFatal(e.getMessage()))
										break;
								}
							}
						// pide los sku para habilitar
						Items[] habilitar = serviciosdeBus.getItemsHabilitar();
						if (habilitar != null && habilitar.length > 0)
							for (Items item : habilitar) {
								String integrationCode = String.format("%d", item.getSku());
								try {
									logger.debug(String.format(
											"habilitaProductos: invoca apiClient.getMenusClient().getProductsClient().enable(%s, %s)",
											integrationCode, item.getLocal()));
									apiClient.getMenusClient().getProductsClient().enable(integrationCode,
											item.getLocal());
								} catch (Exception e) {
									logger.error(
											String.format("habilitaProductos: integrationCode: %s local: %s", integrationCode, item.getLocal()),
											e);
									if (esErrorFatal(e.getMessage()))
										break;
								}
							}

					} catch (Exception ex) {
						logger.error("Error in actualizaStock", ex);
					}
					logger.info(String.format("actualizaStock: termina %s count: %d", getHoraPartida(), count));
				}

				private boolean esErrorFatal(String message) {
					String [] fatales = {"restaurant.notExist"};
					for (String msg : fatales)
						if (message.contains(msg))
							return true;
					return false;
				}

			};

			logger.info(String.format("actualizaStock: genera timer count: %d", count));
			new Timer().schedule(task, 10000, 3600000);
		}
	}

	public String getHoraPartida() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(new Date());
	}
}
