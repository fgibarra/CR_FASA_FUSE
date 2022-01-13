package cl.ahumada.fuse.descuentos.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Coupon implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = -4269721047993285081L;
	@JsonProperty("couponCode")
	public final String couponCode;
	@JsonProperty("couponUsageStatus")
	public final String couponUsageStatus;
	@JsonProperty("couponMessage")
	public final String couponMessage;
	
	@JsonCreator
	public Coupon(@JsonProperty("couponCode")String couponCode, 
			@JsonProperty("couponUsageStatus")String couponUsageStatus, 
			@JsonProperty("couponMessage")String couponMessage) {
		super();
		this.couponCode = couponCode;
		this.couponUsageStatus = couponUsageStatus;
		this.couponMessage = couponMessage;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public String getCouponUsageStatus() {
		return couponUsageStatus;
	}

	public String getCouponMessage() {
		return couponMessage;
	}

}
