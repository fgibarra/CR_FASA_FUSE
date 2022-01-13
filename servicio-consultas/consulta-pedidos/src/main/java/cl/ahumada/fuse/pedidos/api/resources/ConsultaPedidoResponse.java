package cl.ahumada.fuse.pedidos.api.resources;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.pedidos.api.resources.json.Cliente;
import cl.ahumada.fuse.pedidos.api.resources.json.Pedido;
import cl.ahumada.fuse.utils.json.JSonUtilities;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsultaPedidoResponse implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 4125922770963086539L;
	@JsonProperty("Pedido")
	public Pedido[] pedido;
	@JsonProperty("Cliente")
	public Cliente cliente;

	public ConsultaPedidoResponse(@JsonProperty("Pedido")Pedido[] pedido,
			@JsonProperty("Cliente")Cliente cliente) {
		super();
		this.pedido = pedido;
		this.cliente = cliente;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append(JSonUtilities.getInstance().java2json(this));
		} catch (Exception e) {
			sb.append("No pudo convertir a json");
		}
		return sb.toString();
	}
}
