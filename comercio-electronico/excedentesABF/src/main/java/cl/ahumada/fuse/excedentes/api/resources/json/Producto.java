package cl.ahumada.fuse.excedentes.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Producto implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 8151348591415843345L;
	@JsonProperty("codigo_producto")
	public String codigoProducto;
	@JsonProperty("cantidad")
	public Long cantidad;
	@JsonProperty("precio_unitario")
	public Long precioUnitario;
	@JsonProperty("total")
	public Long total;
	@JsonProperty("descuentos")
	public Descuentos [] descuentos;
	
	@JsonCreator
	public Producto(@JsonProperty("codigo_producto")String codigoProducto, 
			@JsonProperty("cantidad")Long cantidad, 
			@JsonProperty("precio_unitario")Long precioUnitario, 
			@JsonProperty("total")Long total, 
			@JsonProperty("descuentos")Descuentos[] descuentos) {
		super();
		this.codigoProducto = codigoProducto;
		this.cantidad = cantidad;
		this.precioUnitario = precioUnitario;
		this.total = total;
		this.descuentos = descuentos;
	}

	public String getCodigoProducto() {
		return codigoProducto;
	}

	public Long getCantidad() {
		return cantidad;
	}

	public Long getPrecioUnitario() {
		return precioUnitario;
	}

	public Long getTotal() {
		return total;
	}

	public Descuentos[] getDescuentos() {
		return descuentos;
	}
	
}
