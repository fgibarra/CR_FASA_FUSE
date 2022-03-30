package cl.ahumada.fuse.requesterPeya.lib.monitorLogistico;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MonitorLogisticoRequest implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 4749509934243302549L;
	@JsonProperty("numeroOrden")
	Long numeroOrden;

	public MonitorLogisticoRequest(@JsonProperty("numeroOrden")Long numeroOrden) {
		super();
		this.numeroOrden = numeroOrden;
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

	public Long getNumeroOrden() {
		return numeroOrden;
	}
	
	
}
