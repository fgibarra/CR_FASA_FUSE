package cl.ahumada.fuse.descuentos.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DetalleDescuentosAbf implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 3805347246401432784L;
	@JsonProperty("DiasTratamiento")
	private Integer diasTratamiento;
	@JsonProperty("NumeroReceta")
	private Integer numeroReceta;
	@JsonProperty("UPC")
	private String upc;
	@JsonProperty("Cantidad")
	private Integer cantidad;
	@JsonProperty("PrecioLista")
	private Long precioLista;
	@JsonProperty("PrecioOferta")
	private Long precioOferta;
	@JsonProperty("RutMedico")
	private String rutMedico;
	
	public DetalleDescuentosAbf(@JsonProperty("DiasTratamiento")Integer diasTratamiento, 
			@JsonProperty("NumeroReceta")Integer numeroReceta, 
			@JsonProperty("UPC")String upc, 
			@JsonProperty("Cantidad")Integer cantidad,
			@JsonProperty("PrecioLista")Long precioLista, 
			@JsonProperty("PrecioOferta")Long precioOferta, 
			@JsonProperty("RutMedico")String rutMedico) {
		super();
		this.diasTratamiento = diasTratamiento;
		this.numeroReceta = numeroReceta;
		this.upc = upc;
		this.cantidad = cantidad;
		this.precioLista = precioLista;
		this.precioOferta = precioOferta;
		this.rutMedico = rutMedico;
	}

	public Integer getDiasTratamiento() {
		return diasTratamiento;
	}

	public void setDiasTratamiento(Integer diasTratamiento) {
		this.diasTratamiento = diasTratamiento;
	}

	public Integer getNumeroReceta() {
		return numeroReceta;
	}

	public void setNumeroReceta(Integer numeroReceta) {
		this.numeroReceta = numeroReceta;
	}

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Long getPrecioLista() {
		return precioLista;
	}

	public void setPrecioLista(Long precioLista) {
		this.precioLista = precioLista;
	}

	public Long getPrecioOferta() {
		return precioOferta;
	}

	public void setPrecioOferta(Long precioOferta) {
		this.precioOferta = precioOferta;
	}

	public String getRutMedico() {
		return rutMedico;
	}

	public void setRutMedico(String rutMedico) {
		this.rutMedico = rutMedico;
	}

}
