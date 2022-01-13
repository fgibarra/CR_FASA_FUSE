package cl.ahumada.fuse.descuentos.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.utils.JSonUtilities;


@JsonIgnoreProperties(ignoreUnknown = true)
public class CartEntryRequest implements Serializable {

	/**
	 *
	 */
	@JsonIgnore
	private static final long serialVersionUID = -5229063717165403019L;

	@JsonProperty("store")
	public final Integer store;
	@JsonProperty("trxStartTime")
	public final String trxStartTime;
	@JsonProperty("trxNumber")
	public final String trxNumber;
	public final Boolean endTransaction;
	@JsonProperty("promovars")
	public final PromovarCE[] promovars;
	@JsonProperty("items")
	public final ItemReq[] items;

	public CartEntryRequest(@JsonProperty("store")Integer store,
			@JsonProperty("trxStartTime")String trxStartTime,
			@JsonProperty("trxNumber")String trxNumber,
			@JsonProperty("promovars")PromovarCE[] promovars,
			@JsonProperty("items")ItemReq[] items) {
		super();
		this.store = store;
		this.trxStartTime = trxStartTime;
		this.trxNumber = trxNumber;
		this.items = items;
		this.promovars = promovars;
		this.endTransaction = Boolean.FALSE;
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

	public Integer getStore() {
		return store;
	}

	public String getTrxStartTime() {
		return trxStartTime;
	}

	public String getTrxNumber() {
		return trxNumber;
	}

	public Boolean getEndTransaction() {
		return endTransaction;
	}

	public ItemReq[] getItems() {
		return items;
	}
	
}
