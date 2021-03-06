package cl.ahumada.fuse.promociones.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
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
	private static final long serialVersionUID = -3798530369086489953L;
	@JsonProperty("type")
	public String type;
	@JsonProperty("valor_descuento")
	public long valorDescuento;
	@JsonProperty("codigo_descuento")
	public final Object codigoDescuento;
	@JsonProperty("descripcion_descuento")
	public final String descripcionDescuento;
	@JsonProperty("aplicar")
	public final boolean aplicar;

	@JsonCreator
	public Descuento(@JsonProperty("type")String type,
			@JsonProperty("valor_descuento")long valorDescuento,
			@JsonProperty("codigo_descuento")Object codigoDescuento,
			@JsonProperty("descripcion_descuento")String descripcionDescuento,
			@JsonProperty("aplicar")boolean aplicar) {
		super();
		this.type = type;
		this.valorDescuento = valorDescuento;
		this.codigoDescuento = Constantes.obj2String(codigoDescuento);
		this.descripcionDescuento = descripcionDescuento;
		this.aplicar = aplicar;
	}

	@JsonIgnore
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("type=").append(type).append('\n');
		sb.append("valor_descuento=").append(valorDescuento).append('\n');
		sb.append("codigo_descuento=").append(codigoDescuento).append('\n');
		sb.append("descripcion_descuento=").append(descripcionDescuento).append('\n');
		sb.append("aplicar=").append(aplicar).append('\n');
		return sb.toString();
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
}
