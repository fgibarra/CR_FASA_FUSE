package cl.ahumada.peya.requester.servicios.dto;

public class ReconciliaDTO {
	String codigoProducto;
	Integer cantidad;
	Double precioUnitario;

	public ReconciliaDTO() {
		super();
	}

	public ReconciliaDTO(String codigoProducto, Integer cantidad) {
		super();
		this.codigoProducto = codigoProducto;
		this.cantidad = cantidad;
	}

	public String getCodigoProducto() {
		return codigoProducto;
	}

	public void setCodigoProducto(String codigoProducto) {
		this.codigoProducto = codigoProducto;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(Double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

}
