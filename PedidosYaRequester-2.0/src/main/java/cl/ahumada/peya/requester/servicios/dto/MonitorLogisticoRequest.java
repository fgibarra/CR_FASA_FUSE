package cl.ahumada.peya.requester.servicios.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MonitorLogisticoRequest implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 3128666636718738571L;
	@JsonProperty("numero-orden")
	public String numeroOrden;
	@JsonProperty("receta")
	public String receta;

	public MonitorLogisticoRequest(@JsonProperty("numero_orden")String numeroOrden, 
			@JsonProperty("receta")String receta) {
		super();
		this.numeroOrden = numeroOrden;
		this.receta = receta;
	}

	@JsonIgnore
	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return String.format("No pudo deserializar clase %s", getClass().getSimpleName());
		}
	}
	public String getNumeroOrden() {
		return numeroOrden;
	}

	public void setNumeroOrden(String numeroOrden) {
		this.numeroOrden = numeroOrden;
	}

	public String getReceta() {
		return receta;
	}

	public void setReceta(String receta) {
		this.receta = receta;
	}

}
