package cl.ahumada.fuse.pedidos.procesor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.pedidos.api.resources.ConsultaPedidoResponse;
import cl.ahumada.fuse.pedidos.api.resources.json.Cliente;
import cl.ahumada.fuse.pedidos.api.resources.json.Pedido;
import cl.ahumada.fuse.pedidos.api.resources.json.Trackings;

public class GeneraRespuestaWs implements Processor {

	Logger logger = Logger.getLogger(getClass());

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		LinkedHashMap<String,Object> map = (LinkedHashMap<String, Object>) exchange.getIn().getBody();
		Map<String,Pedido> pedidos = (Map<String, Pedido>) map.get("pedidos");
		Map<String, List<Trackings>> trackings = (Map<String, List<Trackings>>) map.get("tracking");
		Cliente cliente = (Cliente)map.get("cliente");
		List<Pedido> listaPedidos = new ArrayList<Pedido>();
		
		if (pedidos != null)
			for (String keyPedido : pedidos.keySet()) {
				Pedido pedido = pedidos.get(keyPedido);
				List<Trackings> track= trackings.get(keyPedido);
				if (track != null)
					pedido.setTrackings(track.toArray(new Trackings[0]));
				listaPedidos.add(pedido);
			}
		
		ConsultaPedidoResponse response = new ConsultaPedidoResponse(listaPedidos.toArray(new Pedido[0]), cliente);
		exchange.getIn().setBody(response);
	}

}
