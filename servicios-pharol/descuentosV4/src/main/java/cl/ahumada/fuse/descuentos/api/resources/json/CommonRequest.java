package cl.ahumada.fuse.descuentos.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.utils.JSonUtilities;

/**
 * @author fernando
 *
 * Utilizado para BalanceInquire y CalculateDiscount
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonRequest implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 2888605191074408035L;
	@JsonProperty("storeID")
	public final String storeID;
	@JsonProperty("transactionCode")
	public final Long transactionCode;
	@JsonProperty("loyaltyIdentifierNo")
	public final String loyaltyIdentifierNo;
	@JsonProperty("cashierId")
	public final Long cashierId;
	@JsonProperty("transactionDate")
	public final String transactionDate;
	@JsonProperty("terminalID")
	public final Long terminalId;
	@JsonProperty("posType")
	public final String posType;
	
	public CommonRequest(@JsonProperty("storeID")String storeID, 
			@JsonProperty("transactionCode")Long transactionCode, 
			@JsonProperty("loyaltyIdentifierNo")String loyaltyIdentifierNo, 
			@JsonProperty("cashierId")Long cashierId,
			@JsonProperty("transactionDate")String transactionDate, 
			@JsonProperty("terminalID")Long terminalId, 
			@JsonProperty("posType")String posType) {
		super();
		this.storeID = storeID;
		this.transactionCode = transactionCode;
		this.loyaltyIdentifierNo = loyaltyIdentifierNo;
		this.cashierId = cashierId;
		this.transactionDate = transactionDate;
		this.terminalId = terminalId;
		this.posType = posType;
	}

	@Override
	public String toString() {
		try {
			return JSonUtilities.getInstance().java2json(this);
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public String getStoreID() {
		return storeID;
	}

	public Long getTransactionCode() {
		return transactionCode;
	}

	public String getLoyaltyIdentifierNo() {
		return loyaltyIdentifierNo;
	}

	public Long getCashierId() {
		return cashierId;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public Long getTerminalId() {
		return terminalId;
	}

	public String getPosType() {
		return posType;
	}

}
