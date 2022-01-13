package cl.ahumada.fuse.productos.api.resources.json;

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
	private static final long serialVersionUID = -1329915145741123824L;
	@JsonProperty("Codigo")
	public String codigo;
	@JsonProperty("Nombre")
	public String nombre;
	@JsonProperty("Descripcion")
	public String descripcion;
	@JsonProperty("Imagen")
	public String imagen;
	@JsonProperty("Precio")
	public Long precio;
	@JsonProperty("Promocion")
	public Promocion promocion;

	@JsonCreator
	public Producto(@JsonProperty("Codigo")String codigo, 
			@JsonProperty("Nombre")String nombre, 
			@JsonProperty("Descripcion")String descripcion, 
			@JsonProperty("Imagen")String imagen, 
			@JsonProperty("Precio")Long precio, 
			@JsonProperty("Promocion")Promocion promocion) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.imagen = imagen;
		this.precio = precio;
		this.promocion = promocion;
	}

	public void setPromocion(Promocion promocion) {
		this.promocion = promocion;
	}
	
}
