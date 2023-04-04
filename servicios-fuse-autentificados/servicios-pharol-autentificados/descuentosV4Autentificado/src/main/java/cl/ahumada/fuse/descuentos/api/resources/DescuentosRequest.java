package cl.ahumada.fuse.descuentos.api.resources;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.descuentos.api.resources.json.ConveniosAbf;
import cl.ahumada.fuse.descuentos.api.resources.json.Producto;
import cl.ahumada.fuse.descuentos.api.resources.json.Promovar;
import cl.ahumada.fuse.utils.JSonUtilities;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DescuentosRequest implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = -8781529856236337493L;
	@JsonProperty("numero_local")
	public final String numeroLocal;
	@JsonProperty("producto")
	public final Producto[] producto;
	@JsonProperty("BIN")
	public final String bin;
	@JsonProperty("promo_var")
	public final Promovar[] promoVar;
	@JsonProperty("rut")
	public final String rut;
	@JsonProperty("cupon")
	public final String cupon;
	@JsonProperty("convenios_abf")
	public final ConveniosAbf conveniosAbf;

	@JsonCreator
	public DescuentosRequest(@JsonProperty("numero_local")String numeroLocal, 
			@JsonProperty("producto")Producto[] producto, 
			@JsonProperty("BIN")String bin, 
			@JsonProperty("promo_var")Promovar[] promoVar, 
			@JsonProperty("rut")String rut,
			@JsonProperty("cupon")String cupon, 
			@JsonProperty("convenios_abf")ConveniosAbf conveniosAbf) {
		super();
		this.numeroLocal = numeroLocal;
		this.producto = producto;
		this.bin = bin;
		this.promoVar = promoVar;
		this.rut = rut;
		this.cupon = cupon;
		this.conveniosAbf = conveniosAbf;
	}

	@JsonIgnore
	public String toString() {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append(JSonUtilities.getInstance().java2json(this));
		} catch (Exception e) {
			sb.append("No pudo convertir a JSON");
		}
		return sb.toString();
	}
	
	public String getNumeroLocal() {
		return numeroLocal;
	}

	public Producto[] getProducto() {
		return producto;
	}

	public String getBin() {
		return bin;
	}

	public Promovar[] getPromoVar() {
		return promoVar;
	}

	public String getRut() {
		return rut;
	}

	public String getCupon() {
		return cupon;
	}

	public ConveniosAbf getConveniosAbf() {
		return conveniosAbf;
	}

}
