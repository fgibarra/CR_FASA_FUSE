package cl.ahumada.fuse.coberturaPeyaPos.service.json.peya;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Item implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 8886547528706137300L;
	@JsonProperty("categoryId")
	private final Integer categoryId = Integer.valueOf(36083354);
	@JsonProperty("value")
	private final Float value = 1250.6f;
	@JsonProperty("description")
	private final String description = "No importa";
	@JsonProperty("quantity")
	private final Integer quantity = 1;
	@JsonProperty("volume")
	private final Float volume = 20.02f;
	@JsonProperty("weight")
	private final Float weight = 0.8f;

	@JsonCreator
	public Item() {
		super();
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public Float getValue() {
		return value;
	}

	public String getDescription() {
		return description;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public Float getVolume() {
		return volume;
	}

	public Float getWeight() {
		return weight;
	}

}
