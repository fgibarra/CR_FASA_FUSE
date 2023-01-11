package cl.ahumada.fuse.stock.api.resources;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.stock.api.resources.json.Local;
import cl.ahumada.fuse.utils.json.JSonUtilities;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsultaStockResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8043540934505743890L;
	@JsonProperty("local")
	public Local local[];

	@JsonCreator
	public ConsultaStockResponse(@JsonProperty("local")Local local[]) {
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

	public void setLocal(Local valor[]) {
		this.local = valor;
	}
}
