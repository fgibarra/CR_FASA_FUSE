package cl.ahumada.fuse.pedidos.api.resources.v4;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.pedidos.api.resources.json.CarroCompras;
import cl.ahumada.fuse.pedidos.api.resources.json.Cliente;
import cl.ahumada.fuse.pedidos.api.resources.json.DatosEntrega;
import cl.ahumada.fuse.pedidos.api.resources.json.MedioPago;
import cl.ahumada.fuse.pedidos.api.resources.json.Message;
import cl.ahumada.fuse.utils.json.JSonUtilities;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PedidosRequest implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = -2482287589163133675L;
	@JsonProperty("codigo_comercio")
	public final String codigoComercio;
	@JsonProperty("id_transaccion")
	public final String idTransaccion;
	@JsonProperty("transactionCode")
	public final long transactionCode;
	@JsonProperty("fecha")
	public final String fecha;
	@JsonProperty("numero_local")
	public final long numeroLocal;
	@JsonProperty("costo_despacho")
	public final long costoDespacho;
	@JsonProperty("carro_compras")
	public final CarroCompras carroCompras;
	@JsonProperty("medio_pago")
	public final MedioPago[] medioPago;
	@JsonProperty("cliente")
	public final Cliente cliente;
	@JsonProperty("datos_entrega")
	public final DatosEntrega datosEntrega;
	@JsonProperty("POSMessages")
	public final Message[] posMessages;

	@JsonCreator
	public PedidosRequest(@JsonProperty("codigo_comercio")String codigoComercio,
			@JsonProperty("id_transaccion")String idTransaccion,
			@JsonProperty("transactionCode")long transactionCode,
			@JsonProperty("fecha")String fecha,
			@JsonProperty("numero_local")long numeroLocal,
			@JsonProperty("costo_despacho")long costoDespacho,
		@JsonProperty("carro_compras")CarroCompras carroCompras,
		@JsonProperty("medio_pago")MedioPago[] medioPago,
		@JsonProperty("cliente")Cliente cliente,
		@JsonProperty("datos_entrega")DatosEntrega datosEntrega,
		@JsonProperty("POSMessages")Message[] posMessages) {
	super();
	this.codigoComercio = codigoComercio;
	this.idTransaccion = idTransaccion;
	this.transactionCode = transactionCode;
	this.fecha = fecha;
	this.numeroLocal = numeroLocal;
	this.costoDespacho = costoDespacho;
	this.carroCompras = carroCompras;
	this.medioPago = medioPago;
	this.cliente = cliente;
	this.datosEntrega = datosEntrega;
	this.posMessages = posMessages;
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

	@JsonIgnore
	public String toPipechar() {
		StringBuffer sb = new StringBuffer();
		sb.append(idTransaccion).append('|');
		sb.append(fecha).append('|');
		sb.append(numeroLocal).append('|');
		sb.append(codigoComercio).append('|');
		sb.append(transactionCode).append('|');
		sb.append(costoDespacho).append('|');
		return sb.toString();
	}

	@JsonIgnore
	public String medioPagotoPipechar() {
		StringBuffer sb = new StringBuffer();
		if (medioPago  != null)
			for (MedioPago mp : medioPago)
				sb.append(mp.toPipechar());
		return sb.toString();
	}

	@JsonIgnore
	public String posMessagestoPipechar() {
		StringBuffer sb = new StringBuffer();
		if (posMessages != null)
			for (Message mp : posMessages)
				sb.append(mp.toPipechar());
		return sb.toString();
	}
}
