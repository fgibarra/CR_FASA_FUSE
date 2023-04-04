package cl.ahumada.fuse.descuentos.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.utils.JSonUtilities;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CalculateDiscountRequest implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 8523597726888341907L;
	@JsonProperty("commonRequest")
	public final CommonRequest commonRequest;
	@JsonProperty("ABFCode")
	public final String abfCode;
	@JsonProperty("basketItems")
	public final BasketItem[] basketItems;
	@JsonProperty("coupons")
	public final Coupon[] coupons;
	
	@JsonCreator
	public CalculateDiscountRequest(@JsonProperty("commonRequest")CommonRequest commonRequest, 
			@JsonProperty("ABFCode")String abfCode, 
			@JsonProperty("basketItems")BasketItem[] basketItems,
			@JsonProperty("coupons")Coupon[] coupons) {
		super();
		this.commonRequest = commonRequest;
		this.abfCode = abfCode;
		this.basketItems = basketItems;
		this.coupons = coupons;
	}

	@JsonIgnore
	@Override
	public String toString() {
		try {
			return JSonUtilities.getInstance().java2json(this);
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public CommonRequest getCommonRequest() {
		return commonRequest;
	}

	public String getAbfCode() {
		return abfCode;
	}

	public BasketItem[] getBasketItems() {
		return basketItems;
	}

	public Coupon[] getCoupons() {
		return coupons;
	}

}
