package cl.ahumada.fuse.pedidos.api.resources.json;

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
	private static final long serialVersionUID = -7011771061546629152L;
	@JsonProperty("type")
	public final String type;
	@JsonProperty("valor_descuento")
	public long valorDescuento;
	@JsonProperty("codigo_descuento")
	public final Object codigoDescuento;
	@JsonProperty("descripcion_descuento")
	public final String descripcionDescuento;
	@JsonProperty("aplicar")
	public final boolean aplicar;

	public Descuento(@JsonProperty("type")String type,
			@JsonProperty("valor_descuento")long valorDescuento,
			@JsonProperty("codigo_descuento")Object codigoDescuento,
			@JsonProperty("descripcion_descuento")String descripcionDescuento,
			@JsonProperty("aplicar")boolean aplicar) {
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
		if (descripcionDescuento!=null)
			sb.append(descripcionDescuento);
		sb.append('|');
		sb.append(aplicar?"1":"0");
		return sb.toString();
	}
}
