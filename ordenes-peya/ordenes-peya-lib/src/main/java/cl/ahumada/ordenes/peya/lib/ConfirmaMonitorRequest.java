package cl.ahumada.ordenes.peya.lib;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfirmaMonitorRequest implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 2654993638718954550L;
	@JsonProperty("idOrden")
	Long idOrden;
	@JsonProperty("confirma")
	Integer confirma;

    @JsonCreator
	public ConfirmaMonitorRequest(@JsonProperty("idOrden")Long idOrden, 
			@JsonProperty("confirma")Integer confirma) {
		super();
		this.idOrden = idOrden;
		this.confirma = confirma;
	}

	@Override
	@JsonIgnore
	public String toString() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(Include.NON_NULL);
			return mapper.writeValueAsString(this);
		} catch (Exception e) {
			return String.format("No pudo serializar %s", this.getClass().getSimpleName());
		}
	}

	public Long getIdOrden() {
		return idOrden;
	}

	public void setIdOrden(Long idOrden) {
		this.idOrden = idOrden;
	}

	public Integer getConfirma() {
		return confirma;
	}

	public void setConfirma(Integer confirma) {
		this.confirma = confirma;
	}
}
