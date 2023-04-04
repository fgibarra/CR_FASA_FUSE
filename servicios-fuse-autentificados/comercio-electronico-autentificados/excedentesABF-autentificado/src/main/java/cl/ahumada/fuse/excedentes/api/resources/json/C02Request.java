package cl.ahumada.fuse.excedentes.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.utils.json.JSonUtilities;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class C02Request implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 7234689648840967152L;
	@JsonProperty("rut")
	public String rut;
	@JsonProperty("codigo_convenio")
	public String codigoConvenio;
	@JsonProperty("monto_excedente")
	public Long montoExcedente;
	@JsonProperty("numero_local")
	public Long numeroLocal;
	@JsonProperty("fecha")
	public String fecha;
	@JsonProperty("numero_pedido")
	public String numeroPedido;
	public String getNumeroPedido() {
		return numeroPedido;
	}

	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	@JsonProperty("codigo_autorizador_servicio")
	public String codigoAutorizadorServicio;
	
	@JsonCreator
	public C02Request(			
			@JsonProperty("rut")String rut, 
			@JsonProperty("codigo_convenio")String codigoConvenio, 
			@JsonProperty("monto_excedente")Long montoExcedente, 
			@JsonProperty("numero_local")Long numeroLocal, 
			@JsonProperty("fecha")String fecha,
			@JsonProperty("numero_pedido")String numeroPedido,
			@JsonProperty("codigo_autorizador_servicio")String codigoAutorizadorServicio) {
		super();
		this.rut = rut;
		this.codigoConvenio = codigoConvenio;
		this.montoExcedente = montoExcedente;
		this.numeroLocal = numeroLocal;
		this.fecha = fecha;
		this.numeroPedido = numeroPedido;
		this.codigoAutorizadorServicio = codigoAutorizadorServicio;
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

	public String getRut() {
		return rut;
	}

	public void setRut(String rut) {
		this.rut = rut;
	}

	public String getCodigoConvenio() {
		return codigoConvenio;
	}

	public void setCodigoConvenio(String codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	public Long getMontoExcedente() {
		return montoExcedente;
	}

	public void setMontoExcedente(Long montoExcedente) {
		this.montoExcedente = montoExcedente;
	}

	public Long getNumeroLocal() {
		return numeroLocal;
	}

	public void setNumeroLocal(Long numeroLocal) {
		this.numeroLocal = numeroLocal;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getCodigoAutorizadorServicio() {
		return codigoAutorizadorServicio;
	}

	public void setCodigoAutorizadorServicio(String codigoAutorizadorServicio) {
		this.codigoAutorizadorServicio = codigoAutorizadorServicio;
	}
}
