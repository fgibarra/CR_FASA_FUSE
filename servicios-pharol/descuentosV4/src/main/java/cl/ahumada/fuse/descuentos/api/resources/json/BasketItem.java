package cl.ahumada.fuse.descuentos.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BasketItem implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = -6749070582789550718L;
	@JsonProperty("code")
	public final String code;
	@JsonProperty("lineItemId")
	public final Integer lineItemId;
	@JsonProperty("quantity")
	public final Integer quantity;
	@JsonProperty("productValue")
	public final Float productValue;
	@JsonProperty("totalValue")
	public final Float totalValue;
	@JsonProperty("discountsRestricted")
	public final Boolean discountsRestricted;
	@JsonProperty("restricted")
	public final Boolean restricted;
	@JsonProperty("redeemed")
	public final Boolean redeemed;
	@JsonProperty("valuePaidWithPoints")
	public final Float valuePaidWithPoints;
	@JsonProperty("pointsValue")
	public final Long pointsValue;
	@JsonProperty("discounts")
	public final Discount[] discounts;
	
	@JsonCreator
	public BasketItem(@JsonProperty("code")String code, 
			@JsonProperty("lineItemId")Integer lineItemId, 
			@JsonProperty("quantity")Integer quantity, 
			@JsonProperty("productValue")Float productValue, 
			@JsonProperty("totalValue")Float totalValue,
			@JsonProperty("discountsRestricted")Boolean discountsRestricted, 
			@JsonProperty("restricted")Boolean restricted, 
			@JsonProperty("redeemed")Boolean redeemed, 
			@JsonProperty("valuePaidWithPoints")Float valuePaidWithPoints,
			@JsonProperty("pointsValue")Long pointsValue, 
			@JsonProperty("discounts")Discount[] discounts) {
		super();
		this.code = code;
		this.lineItemId = lineItemId;
		this.quantity = quantity;
		this.productValue = productValue;
		this.totalValue = totalValue;
		this.discountsRestricted = discountsRestricted;
		this.restricted = restricted;
		this.redeemed = redeemed;
		this.valuePaidWithPoints = valuePaidWithPoints;
		this.pointsValue = pointsValue;
		this.discounts = discounts;
	}

	public String getCode() {
		return code;
	}

	public Integer getLineItemId() {
		return lineItemId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public Float getProductValue() {
		return productValue;
	}

	public Float getTotalValue() {
		return totalValue;
	}

	public Boolean getDiscountsRestricted() {
		return discountsRestricted;
	}

	public Boolean getRestricted() {
		return restricted;
	}

	public Boolean getRedeemed() {
		return redeemed;
	}

	public Float getValuePaidWithPoints() {
		return valuePaidWithPoints;
	}

	public Long getPointsValue() {
		return pointsValue;
	}

	public Discount[] getDiscounts() {
		return discounts;
	}

}
