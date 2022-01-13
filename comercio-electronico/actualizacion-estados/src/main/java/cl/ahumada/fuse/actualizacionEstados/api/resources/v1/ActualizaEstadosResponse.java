package cl.ahumada.fuse.actualizacionEstados.api.resources.v1;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ActualizaEstadosResponse implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 4537443423840539778L;
	@JsonProperty("codigo")
	public final String codigo;

	public ActualizaEstadosResponse(@JsonProperty("codigo")String codigo) {
		super();
		this.codigo = codigo;
	}

}
