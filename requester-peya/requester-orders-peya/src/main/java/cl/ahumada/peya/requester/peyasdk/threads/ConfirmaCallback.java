package cl.ahumada.peya.requester.peyasdk.threads;

import java.util.Map;

public interface ConfirmaCallback {

	public void onConfirmaEnd(Map<String, Object> map, Integer confirma);
	
}
