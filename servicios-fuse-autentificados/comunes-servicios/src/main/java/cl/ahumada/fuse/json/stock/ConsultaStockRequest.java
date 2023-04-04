package cl.ahumada.fuse.json.stock;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.json.utils.JSonUtilities;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsultaStockRequest implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6716912688035549334L;
	@JsonProperty("local")
	public final Local local[];

	@JsonCreator
	public ConsultaStockRequest(@JsonProperty("local") Local local[]) {
		this.local = local;
	}

	@JsonIgnore
	public String toString() {
		try {
			return JSonUtilities.getInstance().java2json(this);
		} catch (Exception e) {
			return e.getMessage();
		}
	}
}
