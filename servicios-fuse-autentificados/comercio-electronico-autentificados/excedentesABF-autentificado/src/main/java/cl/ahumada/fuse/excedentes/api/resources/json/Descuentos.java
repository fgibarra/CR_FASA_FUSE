package cl.ahumada.fuse.excedentes.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Descuentos implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = -7952663929369069590L;
	@JsonProperty("type")
	public String type;
	@JsonProperty("descuento")
	public Long descuento;
	@JsonProperty("codigo_descuento")
	public Long codigoDescuento;
	@JsonProperty("descripcion_descuento")
	public String descripcionDescuento;
	@JsonProperty("aplicar")
	public Boolean aplicar;
	
	@JsonCreator
	public Descuentos(@JsonProperty("type")String type, 
			@JsonProperty("descuento")Long descuento, 
			@JsonProperty("codigo_descuento")Long codigoDescuento, 
			@JsonProperty("descripcion_descuento")String descripcionDescuento,
			@JsonProperty("aplicar")Boolean aplicar) {
		super();
		this.type = type;
		this.descuento = descuento;
		this.codigoDescuento = codigoDescuento;
		this.descripcionDescuento = descripcionDescuento;
		this.aplicar = aplicar;
	}

	public String getType() {
		return type;
	}

	public Long getDescuento() {
		return descuento;
	}

	public Long getCodigoDescuento() {
		return codigoDescuento;
	}

	public String getDescripcionDescuento() {
		return descripcionDescuento;
	}

	public Boolean getAplicar() {
		return aplicar;
	}
	
}
