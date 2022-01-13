package cl.ahumada.fuse.descuentos.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.utils.Constantes;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Descuento implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 6783138129941374046L;
	@JsonProperty("type")
	public final String type;
	@JsonProperty("valor_descuento")
	public Long valorDescuento;
	@JsonProperty("codigo_descuento")
	public final Object codigoDescuento;
	@JsonProperty("descripcion_descuento")
	public final String descripcionDescuento;
	@JsonProperty("aplicar")
	public final Boolean aplicar;
	
	public Descuento(@JsonProperty("type")String type, 
			@JsonProperty("valor_descuento")Long valorDescuento, 
			@JsonProperty("codigo_descuento")Object codigoDescuento, 
			@JsonProperty("descripcion_descuento")String descripcionDescuento,
			@JsonProperty("aplicar")Boolean aplicar) {
		super();
		this.type = type;
		this.valorDescuento = valorDescuento;
		this.codigoDescuento = codigoDescuento;
		this.descripcionDescuento = descripcionDescuento;
		this.aplicar = aplicar;
	}

	@JsonIgnore
	public String toPipechar() {
		StringBuffer sb = new StringBuffer();
		sb.append(type).append('|');
		sb.append(valorDescuento).append('|');
		sb.append(Constantes.obj2String(codigoDescuento)).append('|');
		sb.append(descripcionDescuento).append('|');
		sb.append(aplicar?"1":"0");
		return sb.toString();
	}
	public String getType() {
		return type;
	}

	public Long getValorDescuento() {
		return valorDescuento;
	}

	public Object getCodigoDescuento() {
		return codigoDescuento;
	}

	public String getDescripcionDescuento() {
		return descripcionDescuento;
	}

	public Boolean getAplicar() {
		return aplicar;
	}

}
