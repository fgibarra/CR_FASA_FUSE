package cl.ahumada.peya.requester.peyasdk.threads;

import com.pedidosya.reception.sdk.models.Order;

public interface ConfirmaCallback {

	public void onConfirmaEnd(Order order, Integer confirma);
	
}
