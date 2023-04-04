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
public class A01Response implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = -7435183811075023511L;
	@JsonProperty("autorizado")
	public String autorizado;
	@JsonProperty("monto_excedente")
	public Long montoExcedente;
	@JsonProperty("codigo_autorizador_servicio")
	public String codigoAutorizadorServicio;
	
	@JsonCreator
	public A01Response(@JsonProperty("autorizado")String autorizado, 
			@JsonProperty("monto_excedente")Long montoExcedente, 
			@JsonProperty("codigo_autorizador_servicio")String codigoAutorizadorServicio) {
		super();
		this.autorizado = autorizado;
		this.montoExcedente = montoExcedente;
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

	public String getAutorizado() {
		return autorizado;
	}

	public Long getMontoExcedente() {
		return montoExcedente;
	}

	public String getCodigoAutorizadorServicio() {
		return codigoAutorizadorServicio;
	}
	
}
