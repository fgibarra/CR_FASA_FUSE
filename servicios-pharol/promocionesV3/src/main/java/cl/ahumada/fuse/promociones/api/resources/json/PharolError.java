package cl.ahumada.fuse.promociones.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.utils.json.JSonUtilities;

public class PharolError implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 1173652852794158914L;
	@JsonProperty("code")
	public final String code;
	@JsonProperty("message")
	public final String message;
	@JsonProperty("description")
	public String description;

	@JsonCreator
	public PharolError(@JsonProperty("code") String code, @JsonProperty("message") String message) {
		super();
		this.code = code;
		this.message = message;
	}

	@JsonCreator
	public PharolError(@JsonProperty("code") String code,
			@JsonProperty("message") String message,
			@JsonProperty("description") String description) {
		super();
		this.code = code;
		this.message = message;
		this.description = description;
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
