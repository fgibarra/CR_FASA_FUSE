package cl.ahumada.fuse.coberturaPeyaPos.service.json.peya;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Price implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 3150327191164761195L;
	@JsonProperty("currency")
	private final String currency;
	@JsonProperty("distance")
	private final Long distance;
	@JsonProperty("subtotal")
	private final Float subtotal;
	@JsonProperty("taxes")
	private final Float taxes;
	@JsonProperty("total")
	private final Float total;

	@JsonCreator
	public Price(@JsonProperty("currency")String currency, 
			@JsonProperty("distance")Long distance, 
			@JsonProperty("subtotal")Float subtotal, 
			@JsonProperty("taxes")Float taxes, 
			@JsonProperty("total")Float total) {
		super();
		this.currency = currency;
		this.distance = distance;
		this.subtotal = subtotal;
		this.taxes = taxes;
		this.total = total;
	}

	public String getCurrency() {
		return currency;
	}

	public Long getDistance() {
		return distance;
	}

	public Float getSubtotal() {
		return subtotal;
	}

	public Float getTaxes() {
		return taxes;
	}

	public Float getTotal() {
		return total;
	}

}
