package cl.ahumada.fuse.pedidos.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PedidosResponse implements Serializable  {

	@JsonIgnore
	private static final long serialVersionUID = 1406410061667465512L;

	@JsonProperty("respuesta")
	public final long respuesta;

	@JsonCreator
	public PedidosResponse(@JsonProperty("respuesta") long respuesta) {
		this.respuesta = respuesta;
	}
	@JsonIgnore
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("respuesta=").append(respuesta).append('\n');
		return sb.toString();
	}
}
