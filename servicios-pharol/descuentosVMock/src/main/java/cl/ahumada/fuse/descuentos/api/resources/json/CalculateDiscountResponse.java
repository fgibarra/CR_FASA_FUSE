package cl.ahumada.fuse.descuentos.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.utils.JSonUtilities;

public class CalculateDiscountResponse implements Serializable {

	/**
	 *
	 */
	@JsonIgnore
	private static final long serialVersionUID = 3622092841471387382L;
	/**
	 *
	 */
	@JsonProperty("loyaltyTransactionId")
	public final long loyaltyTransactionId;
	@JsonProperty("responseBasketItems")
	public final BasketItem[] responseBasketItems;
	@JsonProperty("couponsUsage")
	public final Coupon[] couponsUsage;
	@JsonProperty("POSMessages")
	public final Message[] posMessages;

	@JsonCreator
	public CalculateDiscountResponse(@JsonProperty("loyaltyTransactionId")long loyaltyTransactionId,
			@JsonProperty("responseBasketItems")BasketItem[] responseBasketItems,
			@JsonProperty("couponsUsage")Coupon[] couponsUsage,
			@JsonProperty("POSMessages")Message[] posMessages) {
		super();
		this.loyaltyTransactionId = loyaltyTransactionId;
		this.responseBasketItems = responseBasketItems;
		this.couponsUsage = couponsUsage;
		this.posMessages = posMessages;
	}

	@JsonIgnore
	public String toString() {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append(JSonUtilities.getInstance().java2json(this));
		} catch (Exception e) {
			sb.append("No pudo convertir a JSON");
		}
		return sb.toString();
	}
}
