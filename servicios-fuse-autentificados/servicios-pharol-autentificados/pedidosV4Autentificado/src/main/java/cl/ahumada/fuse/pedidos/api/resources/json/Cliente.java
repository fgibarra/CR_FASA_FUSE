package cl.ahumada.fuse.pedidos.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Cliente implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = -6092041001387380551L;
	@JsonProperty("nombres")
	public final String nombres;
	@JsonProperty("apellidos")
	public final String apellidos;
	@JsonProperty("rut")
	public final String rut;
	@JsonProperty("mail")
	public final String mail;

	@JsonCreator
	public Cliente(@JsonProperty("nombres") String nombres,
			@JsonProperty("apellidos") String apellidos,
			@JsonProperty("rut") String rut,
			@JsonProperty("mail") String mail) {
		super();
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.rut = rut;
		this.mail = mail;
	}

	@JsonIgnore
	public String toPipechar() {
		StringBuffer sb = new StringBuffer();
		sb.append(nombres).append('|');
		sb.append(apellidos).append('|');
		sb.append(rut).append('|');
		sb.append(mail).append('|');
		return sb.toString();
	}
}
