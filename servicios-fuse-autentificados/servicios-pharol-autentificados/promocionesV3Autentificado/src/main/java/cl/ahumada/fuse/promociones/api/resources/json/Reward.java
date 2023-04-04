package cl.ahumada.fuse.promociones.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Reward implements Serializable {

	/**
	 *
	 */
	@JsonIgnore
	private static final long serialVersionUID = 1179597745082387139L;

	@JsonProperty("entryID")
	public final Integer entryID;
	@JsonProperty("promotionCode")
	public final Integer promotionCode;
	@JsonProperty("promotionDescription")
	public final String promotionDescription;
	@JsonProperty("rewardType")
	public final String rewardType;
	@JsonProperty("extendedRewardAmount")
	public final Integer extendedRewardAmount;

	@JsonCreator
	public Reward(@JsonProperty("entryID")Integer entryID,
			@JsonProperty("promotionCode")Integer promotionCode,
			@JsonProperty("promotionDescription")String promotionDescription,
			@JsonProperty("rewardType")String rewardType,
			@JsonProperty("extendedRewardAmount")Integer extendedRewardAmount) {
		super();
		this.entryID = entryID;
		this.promotionCode = promotionCode;
		this.promotionDescription = promotionDescription;
		this.rewardType = rewardType;
		this.extendedRewardAmount = extendedRewardAmount;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("entryID=").append(entryID).append('\n');
		if (promotionCode != null)
			sb.append("extendedPrice=").append(promotionCode).append('\n');
		if (promotionDescription != null)
			sb.append("extendedSecondPrice=").append(promotionDescription).append('\n');
		if (extendedRewardAmount != null)
			sb.append("structureCode=").append(extendedRewardAmount).append('\n');

		return sb.toString();
	}

	public Integer getEntryID() {
		return entryID;
	}

	public Integer getPromotionCode() {
		return promotionCode;
	}

	public String getPromotionDescription() {
		return promotionDescription;
	}

	public Integer getExtendedRewardAmount() {
		return extendedRewardAmount;
	}

	public String getRewardType() {
		return rewardType;
	}
}
