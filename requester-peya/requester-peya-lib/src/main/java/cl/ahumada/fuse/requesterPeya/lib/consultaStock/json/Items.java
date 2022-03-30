package cl.ahumada.fuse.requesterPeya.lib.consultaStock.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Items implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 871832141544192728L;
	@JsonProperty("sku")
	private final Long sku;
	@JsonProperty("local")
	private final String local;
	@JsonProperty("seccion")
	private final String seccion;
	@JsonProperty("nombre")
	private final String nombre;

	@JsonCreator
	public Items(
			@JsonProperty("sku")Long sku, 
			@JsonProperty("local")String local, 
			@JsonProperty("seccion")String seccion, 
			@JsonProperty("nombre")String nombre) {
		super();
		this.sku = sku;
		this.local = local;
		this.seccion = seccion;
		this.nombre = nombre;
	}

	public Long getSku() {
		return sku;
	}

	public String getLocal() {
		return local;
	}

	public String getSeccion() {
		return seccion;
	}

	public String getNombre() {
		return nombre;
	}
}
