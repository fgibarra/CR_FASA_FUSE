package cl.ahumada.fuse.pedidos.api.resources.json;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Message implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 6862509334312077600L;
	public final String type;
	public final String channel;
	public final String content;

	@JsonCreator
	public Message(@JsonProperty("type")String type,
			@JsonProperty("channel")String channel,
			@JsonProperty("content")String content) {
		super();
		this.type = type;
		this.channel = channel;
		this.content = content;
	}

	@JsonIgnore
	public String toPipechar() {
		StringBuffer sb = new StringBuffer();
		sb.append(type).append('|');
		sb.append(channel).append('|');
		sb.append(content).append('|');
		return sb.toString();
	}
}
