package cl.ahumada.fuse.pedidos.api.resources.json;

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
	private static final long serialVersionUID = -5384812656538350098L;
	@JsonProperty("Codigo")
	public String codigo;
	@JsonProperty("Nombre")
	public String nombre;
	@JsonProperty("Imagen")
	public String imagen;
	@JsonProperty("Cantidad")
	public Long cantidad;
	@JsonProperty("Precio")
	public Long precio;
	@JsonProperty("Descuento")
	public Long descuento;
	@JsonProperty("Total")
	public Long total;
	
	@JsonCreator
	public Producto(@JsonProperty("Codigo")String codigo, 
			@JsonProperty("Nombre")String nombre, 
			@JsonProperty("Imagen")String imagen, 
			@JsonProperty("Cantidad")Long cantidad, 
			@JsonProperty("Precio")Long precio, 
			@JsonProperty("Descuento")Long descuento,
			@JsonProperty("Total")Long total) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.imagen = imagen;
		this.cantidad = cantidad;
		this.precio = precio;
		this.descuento = descuento;
		this.total = total;
	}
	
}
