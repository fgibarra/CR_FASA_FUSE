package cl.ahumada.fuse.stock.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Stock implements Serializable {

	/**
	 *
	 */
	@JsonIgnore
	private static final long serialVersionUID = 1L;

	@JsonProperty("codigo_producto")
	public Long codigoProducto;
	@JsonProperty("cantidad")
	public Long cantidad;
	
	//@JsonProperty("seccion")
	@JsonIgnore
	public Long seccion;
	//@JsonProperty("nombre-producto")
	@JsonIgnore
	public String nombreProducto;
	
	@JsonCreator
	public Stock(@JsonProperty("codigo_producto") Long codigoProducto,
			    @JsonProperty("cantidad") Long cantidad) {
		this.codigoProducto = codigoProducto;
		this.cantidad = cantidad;
	}

	public long getSeccion() {
		return seccion;
	}

	public void setSeccion(long seccion) {
		this.seccion = seccion;
	}

	public String getNombreProducto() {
		return nombreProducto;
	}

	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}

	public long getCodigoProducto() {
		return codigoProducto;
	}

	public long getCantidad() {
		return cantidad;
	}
}
