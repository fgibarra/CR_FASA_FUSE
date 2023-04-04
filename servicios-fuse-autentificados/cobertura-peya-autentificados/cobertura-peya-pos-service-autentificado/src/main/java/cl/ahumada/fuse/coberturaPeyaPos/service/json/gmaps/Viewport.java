package cl.ahumada.fuse.coberturaPeyaPos.service.json.gmaps;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Viewport implements Serializable {
	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 5323099046018691248L;
	@JsonProperty("northeast")
	private Location northeast;
	@JsonProperty("southwest")
	private Location southwest;
	
	@JsonCreator
	public Viewport(@JsonProperty("northeast")Location northeast, 
			@JsonProperty("southwest")Location southwest) {
		super();
		this.northeast = northeast;
		this.southwest = southwest;
	}

	public Location getNortheast() {
		return northeast;
	}

	public void setNortheast(Location northeast) {
		this.northeast = northeast;
	}

	public Location getSouthwest() {
		return southwest;
	}

	public void setSouthwest(Location southwest) {
		this.southwest = southwest;
	}
	
}
