package cl.ahumada.fuse.stockFarmacia.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ubicacion implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 1162356356438645890L;
	@JsonProperty("Region")
	public Integer region;
	@JsonProperty("Comuna")
	public Integer comuna;
	@JsonProperty("Direccion")
	public String direccion;
	@JsonProperty("Latitud")
	public String latitud;
	@JsonProperty("Longitud")
	public String longitud;
	
	@JsonCreator
	public Ubicacion(@JsonProperty("Region")Integer region, 
			@JsonProperty("Comuna")Integer comuna, 
			@JsonProperty("Direccion")String direccion, 
			@JsonProperty("Latitud")String latitud, 
			@JsonProperty("Longitud")String longitud) {
		super();
		this.region = region;
		this.comuna = comuna;
		this.direccion = direccion;
		this.latitud = latitud;
		this.longitud = longitud;
	}
	
}
