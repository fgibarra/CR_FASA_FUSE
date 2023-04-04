package cl.ahumada.fuse.json.stock;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Stock implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2970373853866244904L;
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
