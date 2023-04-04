package cl.ahumada.fuse.coberturaPeyaPos.service.json.gmaps;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Location implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 1850354804477894962L;
	@JsonProperty("lat")
	private Float lat;
	@JsonProperty("lng")
	private Float lng;
	
	@JsonCreator
	public Location(@JsonProperty("lat")Float lat, 
			@JsonProperty("lng")Float lng) {
		super();
		this.lat = lat;
		this.lng = lng;
	}

	public Float getLat() {
		return lat;
	}

	public void setLat(Float lat) {
		this.lat = lat;
	}

	public Float getLng() {
		return lng;
	}

	public void setLng(Float lng) {
		this.lng = lng;
	}
	
}
