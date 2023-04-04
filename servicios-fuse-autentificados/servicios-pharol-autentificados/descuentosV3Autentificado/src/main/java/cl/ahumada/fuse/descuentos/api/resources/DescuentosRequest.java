package cl.ahumada.fuse.descuentos.api.resources;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.descuentos.api.resources.json.Producto;
import cl.ahumada.fuse.utils.JSonUtilities;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DescuentosRequest implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = -2530991092851163431L;
	@JsonProperty("numero_local")
	public final String numeroLocal;
	@JsonProperty("producto")
	public final Producto[] producto;
	@JsonProperty("BIN")
	public final String bin;
	@JsonProperty("promo_var")
	public final Long promoVar;
	@JsonProperty("rut")
	public final String rut;
	
	@JsonCreator
	public DescuentosRequest(@JsonProperty("numero_local")String numeroLocal, 
			@JsonProperty("producto")Producto[] producto, 
			@JsonProperty("BIN")String bin, 
			@JsonProperty("promovarConvenio")Long promoVar, 
			@JsonProperty("rut")String rut) {
		super();
		this.numeroLocal = numeroLocal;
		this.producto = producto;
		this.bin = bin;
		this.rut = rut;
		this.promoVar = promoVar;
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

	public String getRut() {
		return rut;
	}

	public long getPromoVar() {
		return promoVar;
	}

}
