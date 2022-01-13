package cl.ahumada.fuse.promociones.api.resources;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.promociones.api.resources.json.Producto;
import cl.ahumada.fuse.utils.json.JSonUtilities;

public class PromocionesResponse implements Serializable {

	/**
	 *
	 */
	@JsonIgnore
	private static final long serialVersionUID = -2521375735580381747L;

	@JsonProperty("producto")
	public Producto[] producto;

	@JsonCreator
	public PromocionesResponse(@JsonProperty("producto") Producto[] producto) {
		this.producto = producto;
	}

	@JsonIgnore
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("producto").append('\n').append(producto.toString());

		try {
			sb.append("JSON:\n").append(JSonUtilities.getInstance().java2json(this));
		} catch (Exception e) {
			;
		}
		return sb.toString();
	}
}
