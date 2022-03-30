package cl.ahumada.fuse.requesterPeya.lib.consultaStock;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import cl.ahumada.fuse.requesterPeya.lib.consultaStock.json.Items;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MantencionStockResponse implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 9101298990847351544L;
	@JsonProperty("items")
	private final Items[] items;

	@JsonCreator
	public MantencionStockResponse(@JsonProperty("items")Items[] items) {
		super();
		this.items = items;
	}

	@Override
	@JsonIgnore
	public String toString() {
		try {
			ObjectMapper mapper = new ObjectMapper();
		    mapper.setSerializationInclusion(Include.NON_NULL);
		    return mapper.writeValueAsString(this);
		} catch (Exception e) {
			return String.format("No pudo serializar %s", this.getClass().getSimpleName());
		}
	}

	public Items[] getItems() {
		return items;
	}
}
