package cl.ahumada.fuse.descuentos.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PromovarCE implements Serializable {
	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 6378284008283581541L;
	@JsonProperty("code")
	public final String code;
	@JsonProperty("value")
	public final Integer value;
	@JsonProperty("costCenter")
	public final Integer costCenter;

	public PromovarCE(
			@JsonProperty("code")String code, 
			@JsonProperty("value")Integer value, 
			@JsonProperty("costCenter")Integer costCenter) {
		super();
		this.code = code;
		this.value = value;
		this.costCenter = costCenter;
	}

	public String getCode() {
		return code;
	}

	public Integer getValue() {
		return value;
	}

	public Integer getCostCenter() {
		return costCenter;
	}

}
