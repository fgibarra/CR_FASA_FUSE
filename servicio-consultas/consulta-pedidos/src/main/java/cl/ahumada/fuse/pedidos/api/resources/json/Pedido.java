package cl.ahumada.fuse.pedidos.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pedido implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 3531256363144365520L;
	@JsonProperty("Numero_pedido_ahumada")
	public String numeroPedidoAhumada;
	@JsonProperty("Numero_pedido_comercio")
	public String numeroPedidoComercio;
	@JsonProperty("Codigo_comercio")
	public String codigoComercio;
	@JsonProperty("Tipo_entrega")
	public Integer tipoEntrega;
	@JsonProperty("Fecha_entrega")
	public String fechaEntrega;
	@JsonProperty("Estado_pedido")
	public String estadoPedido;
	@JsonProperty("Tipo_entrega_desc")
	public String tipoEntregaDesc;
	@JsonProperty("Estado_pedido_desc")
	public String estadoPedidoDesc;
	@JsonProperty("Fecha_pedido")
	public String fechaPedido;
	@JsonProperty("Producto")
	public Producto[] producto;
	@JsonProperty("Trackings")
	public Trackings[] trackings;
	@JsonProperty("Medio_pago")
	public MedioPago[] medioPago;
	
	@JsonCreator
	public Pedido(@JsonProperty("Numero_pedido_ahumada")String numeroPedidoAhumada, 
			@JsonProperty("Numero_pedido_comercio")String numeroPedidoComercio, 
			@JsonProperty("Codigo_comercio")String codigoComercio,
			@JsonProperty("Tipo_entrega")Integer tipoEntrega,
			@JsonProperty("Fecha_entrega")String fechaEntrega,
			@JsonProperty("Estado_pedido")String estadoPedido,
			@JsonProperty("Tipo_entrega_desc")String tipoEntregaDesc,
			@JsonProperty("Estado_pedido_desc")String estadoPedidoDesc,
			@JsonProperty("Fecha_pedido")String fechaPedido,
			@JsonProperty("Producto")Producto[] producto,
			@JsonProperty("Trackings")Trackings[] trackings,
			@JsonProperty("Medio_pago")MedioPago[] medioPago
			) {
		super();
		this.numeroPedidoAhumada = numeroPedidoAhumada;
		this.numeroPedidoComercio = numeroPedidoComercio;
		this.codigoComercio = codigoComercio;
		this.tipoEntrega = tipoEntrega;
		this.fechaEntrega = fechaEntrega;
		this.estadoPedido = estadoPedido;
		this.tipoEntregaDesc = tipoEntregaDesc;
		this.fechaPedido = fechaPedido;
		this.producto = producto;
		this.trackings = trackings;
		this.medioPago = medioPago;
		this.estadoPedidoDesc = estadoPedidoDesc;
	}

	public void setTrackings(Trackings[] trackings) {
		this.trackings = trackings;
	}

	public void setProducto(Producto[] producto) {
		this.producto = producto;
	}
	
}
