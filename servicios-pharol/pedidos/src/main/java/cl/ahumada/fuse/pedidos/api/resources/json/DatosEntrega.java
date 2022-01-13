package cl.ahumada.fuse.pedidos.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DatosEntrega implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 4583111867036324606L;
	@JsonProperty("telefono")
	public String telefono;
	@JsonProperty("calle")
	public String calle;
	@JsonProperty("numero")
	public String numero;
	@JsonProperty("dpto")
	public String dpto;
	@JsonProperty("comuna")
	public String comuna;
	@JsonProperty("region")
	public String region;
	@JsonProperty("tipo_entrega")
	public String tipoEntrega;
	@JsonProperty("ruta")
	public String ruta;
	@JsonProperty("fecha_entrega_desde")
	public String fechaEntregaDesde;
	@JsonProperty("fecha_entrega_hasta")
	public String fechaEntregaHasta;

	@JsonCreator
	public DatosEntrega(@JsonProperty("telefono") String telefono,
			@JsonProperty("calle") String calle,
			@JsonProperty("numero") String numero,
			@JsonProperty("dpto") String dpto,
			@JsonProperty("comuna") String comuna,
			@JsonProperty("region") String region,
			@JsonProperty("tipo_entrega") String tipoEntrega,
			@JsonProperty("ruta") String ruta,
			@JsonProperty("fecha_entrega_desde") String fechaEntregaDesde,
			@JsonProperty("fecha_entrega_hasta") String fechaEntregaHasta) {
		super();
		this.telefono = telefono;
		this.calle = calle;
		this.numero = numero;
		this.dpto = dpto;
		this.comuna = comuna;
		this.region = region;
		this.tipoEntrega = tipoEntrega;
		this.ruta = ruta;
		this.fechaEntregaDesde = fechaEntregaDesde;
		this.fechaEntregaHasta = fechaEntregaHasta;
	}

	@JsonIgnore
	public String toPipechar() {
		StringBuffer sb = new StringBuffer();
		sb.append(telefono).append('|');
		sb.append(calle).append('|');
		sb.append(numero).append('|');
		sb.append(dpto).append('|');
		sb.append(comuna).append('|');
		sb.append(region).append('|');
		sb.append(tipoEntrega).append('|');
		sb.append(ruta).append('|');
		sb.append(fechaEntregaDesde).append('|');
		sb.append(fechaEntregaHasta).append('|');
		return sb.toString();
	}
}
