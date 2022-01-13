package cl.ahumada.fuse.pedidos.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Cliente implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = -975371283289228649L;
	@JsonProperty("Rut")
	public String rut;
	@JsonProperty("Mail")
	public String mail;
	@JsonProperty("Celular")
	public String celular;
	@JsonProperty("Nombre")
	public String nombre;
	@JsonProperty("Apellidos")
	public String apellidos;
	
	@JsonCreator
	public Cliente(@JsonProperty("Rut")String rut, 
			@JsonProperty("Mail")String mail, 
			@JsonProperty("Celular")String celular, 
			@JsonProperty("Nombre")String nombre, 
			@JsonProperty("Apellidos")String apellidos) {
		super();
		this.rut = rut;
		this.mail = mail;
		this.celular = celular;
		this.nombre = nombre;
		this.apellidos = apellidos;
	}
	
	
}
