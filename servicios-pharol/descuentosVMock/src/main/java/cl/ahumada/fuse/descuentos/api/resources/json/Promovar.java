package cl.ahumada.fuse.descuentos.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Promovar implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 554149196258069104L;
	@JsonProperty("promovar")
	public final Long promovar;

	@JsonCreator
	public Promovar(@JsonProperty("promovar")Long promovar) {
		super();
		this.promovar = promovar;
	}

	public Long getPromovar() {
		return promovar;
	}
	
}
