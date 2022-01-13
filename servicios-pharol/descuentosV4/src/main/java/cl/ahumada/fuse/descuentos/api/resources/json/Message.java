package cl.ahumada.fuse.descuentos.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Message implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 3915473537246553308L;
	@JsonProperty("type")
	public final String type;
	@JsonProperty("channel")
	public final String channel;
	@JsonProperty("content")
	public final String content;
	
	public Message(@JsonProperty("type")String type, 
			@JsonProperty("channel")String channel, 
			@JsonProperty("content")String content) {
		super();
		this.type = type;
		this.channel = channel;
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public String getChannel() {
		return channel;
	}

	public String getContent() {
		return content;
	}

}
