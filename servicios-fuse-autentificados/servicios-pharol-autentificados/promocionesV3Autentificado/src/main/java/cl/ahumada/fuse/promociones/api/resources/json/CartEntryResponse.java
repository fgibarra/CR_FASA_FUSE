package cl.ahumada.fuse.promociones.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.utils.json.JSonUtilities;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CartEntryResponse implements Serializable {

	/**
	 *
	 */
	@JsonIgnore
	private static final long serialVersionUID = -7689332097018299117L;

	@JsonProperty("status")
	public final String status;
	@JsonProperty("statusDescription")
	public final String statusDescription;
	@JsonProperty("items")
	public final ItemResp[] items;
	@JsonProperty("rewards")
	public final Reward[] rewards;

	@JsonCreator
	public CartEntryResponse(@JsonProperty("status")String status,
			@JsonProperty("statusDescription")String statusDescription,
			@JsonProperty("items")ItemResp[] items,
			@JsonProperty("rewards")Reward[] rewards) {
		super();
		this.status = status;
		this.statusDescription = statusDescription;
		this.items = items;
		this.rewards = rewards;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("JSON:\n");
		try {
			sb.append(JSonUtilities.getInstance().java2json(this));
		} catch (Exception e) {
			;
		}
		return sb.toString();
	}

	public String getStatus() {
		return status;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public ItemResp[] getItems() {
		return items;
	}

	public Reward[] getRewards() {
		return rewards;
	}
}
