package cl.ahumada.fuse.actualizacionEstados.api.resources.v1;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.utils.json.JSonUtilities;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ActualizaEstadosRequest implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 6557863996937644108L;
	@JsonProperty("order_line")
	public final String orderLine;
	@JsonProperty("estimated_delivery_date")
	public final String estimatedDeliveryDate;
	@JsonProperty("estatus_code")
	public final String estatusCode;
	@JsonProperty("comment")
	public final String comment;
	
	@JsonCreator
	public ActualizaEstadosRequest(@JsonProperty("order_line")String orderLine, 
			@JsonProperty("estimated_dellivery_date")String estimatedDeliveryDate, 
			@JsonProperty("estatus_code")String estatusCode, 
			@JsonProperty("comment")String comment) {
		super();
		this.orderLine = orderLine;
		this.estimatedDeliveryDate = estimatedDeliveryDate;
		this.estatusCode = estatusCode;
		this.comment = comment;
	}

	@Override
	@JsonIgnore
	public String toString() {
		try {
			return JSonUtilities.getInstance().java2json(this);
		} catch (Exception e) {
			return String.format("No pudo serializar %s",this.getClass().getSimpleName());
		}
	}

	public String getOrderLine() {
		return orderLine;
	}

	public String getEstimatedDeliveryDate() {
		return estimatedDeliveryDate;
	}

	public String getEstatusCode() {
		return estatusCode;
	}

	public String getComment() {
		return comment;
	}
	
}
