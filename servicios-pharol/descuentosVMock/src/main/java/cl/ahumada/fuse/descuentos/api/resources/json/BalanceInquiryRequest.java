package cl.ahumada.fuse.descuentos.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.utils.JSonUtilities;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BalanceInquiryRequest implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = -6269815449903415321L;
	@JsonProperty("commonRequest")
	public final CommonRequest commonRequest;

	public BalanceInquiryRequest(@JsonProperty("commonRequest")CommonRequest commonRequest) {
		super();
		this.commonRequest = commonRequest;
	}

	@JsonIgnore
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

}
