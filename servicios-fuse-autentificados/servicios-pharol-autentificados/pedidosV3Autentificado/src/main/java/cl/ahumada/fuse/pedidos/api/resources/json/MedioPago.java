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
	private static final long serialVersionUID = -7623709619110923557L;
	@JsonProperty("forma_pago")
	public final int formaPago;
	@JsonProperty("monto")
	public final int monto;
	@JsonProperty("codigo_autorizacion")
	public final int codigoAutorizacion;
	@JsonProperty("codigo_comercio_tbk")
	public final String codigoComercioTbk;
	@JsonProperty("obj_unico_trx")
	public final String objUnicoTrx;
	@JsonProperty("tipo_pago")
	public final int tipoPago;

	@JsonCreator
	public MedioPago(@JsonProperty("forma_pago") int formaPago,
			@JsonProperty("monto") int monto,
			@JsonProperty("codigo_autorizacion") int codigoAutorizacion,
			@JsonProperty("codigo_comercio_tbk") String codigoComercioTbk,
			@JsonProperty("obj_unico_trx") String objUnicoTrx,
			@JsonProperty("tipo_pago") int tipoPago) {
		this.formaPago = formaPago;
		this.monto = monto;
		this.codigoAutorizacion = codigoAutorizacion;
		this.codigoComercioTbk = codigoComercioTbk;
		this.objUnicoTrx = objUnicoTrx;
		this.tipoPago = tipoPago;
	}

	@JsonIgnore
	public String toPipechar() {
		StringBuffer sb = new StringBuffer();
		sb.append(formaPago).append('|');
		sb.append(monto).append('|');
		sb.append(codigoAutorizacion).append('|');
		sb.append(codigoComercioTbk).append('|');
		sb.append(objUnicoTrx).append('|');
		sb.append(tipoPago).append('|');
		return sb.toString();
	}
}
