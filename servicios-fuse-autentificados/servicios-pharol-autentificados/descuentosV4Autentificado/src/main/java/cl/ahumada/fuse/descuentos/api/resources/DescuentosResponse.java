package cl.ahumada.fuse.descuentos.api.resources;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.descuentos.api.resources.json.ConveniosAbf;
import cl.ahumada.fuse.descuentos.api.resources.json.Message;
import cl.ahumada.fuse.descuentos.api.resources.json.Producto;
import cl.ahumada.fuse.utils.JSonUtilities;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DescuentosResponse implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 1202980497701369916L;
	@JsonProperty("transactionCode")
	public final Long transactionCode;
	@JsonProperty("producto")
	public final Producto[] producto;
	@JsonProperty("convenios_abf")
	public final ConveniosAbf conveniosAbf;
	@JsonProperty("POSMessages")
	public final Message[] posMessages;
	
	public DescuentosResponse(@JsonProperty("transactionCode")Long transactionCode, 
			@JsonProperty("producto")Producto[] producto, 
			@JsonProperty("POSMessages")Message[] posMessages,
			@JsonProperty("convenios_abf")ConveniosAbf conveniosAbf) {
		super();
		this.transactionCode = transactionCode;
		this.producto = producto;
		this.posMessages = posMessages;
		this.conveniosAbf = conveniosAbf;
	}

	@JsonIgnore
	public String toString() {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append(JSonUtilities.getInstance().java2json(this));
		} catch (Exception e) {
			sb.append("No pudo convertir a JSON");
		}
		return sb.toString();
	}
	
	public Long getTransactionCode() {
		return transactionCode;
	}

	public Producto[] getProducto() {
		return producto;
	}

	public Message[] getPosMessages() {
		return posMessages;
	}
	
}
