package cl.ahumada.fuse.promociones.api.resources;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.promociones.api.resources.json.Producto;
import cl.ahumada.fuse.utils.json.JSonUtilities;

public class PromocionesRequest implements Serializable {

	/**
	 *
	 */
	@JsonIgnore
	private static final long serialVersionUID = 3808444127713604775L;

	@JsonProperty("Numero_local")
	public Integer numeroLocal;
	@JsonProperty("Producto")
	public Producto[] producto;
	@JsonProperty("BIN")
	public Integer bin;
	@JsonProperty("promo_var")
	public Integer promoVar;

	@JsonCreator
	public PromocionesRequest(@JsonProperty("Numero_local") Integer numeroLocal,
			@JsonProperty("Producto") Producto[] producto,
			@JsonProperty("BIN") Integer bin,
			@JsonProperty("promo_var") Integer promoVar) {
		this.numeroLocal = numeroLocal;
		this.producto = producto;
		this.bin = bin;
		this.promoVar = promoVar;
	}

	@JsonIgnore
	public String toString() {
		try {
			return JSonUtilities.getInstance().java2json(this);
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public Integer getNumeroLocal() {
		return numeroLocal;
	}

	public Producto[] getProducto() {
		return producto;
	}

	public Integer getBin() {
		return bin;
	}

	public Integer getPromoVar() {
		return promoVar;
	}
}
