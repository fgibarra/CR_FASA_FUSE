package cl.ahumada.fuse.descuentos.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class POSMessages implements Serializable {

	@JsonIgnore
	private static final long serialVersionUID = -6903104959100127536L;
	@JsonProperty("POSMessages")
	public final Message[] posMessages;

	@JsonCreator
	public POSMessages(@JsonProperty("POSMessages")Message[] posMessages) {
		super();
		this.posMessages = posMessages;
	}

	@JsonIgnore
	public String toPipechar() {
		StringBuffer sb = new StringBuffer();
		for (Message p : posMessages)
			sb.append(p.toString()).append('|');
		return sb.toString();
	}

}
