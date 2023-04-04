package cl.ahumada.fuse.descuentos.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConveniosAbf implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = -688519167833705356L;
	@JsonProperty("codigo_cliente")
	public final String codigoCliente;
	@JsonProperty("grupo")
	public final String grupo;
	@JsonProperty("plan")
	public final String plan;

	public ConveniosAbf(@JsonProperty("codigo_cliente")String codigoCliente, 
			@JsonProperty("grupo")String grupo, 
			@JsonProperty("plan")String plan) {
		super();
		this.codigoCliente = codigoCliente;
		this.grupo = grupo;
		this.plan = plan;
	}

	public String getCodigoCliente() {
		return codigoCliente;
	}

	public String getGrupo() {
		return grupo;
	}

	public String getPlan() {
		return plan;
	}

}
