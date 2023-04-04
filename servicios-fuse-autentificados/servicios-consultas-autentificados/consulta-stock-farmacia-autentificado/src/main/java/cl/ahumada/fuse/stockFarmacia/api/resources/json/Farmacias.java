package cl.ahumada.fuse.stockFarmacia.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Farmacias implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = -1260117045464579792L;
	@JsonProperty("Codigo")
	public String codigo;
	@JsonProperty("Direccion")
	public String direccion;
	@JsonProperty("Region")
	public String region;
	@JsonProperty("Ciudad")
	public String ciudad;
	@JsonProperty("Comuna")
	public String comuna;
	@JsonProperty("Latitud")
	public String latitud;
	@JsonProperty("Longitud")
	public String longitud;
	@JsonProperty("Horario")
	public String horario;
	@JsonProperty("Nombre_QF")
	public String nombreQF;
	@JsonProperty("SAFE")
	public String safe;
	@JsonProperty("Estacionamiento")
	public String estacionamiento;
	@JsonProperty("Farmacia24H")
	public String farmacia24H;
	
	@JsonCreator
	public Farmacias(@JsonProperty("Codigo")String codigo, 
			@JsonProperty("Direccion")String direccion, 
			@JsonProperty("Region")String region, 
			@JsonProperty("Ciudad")String ciudad, 
			@JsonProperty("Comuna")String comuna, 
			@JsonProperty("Latitud")String latitud,
			@JsonProperty("Longitud")String longitud, 
			@JsonProperty("Horario")String horario, 
			@JsonProperty("Farmacia24H")String farmacia24h,
			@JsonProperty("SAFE")String safe, 
			@JsonProperty("Estacionamiento")String estacionamiento, 
			@JsonProperty("Nombre_QF")String nombreQF) {
		super();
		this.codigo = codigo;
		this.direccion = direccion;
		this.region = region;
		this.ciudad = ciudad;
		this.comuna = comuna;
		this.latitud = latitud;
		this.longitud = longitud;
		this.horario = horario;
		this.nombreQF = nombreQF;
		this.safe = safe;
		this.estacionamiento = estacionamiento;
		farmacia24H = farmacia24h;
	}
	
}
