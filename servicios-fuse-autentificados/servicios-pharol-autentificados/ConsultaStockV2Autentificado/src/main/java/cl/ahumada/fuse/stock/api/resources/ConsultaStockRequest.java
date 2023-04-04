package cl.ahumada.fuse.stock.api.resources;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.stock.api.resources.json.Local;
import cl.ahumada.fuse.utils.json.JSonUtilities;

public class ConsultaStockRequest implements Serializable {

	@JsonIgnore
	private static final long serialVersionUID = -1672571109821602672L;

	
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
