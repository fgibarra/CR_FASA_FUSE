package cl.ahumada.ordenes.peya.module;

import com.pedidosya.reception.sdk.ApiClient;
import com.pedidosya.reception.sdk.clients.OrdersClient;
import com.pedidosya.reception.sdk.exceptions.ApiException;
import com.pedidosya.reception.sdk.models.DeliveryTime;
import com.pedidosya.reception.sdk.models.Order;
import com.pedidosya.reception.sdk.models.RejectMessage;
import java.util.List;

import org.apache.log4j.Logger;

public class Actions {


    private ApiClient apiClient;
	private Logger logger = Logger.getLogger(getClass());

    public Actions(ApiClient apiClient) {
        this.apiClient = apiClient;

    }

    public void getConfirm(Order order) throws ApiException {
        OrdersClient ordersClient = apiClient.getOrdersClient();

        final List<DeliveryTime> deliveryTimes = apiClient.getOrdersClient().getDeliveryTimesClient().getAll();

        if (order.getLogistics() || order.getPickup()) {

            try {
                apiClient.getOrdersClient().confirm(order);
            } catch (ApiException ex) {
                logger.error("Error to cofirm", ex);
            }

        } else {//MKP MarketPlace.
            ordersClient.confirm(order, deliveryTimes.get(0));


        }
    }

    public void getReject(Order order) throws ApiException {
        OrdersClient ordersClient = apiClient.getOrdersClient();

        final List<RejectMessage> rejectMessages = apiClient.getOrdersClient().getRejectMessagesClient().getAll();

        ordersClient.reject(order, rejectMessages.get(2));

    }
}
