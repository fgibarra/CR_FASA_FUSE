package cl.ahumada.fuse.productos.api.resources;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.utils.json.JSonUtilities;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsultaProductoRequest implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = -4073422648539686985L;
	@JsonProperty("Busqueda")
	public String busqueda;

	@JsonCreator
	public ConsultaProductoRequest(@JsonProperty("Busqueda")String busqueda) {
		super();
		this.busqueda = busqueda;
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
