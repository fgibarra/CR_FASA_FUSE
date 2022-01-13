package cl.ahumada.fuse.productos.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Promocion implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = -488457974669945609L;
	@JsonProperty("Precio_oferta")
	public Long precioOferta;
	@JsonProperty("Mecanica_descripcion")
	public String mecanicaDescripcion;
	@JsonProperty("Mecanica_vigencia")
	public String mecanicaVigencia;

	@JsonCreator
	public Promocion(@JsonProperty("Precio_oferta")Long precioOferta, 
			@JsonProperty("Mecanica_descripcion")String mecanicaDescripcion, 
			@JsonProperty("Mecanica_vigencia")String mecanicaVigencia) {
		super();
		this.precioOferta = precioOferta;
		this.mecanicaDescripcion = mecanicaDescripcion;
		this.mecanicaVigencia = mecanicaVigencia;
	}
	
	
}
