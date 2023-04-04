package cl.ahumada.fuse.descuentos.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.utils.JSonUtilities;

public class BalanceInquiryResponse implements Serializable {

	@JsonIgnore
	private static final long serialVersionUID = -1817523718518455911L;
	@JsonProperty("cardAllowsRedemption")
	public final Boolean cardAllowsRedemption;
	@JsonProperty("redemptionBlocked")
	public final Boolean redemptionBlocked;
	@JsonProperty("pointsBalance")
	public final Float pointsBalance;
	@JsonProperty("firstName")
	public final String firstName;
	@JsonProperty("minPWPBalance")
	public final Long minPWPBalance;
	@JsonProperty("POSMessages")
	public final POSMessages[] posMessages;
	
	@JsonCreator
	public BalanceInquiryResponse(@JsonProperty("cardAllowsRedemption")Boolean cardAllowsRedemption, 
			@JsonProperty("redemptionBlocked")Boolean redemptionBlocked, 
			@JsonProperty("pointsBalance")Float pointsBalance,
			@JsonProperty("firstName")String firstName, 
			@JsonProperty("minPWPBalance")Long minPWPBalance, 
			@JsonProperty("POSMessages")POSMessages[] posMessages) {
		super();
		this.cardAllowsRedemption = cardAllowsRedemption;
		this.redemptionBlocked = redemptionBlocked;
		this.pointsBalance = pointsBalance;
		this.firstName = firstName;
		this.minPWPBalance = minPWPBalance;
		this.posMessages = posMessages;
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

	public Boolean getCardAllowsRedemption() {
		return cardAllowsRedemption;
	}

	public Boolean getRedemptionBlocked() {
		return redemptionBlocked;
	}

	public Float getPointsBalance() {
		return pointsBalance;
	}

	public String getFirstName() {
		return firstName;
	}

	public Long getMinPWPBalance() {
		return minPWPBalance;
	}

	public POSMessages[] getPosMessages() {
		return posMessages;
	}


}
