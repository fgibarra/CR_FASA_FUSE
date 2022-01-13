package cl.ahumada.fuse.stockFarmacia.api.resources;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.stockFarmacia.api.resources.json.Producto;
import cl.ahumada.fuse.stockFarmacia.api.resources.json.Ubicacion;
import cl.ahumada.fuse.utils.json.JSonUtilities;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsultaStockRequest implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 5433810191000775848L;
	@JsonProperty("Producto")
	public Producto producto;
	@JsonProperty("Ubicacion")
	public Ubicacion ubicacion;
	@JsonProperty("Abiertas")
	public Integer abiertas;
	
	@JsonCreator
	public ConsultaStockRequest(@JsonProperty("Producto")Producto producto, 
			@JsonProperty("Ubicacion")Ubicacion ubicacion, 
			@JsonProperty("Abiertas")Integer abiertas) {
		super();
		this.producto = producto;
		this.ubicacion = ubicacion;
		this.abiertas = abiertas;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append(JSonUtilities.getInstance().java2json(this));
		} catch (Exception e) {
			sb.append("No pudo convertir a json");
		}
		return sb.toString();
	}
}
