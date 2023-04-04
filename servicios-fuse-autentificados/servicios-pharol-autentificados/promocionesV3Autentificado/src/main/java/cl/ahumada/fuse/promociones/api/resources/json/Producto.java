package cl.ahumada.fuse.promociones.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.utils.Constantes;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Producto implements Serializable {

	/**
	 *
	 */
	@JsonIgnore
	private static final long serialVersionUID = -4565074459584449056L;

	@JsonProperty("codigo_producto")
	public Object codigoProducto;
	@JsonProperty("cantidad")
	public final long cantidad;
	@JsonProperty("precio_unitario")
	public final long precioUnitario;
	@JsonProperty("total")
	public final long total;
	@JsonProperty("descuentos")
	public Descuento[] descuentos;

	@JsonCreator
	public Producto(@JsonProperty("codigo_producto")Object codigoProducto,
			@JsonProperty("cantidad")Long cantidad,
			@JsonProperty("precio_unitario")Long precioUnitario,
			@JsonProperty("total")Long total,
			@JsonProperty("descuentos")Descuento[] descuentos) {
		this.codigoProducto = codigoProducto;
		this.cantidad = cantidad;
		this.precioUnitario = precioUnitario;
		this.total = total;
		this.descuentos = descuentos;
	}

	@JsonIgnore
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("codigoProducto=").append(codigoProducto).append('\n');
		sb.append("cantidad=").append(cantidad).append('\n');
		sb.append("precioUnitario=").append(precioUnitario).append('\n');
		sb.append("total=").append(total).append('\n');
		if (descuentos != null && descuentos.length > 0)
			for (Descuento d : descuentos)
				sb.append("descuento\n").append(d.toString()).append('\n');
		return sb.toString();
	}

	@JsonIgnore
	public String toPipechar() {
		StringBuffer sb = new StringBuffer();
		sb.append(Constantes.obj2String(codigoProducto)).append('|');
		sb.append(cantidad).append('|');
		sb.append(precioUnitario).append('|');
		sb.append(total).append('|');
		sb.append(descuentos!=null?descuentos.length:0).append('|');
		if (descuentos!=null && descuentos.length > 0)
			for (Descuento d : descuentos)
				sb.append(d.toPipechar()).append('|');
		return sb.toString();
	}

	public Object getCodigoProducto() {
		return codigoProducto;
	}

	public void setCodigoProducto(Object codigoProducto) {
		this.codigoProducto = codigoProducto;
	}

	public long getCantidad() {
		return cantidad;
	}

	public long getPrecioUnitario() {
		return precioUnitario;
	}

	public long getTotal() {
		return total;
	}

	public Descuento[] getDescuentos() {
		return descuentos;
	}
}
