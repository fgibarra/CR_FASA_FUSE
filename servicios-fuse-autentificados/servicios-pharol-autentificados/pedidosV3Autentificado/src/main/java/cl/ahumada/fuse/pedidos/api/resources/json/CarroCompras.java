package cl.ahumada.fuse.pedidos.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CarroCompras implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = -7889553092214326667L;
	@JsonProperty("producto")
	public final Producto producto[];
	@JsonProperty("descuento_total")
	public final long descuentoTotal;
	@JsonProperty("neto")
	public final long neto;
	@JsonProperty("iva")
	public final long iva;
	@JsonProperty("total_boleta")
	public final long totalBoleta;

	@JsonCreator
	public CarroCompras(@JsonProperty("producto") Producto[] producto,
			@JsonProperty("descuento_total") Long descuentoTotal,
			@JsonProperty("neto") Long neto,
			@JsonProperty("iva") Long iva,
			@JsonProperty("total_boleta") Long totalBoleta) {
		super();
		this.producto = producto;
		this.descuentoTotal = descuentoTotal;
		this.neto = neto;
		this.iva = iva;
		this.totalBoleta = totalBoleta;
	}

	@JsonIgnore
	public String totalVentatoPipechar() {
		StringBuffer sb = new StringBuffer();
		sb.append(descuentoTotal).append('|');
		sb.append(neto).append('|');
		sb.append(iva).append('|');
		sb.append(totalBoleta).append('|');
		return sb.toString();
	}

	@JsonIgnore
	public String toPipechar() {
		StringBuffer sb = new StringBuffer();
		if (producto!=null && producto.length>0)
			for (Producto pr : producto)
				sb.append(pr.toPipechar());
		return sb.toString();
	}
}
