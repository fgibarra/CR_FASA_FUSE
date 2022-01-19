package cl.ahumada.fuse.coberturaPeyaPos.service.json.peya;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.coberturaPeyaPos.service.json.Imprimible;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Waypoints extends Imprimible implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = -434746972091840412L;
	@JsonProperty("type")
	private String type;
	@JsonProperty("latitud")
	private Float latitud;
	@JsonProperty("longitude")
	private Float longitude;
	@JsonProperty("addressStreet")
	private String addressStreet;
	@JsonProperty("addressAdditional")
	private String addressAdditional;
	
	@JsonCreator
	public Waypoints(@JsonProperty("type")String type, 
			@JsonProperty("latitud")Float latitud, 
			@JsonProperty("longitude")Float longitude, 
			@JsonProperty("addressStreet")String addressStreet, 
			@JsonProperty("addressAdditional")String addressAdditional) {
		super();
		this.type = type;
		this.latitud = latitud;
		this.longitude = longitude;
		this.addressStreet = addressStreet;
		this.addressAdditional = addressAdditional;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Float getLatitud() {
		return latitud;
	}

	public void setLatitud(Float latitud) {
		this.latitud = latitud;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public String getAddressStreet() {
		return addressStreet;
	}

	public void setAddressStreet(String addressStreet) {
		this.addressStreet = addressStreet;
	}

	public String getAddressAdditional() {
		return addressAdditional;
	}

	public void setAddressAdditional(String addressAdditional) {
		this.addressAdditional = addressAdditional;
	}
	
}
