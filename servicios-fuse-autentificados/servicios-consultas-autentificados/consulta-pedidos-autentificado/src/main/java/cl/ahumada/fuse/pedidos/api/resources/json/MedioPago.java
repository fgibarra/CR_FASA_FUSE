package cl.ahumada.fuse.pedidos.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MedioPago implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 2045056258612140998L;
	@JsonProperty("Forma_pago")
	public Integer formaPago;
	@JsonProperty("Monto")
	public Long monto;
	@JsonProperty("Codigo_autorizacion")
	public Long codigoAutorizacion;
	@JsonProperty("Codigo_comercio_tbk")
	public String codigoComercioTbk;
	@JsonProperty("Obj_unico_trx")
	public String objUnicoTrx;
	@JsonProperty("Tipo_pago")
	public Long tipoPago;
	@JsonProperty("Forma_pago_desc")
	public String formaPagoDesc;
	@JsonProperty("Tipo_pago_desc")
	public String tipoPagoDesc;
	
	@JsonCreator
	public MedioPago(@JsonProperty("Forma_pago")Integer formaPago, 
			@JsonProperty("Tipo_pago")Long tipoPago,
			@JsonProperty("Monto")Long monto, 
			@JsonProperty("Codigo_autorizacion")Long codigoAutorizacion, 
			@JsonProperty("Codigo_comercio_tbk")String codigoComercioTbk,
			@JsonProperty("Obj_unico_trx")String objUnicoTrx,
			@JsonProperty("Forma_pago_desc")String formaPagoDesc, 
			@JsonProperty("Tipo_pago_desc")String tipoPagoDesc 
			) {
		super();
		this.formaPago = formaPago;
		this.monto = monto;
		this.codigoAutorizacion = codigoAutorizacion;
		this.codigoComercioTbk = codigoComercioTbk;
		this.objUnicoTrx = objUnicoTrx;
		this.tipoPago = tipoPago;
		this.formaPagoDesc = formaPagoDesc;
		this.tipoPagoDesc = tipoPagoDesc;
	}

}
