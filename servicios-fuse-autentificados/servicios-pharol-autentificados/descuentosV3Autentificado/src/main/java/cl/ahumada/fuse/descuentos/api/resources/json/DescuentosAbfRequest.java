package cl.ahumada.fuse.descuentos.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.utils.JSonUtilities;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DescuentosAbfRequest implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 2212360963554710304L;
	@JsonProperty("cabecera")
	private CabeceraDescuentosAbf cabecera;
	@JsonProperty("detalle")
	private DetalleDescuentosAbf[] detalle;
	
	public DescuentosAbfRequest(@JsonProperty("cabecera")CabeceraDescuentosAbf cabecera, 
			@JsonProperty("detalle")DetalleDescuentosAbf[] detalle) {
		super();
		this.cabecera = cabecera;
		this.detalle = detalle;
	}
	
	@JsonIgnore
	@Override
	public String toString() {
		try {
			return JSonUtilities.getInstance().java2json(this);
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public CabeceraDescuentosAbf getCabecera() {
		return cabecera;
	}

	public void setCabecera(CabeceraDescuentosAbf cabecera) {
		this.cabecera = cabecera;
	}

	public DetalleDescuentosAbf[] getDetalle() {
		return detalle;
	}

	public void setDetalle(DetalleDescuentosAbf[] detalle) {
		this.detalle = detalle;
	}

	
}
