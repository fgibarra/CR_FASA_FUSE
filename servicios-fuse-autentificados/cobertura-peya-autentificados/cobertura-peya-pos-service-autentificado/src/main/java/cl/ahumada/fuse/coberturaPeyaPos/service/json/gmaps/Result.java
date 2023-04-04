package cl.ahumada.fuse.coberturaPeyaPos.service.json.gmaps;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 5903190488325740318L;
	@JsonProperty("formatted_address")
	private String formattedAddress;
	@JsonProperty("geometry")
	private Geometry geometry;
	
	@JsonCreator
	public Result(@JsonProperty("formatted_address")String formattedAddress, 
			@JsonProperty("geometry")Geometry geometry) {
		super();
		this.formattedAddress = formattedAddress;
		this.geometry = geometry;
	}

	public String getFormattedAddress() {
		return formattedAddress;
	}

	public void setFormattedAddress(String formattedAddress) {
		this.formattedAddress = formattedAddress;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
	
}
