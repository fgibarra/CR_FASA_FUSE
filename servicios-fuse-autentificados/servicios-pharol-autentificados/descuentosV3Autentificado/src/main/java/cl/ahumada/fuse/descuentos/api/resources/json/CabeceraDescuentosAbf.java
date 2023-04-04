package cl.ahumada.fuse.descuentos.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CabeceraDescuentosAbf implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 5208586862707488759L;
	@JsonProperty("NumeroTransaccion")
	private Integer numeroTransaccion;
	@JsonProperty("CodigoSistema")
	private String codigoSistema;
	@JsonProperty("Local")
	private String local;
	@JsonProperty("Caja")
	private Integer caja;
	@JsonProperty("Vendedor")
	private String vendedor;
	@JsonProperty("FechaTrx")
	private String fechaTrx;
	@JsonProperty("FechaContable")
	private String fechaContable;
	@JsonProperty("FechaReceta")
	private String fechaReceta;
	@JsonProperty("CredencialBeneficiario")
	private String credencialBeneficiario;
	@JsonProperty("CodigoBeneficiario")
	private String codigoBeneficiario;
	
	public CabeceraDescuentosAbf(@JsonProperty("NumeroTransaccion")Integer numeroTransaccion, 
			@JsonProperty("CodigoSistema")String codigoSistema, 
			@JsonProperty("Local")String local, 
			@JsonProperty("Caja")Integer caja,
			@JsonProperty("Vendedor")String vendedor, 
			@JsonProperty("FechaTrx")String fechaTrx, 
			@JsonProperty("FechaContable")String fechaContable, 
			@JsonProperty("FechaReceta")String fechaReceta, 
			@JsonProperty("CredencialBeneficiario")String credencialBeneficiario,
			@JsonProperty("CodigoBeneficiario")String codigoBeneficiario) {
		super();
		this.numeroTransaccion = numeroTransaccion;
		this.codigoSistema = codigoSistema;
		this.local = local;
		this.caja = caja;
		this.vendedor = vendedor;
		this.fechaTrx = fechaTrx;
		this.fechaContable = fechaContable;
		this.fechaReceta = fechaReceta;
		this.credencialBeneficiario = credencialBeneficiario;
		this.codigoBeneficiario = codigoBeneficiario;
	}

	public Integer getNumeroTransaccion() {
		return numeroTransaccion;
	}

	public void setNumeroTransaccion(Integer numeroTransaccion) {
		this.numeroTransaccion = numeroTransaccion;
	}

	public String getCodigoSistema() {
		return codigoSistema;
	}

	public void setCodigoSistema(String codigoSistema) {
		this.codigoSistema = codigoSistema;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public Integer getCaja() {
		return caja;
	}

	public void setCaja(Integer caja) {
		this.caja = caja;
	}

	public String getVendedor() {
		return vendedor;
	}

	public void setVendedor(String vendedor) {
		this.vendedor = vendedor;
	}

	public String getFechaTrx() {
		return fechaTrx;
	}

	public void setFechaTrx(String fechaTrx) {
		this.fechaTrx = fechaTrx;
	}

	public String getFechaContable() {
		return fechaContable;
	}

	public void setFechaContable(String fechaContable) {
		this.fechaContable = fechaContable;
	}

	public String getFechaReceta() {
		return fechaReceta;
	}

	public void setFechaReceta(String fechaReceta) {
		this.fechaReceta = fechaReceta;
	}

	public String getCredencialBeneficiario() {
		return credencialBeneficiario;
	}

	public void setCredencialBeneficiario(String credencialBeneficiario) {
		this.credencialBeneficiario = credencialBeneficiario;
	}

	public String getCodigoBeneficiario() {
		return codigoBeneficiario;
	}

	public void setCodigoBeneficiario(String codigoBeneficiario) {
		this.codigoBeneficiario = codigoBeneficiario;
	}

	
}
