package cl.ahumada.fuse.pedidos.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PharolError implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 6498850482736217368L;
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
		StringBuffer sb = new StringBuffer();
		sb.append("code=").append(code).append('\n');
		sb.append("message=").append(message).append('\n');
		sb.append("description=").append(description).append('\n');
		return sb.toString();
	}
}
