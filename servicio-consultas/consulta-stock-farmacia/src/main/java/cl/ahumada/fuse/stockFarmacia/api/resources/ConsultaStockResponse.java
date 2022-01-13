package cl.ahumada.fuse.stockFarmacia.api.resources;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.stockFarmacia.api.resources.json.Farmacias;
import cl.ahumada.fuse.utils.json.JSonUtilities;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsultaStockResponse implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = -3382537096350470213L;
	@JsonProperty("Farmacias")
	public Farmacias[] farmacias;
	
	@JsonCreator
	public ConsultaStockResponse(@JsonProperty("Farmacias")Farmacias[] farmacias) {
		super();
		this.farmacias = farmacias;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append(JSonUtilities.getInstance().java2json(this));
		} catch (Exception e) {
			sb.append("No pudo convertir a json");
		}
		return sb.toString();
	}
}
