package cl.ahumada.fuse.promociones.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemResp implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = -4054107616365420165L;

	@JsonProperty("entryID")
	public final Integer entryID;
	
	// para el request
	@JsonProperty("itemCode")
	public final String itemCode;

	@JsonProperty("quantitySold")
	public final Integer quantitySold;
	
	@JsonCreator
	public ItemResp(@JsonProperty("itemCode")String itemCode, 
			@JsonProperty("quantitySold")Integer quantitySold,
			@JsonProperty("entryID")Integer entryID) {
		super();
		this.entryID = entryID;
		this.itemCode = itemCode;
		this.quantitySold = quantitySold;
	}

	public Integer getEntryID() {
		return entryID;
	}

	public String getItemCode() {
		return itemCode;
	}

	public Integer getQuantitySold() {
		return quantitySold;
	}
}
