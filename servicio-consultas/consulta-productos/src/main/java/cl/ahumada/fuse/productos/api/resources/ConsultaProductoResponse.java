package cl.ahumada.fuse.productos.api.resources;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.productos.api.resources.json.Producto;
import cl.ahumada.fuse.utils.json.JSonUtilities;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsultaProductoResponse implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 7610225901687882149L;
	@JsonProperty("Producto")
	public Producto[] producto;

	@JsonCreator
	public ConsultaProductoResponse(@JsonProperty("Producto")Producto[] producto) {
		super();
		this.producto = producto;
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
