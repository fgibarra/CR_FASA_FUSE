package cl.ahumada.fuse.requesterPeya.lib.monitorLogistico;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MonitorLogisticoRequest implements Serializable {


	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 3128666636718738571L;
	@JsonProperty("order_number")
	public String numeroOrden;
	@JsonProperty("city_name")
	public String ciudad;
	@JsonProperty("comuna_name")
	public String comuna;
	@JsonProperty("created_at")
	public String fechaCreacion;
	@JsonProperty("email")
	public String email;
	@JsonProperty("external_number")
	public String externalNumber;
	@JsonProperty("first_name")
	public String nombre;
	@JsonProperty("last_name")
	public String apellido;
	@JsonProperty("municipio_name")
	public String region;
	@JsonProperty("phone_number")
	public String telefono;
	@JsonProperty("state_name")
	public String state;
	@JsonProperty("street_name")
	public String direccion;
	@JsonProperty("tipo_envio")
	public String tipoEnvio;
	@JsonProperty("zip_code")
	public String zipCode;
	@JsonProperty("click_collect")
	public String clickCollect;
	@JsonProperty("event_type")
	public String eventType;
	@JsonProperty("internal_number")
	public String internalNumber;
	@JsonProperty("codigo_comercio")
	public String codigoComercio;
	@JsonProperty("rut")
	public String rut;
	@JsonProperty("lat_direccion")
	public Double latDireccion;
	@JsonProperty("long_direccion")
	public Double longDireccion;
	@JsonProperty("home_type")
	public String homeType;
	@JsonProperty("initial_hour")
	public String initialHour;
	@JsonProperty("final_hour")
	public String finalHour;
	@JsonProperty("transaction_code")
	public String transactionCode;
	@JsonProperty("total_remision")
	public Long totalRemision;
	@JsonProperty("currency_isocode")
	public String currencyIsocode;
	@JsonProperty("costo_despacho")
	public Long costoDespacho;
	@JsonProperty("requiere_validacion_qf")
	public Boolean requiereValidacionQF;
	@JsonProperty("items")
	public ItemML items[];
	@JsonProperty("medios_pago")
	public MedioPagoML mediosPago[];
	@JsonProperty("imgrecetas")
	public String imgrecetas[];
	@JsonProperty("posmessages")
	public MessageML posmessages[];

	public MonitorLogisticoRequest(@JsonProperty("numero_orden")String numeroOrden, 
			@JsonProperty("receta")List<String> recetas) {
		super();
		this.numeroOrden = numeroOrden;
		this.imgrecetas = recetas != null ? recetas.toArray(new String[0]) : null ;
	}

	@JsonIgnore
	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return String.format("No pudo deserializar clase %s", getClass().getSimpleName());
		}
	}

	public String getNumeroOrden() {
		return numeroOrden;
	}

	public void setNumeroOrden(String numeroOrden) {
		this.numeroOrden = numeroOrden;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getComuna() {
		return comuna;
	}

	public void setComuna(String comuna) {
		this.comuna = comuna;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getExternalNumber() {
		return externalNumber;
	}

	public void setExternalNumber(String externalNumber) {
		this.externalNumber = externalNumber;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTipoEnvio() {
		return tipoEnvio;
	}

	public void setTipoEnvio(String tipoEnvio) {
		this.tipoEnvio = tipoEnvio;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getClickCollect() {
		return clickCollect;
	}

	public void setClickCollect(String clickCollect) {
		this.clickCollect = clickCollect;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getInternalNumber() {
		return internalNumber;
	}

	public void setInternalNumber(String internalNumber) {
		this.internalNumber = internalNumber;
	}

	public String getCodigoComercio() {
		return codigoComercio;
	}

	public void setCodigoComercio(String codigoComercio) {
		this.codigoComercio = codigoComercio;
	}

	public String getRut() {
		return rut;
	}

	public void setRut(String rut) {
		this.rut = rut;
	}

	public Double getLatDireccion() {
		return latDireccion;
	}

	public void setLatDireccion(Double latDireccion) {
		this.latDireccion = latDireccion;
	}

	public Double getLongDireccion() {
		return longDireccion;
	}

	public void setLongDireccion(Double longDireccion) {
		this.longDireccion = longDireccion;
	}

	public String getHomeType() {
		return homeType;
	}

	public void setHomeType(String homeType) {
		this.homeType = homeType;
	}

	public String getInitialHour() {
		return initialHour;
	}

	public void setInitialHour(String initialHour) {
		this.initialHour = initialHour;
	}

	public String getFinalHour() {
		return finalHour;
	}

	public void setFinalHour(String finalHour) {
		this.finalHour = finalHour;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public Long getTotalRemision() {
		return totalRemision;
	}

	public void setTotalRemision(Long totalRemision) {
		this.totalRemision = totalRemision;
	}

	public String getCurrencyIsocode() {
		return currencyIsocode;
	}

	public void setCurrencyIsocode(String currencyIsocode) {
		this.currencyIsocode = currencyIsocode;
	}

	public Long getCostoDespacho() {
		return costoDespacho;
	}

	public void setCostoDespacho(Long costoDespacho) {
		this.costoDespacho = costoDespacho;
	}

	public Boolean getRequiereValidacionQF() {
		return requiereValidacionQF;
	}

	public void setRequiereValidacionQF(Boolean requiereValidacionQF) {
		this.requiereValidacionQF = requiereValidacionQF;
	}

	public ItemML[] getItems() {
		return items;
	}

	public void setItems(ItemML[] items) {
		this.items = items;
	}

	public MedioPagoML[] getMediosPago() {
		return mediosPago;
	}

	public void setMediosPago(MedioPagoML[] mediosPago) {
		this.mediosPago = mediosPago;
	}

	public String[] getImgrecetas() {
		return imgrecetas;
	}

	public void setImgrecetas(String[] imgrecetas) {
		this.imgrecetas = imgrecetas;
	}

	public MessageML[] getPosmessages() {
		return posmessages;
	}

	public void setPosmessages(MessageML[] posmessages) {
		this.posmessages = posmessages;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
