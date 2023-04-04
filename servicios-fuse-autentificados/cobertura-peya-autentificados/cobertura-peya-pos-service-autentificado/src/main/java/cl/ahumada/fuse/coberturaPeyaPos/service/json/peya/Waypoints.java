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
	@JsonProperty("latitude")
	private Float latitud;
	@JsonProperty("longitude")
	private Float longitude;
	@JsonProperty("addressStreet")
	private String addressStreet;
	@JsonProperty("addressAdditional")
	private String addressAdditional;
	@JsonProperty("city")
	private String city;
	@JsonProperty("phone")
	private final String phone = "+56912345678";
	@JsonProperty("name")
	private String name;
	@JsonProperty("order")
	private Integer order;
	
	@JsonCreator
	public Waypoints(@JsonProperty("type")String type, 
			@JsonProperty("latitude")Float latitud, 
			@JsonProperty("longitude")Float longitude, 
			@JsonProperty("addressStreet")String addressStreet, 
			@JsonProperty("addressAdditional")String addressAdditional,
			@JsonProperty("city")String city,
			@JsonProperty("name")String name,
			@JsonProperty("order")Integer order
			) {
		super();
		this.type = type;
		this.latitud = latitud;
		this.longitude = longitude;
		this.addressStreet = addressStreet;
		this.addressAdditional = addressAdditional;
		this.city = city;
		this.name = name;
		this.order = order;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}
	
}
