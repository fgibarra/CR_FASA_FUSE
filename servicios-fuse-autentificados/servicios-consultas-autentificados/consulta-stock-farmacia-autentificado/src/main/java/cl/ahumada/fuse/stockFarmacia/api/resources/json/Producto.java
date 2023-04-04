package cl.ahumada.fuse.stockFarmacia.api.resources.json;

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
	private static final long serialVersionUID = -4406048026306807334L;
	@JsonProperty("Codigo")
	public Long codigo;

	@JsonCreator
	public Producto(@JsonProperty("Codigo")Long codigo) {
		super();
		this.codigo = codigo;
	}
	
}
