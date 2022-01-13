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
public class A01Request implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = -2873173302576589024L;
	@JsonProperty("rut")
	public String rut;
	@JsonProperty("codigo_convenio")
	public String codigoConvenio;
	@JsonProperty("monto_excedente")
	public Long montoExcedente;
	@JsonProperty("numero_local")
	public Long numeroLocal;
	@JsonProperty("producto")
	public Producto [] producto;
	
	@JsonCreator
	public A01Request(@JsonProperty("rut")String rut, 
			@JsonProperty("codigo_convenio")String codigoConvenio, 
			@JsonProperty("monto_excedente")Long montoExcedente, 
			@JsonProperty("numero_local")Long numeroLocal, 
			@JsonProperty("producto")Producto[] producto) {
		super();
		this.rut = rut;
		this.codigoConvenio = codigoConvenio;
		this.montoExcedente = montoExcedente;
		this.numeroLocal = numeroLocal;
		this.producto = producto;
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
	public int getCantidadProductos() {
		if (producto == null) return 0;
		return producto.length;
	}

	@JsonIgnore
	public String productos2Pipe() {
		StringBuffer sb = new StringBuffer();
		if (producto != null)
			for (Producto prod : producto) {
				sb.append(String.format("^%s^%d^%d^%d", 
						prod.getCodigoProducto(),prod.getCantidad(),prod.getTotal(),prod.getTotal()));
			}
		return sb.toString();
	}
	public String getRut() {
		return rut;
	}

	public String getCodigoConvenio() {
		return codigoConvenio;
	}

	public Long getMontoExcedente() {
		return montoExcedente;
	}

	public Long getNumeroLocal() {
		return numeroLocal;
	}

	public Producto[] getProducto() {
		return producto;
	}
	
	
}
