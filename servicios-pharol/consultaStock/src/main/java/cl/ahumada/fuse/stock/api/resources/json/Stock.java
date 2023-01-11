package cl.ahumada.fuse.stock.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Stock implements Serializable {

	/**
	 *
	 */
	@JsonIgnore
	private static final long serialVersionUID = 1L;

	@JsonProperty("codigo_producto")
	public long codigoProducto;
	@JsonProperty("cantidad")
	public long cantidad;

	@JsonCreator
	public Stock(@JsonProperty("codigo_producto") Long codigoProducto,
			    @JsonProperty("cantidad") Long cantidad) {
		this.codigoProducto = codigoProducto;
		this.cantidad = cantidad;
	}
}
