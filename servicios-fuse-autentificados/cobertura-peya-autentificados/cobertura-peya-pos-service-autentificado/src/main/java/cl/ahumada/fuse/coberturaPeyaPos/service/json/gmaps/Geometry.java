package cl.ahumada.fuse.coberturaPeyaPos.service.json.gmaps;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Geometry implements Serializable {
	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 7567498540985808713L;
	@JsonProperty("location")
	private Location location;
	@JsonProperty("location_type")
	private String locationType;
	@JsonProperty("viewport")
	private Viewport viewport;
	
	@JsonCreator
	public Geometry(@JsonProperty("location")Location location, 
			@JsonProperty("location_type")String locationType, 
			@JsonProperty("viewport")Viewport viewport) {
		super();
		this.location = location;
		this.locationType = locationType;
		this.viewport = viewport;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public Viewport getViewport() {
		return viewport;
	}

	public void setViewport(Viewport viewport) {
		this.viewport = viewport;
	}
	
}
