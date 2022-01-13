package cl.ahumada.fuse.pedidos.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Trackings implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 2449385260023499903L;
	@JsonProperty("Id")
	public String id;
	@JsonProperty("Descripcion")
	public String descripcion;
	@JsonProperty("Fecha")
	public String fecha;
	@JsonProperty("Estado")
	public String estado;
	
	@JsonCreator
	public Trackings(@JsonProperty("Id")String id, 
			@JsonProperty("Descripcion")String descripcion, 
			@JsonProperty("Fecha")String fecha,
			@JsonProperty("Estado")String estado) {
		super();
		this.id = id;
		this.descripcion = descripcion;
		this.fecha = fecha;
		this.estado = estado;
	}

}
