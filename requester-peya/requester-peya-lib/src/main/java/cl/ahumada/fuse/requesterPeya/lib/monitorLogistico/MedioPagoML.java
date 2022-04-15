package cl.ahumada.fuse.requesterPeya.lib.monitorLogistico;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MedioPagoML implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = -3546393685731954028L;
	@JsonProperty("forma_pago")
	public Integer formaPago;
	@JsonProperty("monto")
	public Integer monto;
	@JsonProperty("codigo_autorizacion")
	public Integer codigoAutorizacion;
	@JsonProperty("codigo_tbk")
	public String codigoComercioTbk;
	@JsonProperty("objeto_trx")
	public String objUnicoTrx;
	@JsonProperty("tipo_pago")
	public Integer tipoPago;
	@JsonProperty("cod_cierre")
	public String codCierre;
	@JsonProperty("id_venta")
	public String idVenta;

	public MedioPagoML() {
		super();
	}
	
	public Integer getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(Integer formaPago) {
		this.formaPago = formaPago;
	}

	public Integer getMonto() {
		return monto;
	}

	public void setMonto(Integer monto) {
		this.monto = monto;
	}

	public Integer getCodigoAutorizacion() {
		return codigoAutorizacion;
	}

	public void setCodigoAutorizacion(Integer codigoAutorizacion) {
		this.codigoAutorizacion = codigoAutorizacion;
	}

	public String getCodigoComercioTbk() {
		return codigoComercioTbk;
	}

	public void setCodigoComercioTbk(String codigoComercioTbk) {
		this.codigoComercioTbk = codigoComercioTbk;
	}

	public String getObjUnicoTrx() {
		return objUnicoTrx;
	}

	public void setObjUnicoTrx(String objUnicoTrx) {
		this.objUnicoTrx = objUnicoTrx;
	}

	public Integer getTipoPago() {
		return tipoPago;
	}

	public void setTipoPago(Integer tipoPago) {
		this.tipoPago = tipoPago;
	}

	public String getCodCierre() {
		return codCierre;
	}

	public void setCodCierre(String codCierre) {
		this.codCierre = codCierre;
	}

	public String getIdVenta() {
		return idVenta;
	}

	public void setIdVenta(String idVenta) {
		this.idVenta = idVenta;
	}

}
