package cl.ahumada.fuse.pedidos.api.resources;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.pedidos.api.resources.json.Cliente;
import cl.ahumada.fuse.pedidos.api.resources.json.Pedido;
import cl.ahumada.fuse.utils.json.JSonUtilities;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsultaPedidoRequest implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 3360349782438407934L;
	@JsonProperty("Cliente")
	public Cliente cliente;
	@JsonProperty("Pedido")
	public Pedido pedido;
	
	@JsonCreator
	public ConsultaPedidoRequest(@JsonProperty("Cliente")Cliente cliente, 
			@JsonProperty("Pedido")Pedido pedido) {
		super();
		this.cliente = cliente;
		this.pedido = pedido;
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
