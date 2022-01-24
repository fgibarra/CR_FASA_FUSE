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
public class CoberturaResponse extends Imprimible implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 2286102974257493318L;
	@JsonProperty("deliveryTime")
	private String deliveryTime;
	@JsonProperty("price")
	private Price price;

	@JsonCreator
	public CoberturaResponse(@JsonProperty("deliveryTime")String deliveryTime, 
			@JsonProperty("price")Price price) {
		super();
		this.deliveryTime = deliveryTime;
		this.price = price;
	}

	@JsonIgnore
	public Long getDistancia() {
		if (price != null)
			return price.getDistance();
		return 9000l;
	}
	public String getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

}
