package cl.ahumada.fuse.descuentos.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Discount implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = -1700644655390835006L;
	@JsonProperty("type")
	public final String type;
	@JsonProperty("value")
	public final Float value;
	@JsonProperty("apply")
	public final Boolean apply;
	@JsonProperty("code")
	public final Object code;
	@JsonProperty("description")
	public final String description;
	
	@JsonCreator
	public Discount(@JsonProperty("type")String type, 
			@JsonProperty("value")Float value, 
			@JsonProperty("apply")Boolean apply, 
			@JsonProperty("code")Object code, 
			@JsonProperty("description")String description) {
		super();
		this.type = type;
		this.value = value;
		this.apply = apply;
		this.code = code;
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public Float getValue() {
		return value;
	}

	public Boolean getApply() {
		return apply;
	}

	public Object getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

}
