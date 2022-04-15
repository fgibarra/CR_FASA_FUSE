package cl.ahumada.peya.requester.servicios;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.pedidosya.reception.sdk.models.Address;
import com.pedidosya.reception.sdk.models.Attachment;
import com.pedidosya.reception.sdk.models.Detail;
import com.pedidosya.reception.sdk.models.Discount;
import com.pedidosya.reception.sdk.models.Order;
import com.pedidosya.reception.sdk.models.Payment;
import com.pedidosya.reception.sdk.models.Product;
import com.pedidosya.reception.sdk.models.User;

import cl.ahumada.esb.dto.pharol.json.Local;
import cl.ahumada.esb.dto.pharol.json.Stock;
import cl.ahumada.esb.dto.pharolV2.json.CarroCompras;
import cl.ahumada.esb.dto.pharolV2.json.Cliente;
import cl.ahumada.esb.dto.pharolV2.json.DatosEntrega;
import cl.ahumada.esb.dto.pharolV2.json.Descuento;
import cl.ahumada.esb.dto.pharolV2.json.MedioPago;
import cl.ahumada.esb.dto.pharolV2.json.Producto;
import cl.ahumada.esb.dto.pharolV2.orquestadorDescuentos.comun.Message;
import cl.ahumada.esb.dto.pharolV4.pedidos.PedidosRequest;
import cl.ahumada.fuse.requesterPeya.lib.monitorLogistico.ItemML;
import cl.ahumada.fuse.requesterPeya.lib.monitorLogistico.MedioPagoML;
import cl.ahumada.fuse.requesterPeya.lib.monitorLogistico.MonitorLogisticoRequest;
import cl.ahumada.peya.requester.main.PeyaController;
import cl.ahumada.peya.requester.servicios.dto.ReconciliaDTO;

public class ObjectFactory {

	private Logger logger = Logger.getLogger(getClass());
	Long totalBoleta;
	private Properties integracionProps;
	
	
	public Local factoryLocal(Order order) {
		// generar el request para consultar stock

		String valor = order.getRestaurant().getIntegrationCode();
		if (valor == null)
			throw new RuntimeException("Aun no estan asignados los numero de local");
		Long numeroLocal = Long.valueOf(valor);
		List<Stock> stock = new ArrayList<Stock>();
		for (Detail detail : order.getDetails()) {
			Long codigoProducto = Long.valueOf(detail.getProduct().getIntegrationCode());
			Long cantidad = (long)detail.getQuantity();
			stock.add(new Stock(codigoProducto, cantidad));
		}
		return new Local(numeroLocal, stock.toArray(new Stock[0]));
	}

	// aca se usa el pedido reconciliado
	public PedidosRequest factoryPedidosRequest(Map<String, Object> map) throws IOException {
		Order order = (Order) map.get("order");
		
		integracionProps = (Properties) map.get("integracionProps");
		PedidosRequest request = null;
		long idTransaccion = order.getId();
//		long idTransaccion = new java.util.Date().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String fecha = sdf.format(new java.util.Date());
		String valor = order.getRestaurant().getIntegrationCode();
		if (valor == null)
			throw new RuntimeException("Aun no estan asignados los numero de local");
		Long numeroLocal = Long.valueOf(valor);
		Long costoDespacho = order.getPayment().getShippingNoDiscount().longValue();
		CarroCompras carroCompras = factoryCarroCompras(map);

		MedioPago[] medioPago = factoryMedioPago(order.getPayment());
		Cliente cliente = factoryCliente(order.getUser());
		DatosEntrega datosEntrega = factoryDatosEntrega(order);
		Message[] posMessages = null;

		logger.debug(String.format("factoryPedidosRequest: idTransaccion:%s fecha:%s numeroLocal=%s costoDespacho:%s carroCompras:%s, medioPago=%s cliente=%s datosEntrega=%s",
				idTransaccion, fecha, numeroLocal,
				costoDespacho, carroCompras, medioPago[0],cliente, datosEntrega));

		try {
			request = new PedidosRequest("PEDIDOSYA", idTransaccion, 0l, fecha, numeroLocal,
					costoDespacho,carroCompras, medioPago, cliente, datosEntrega, posMessages);
		} catch (Exception e) {
			logger.error(String.format("factoryPedidosRequest: idTransaccion:%s fecha:%s numeroLocal=%s costoDespacho:%s carroCompras:%s, medioPago=%s cliente=%s datosEntrega=%s clase=%s",
				idTransaccion, fecha, numeroLocal,
				costoDespacho, carroCompras, medioPago[0],cliente, datosEntrega, request.getClass().getSimpleName()), e);
		}

		return request;
	}

	public CarroCompras factoryCarroCompras(Map<String,Object> map) throws IOException {
		Order order = (Order)map.get(ServiciosdeBus.ORDER_KEY);
		List<String> productosStock0 = generaListaRemueve(map);
		List<ReconciliaDTO> listaFaltaStock = generaListaModifica(map);
		Producto producto[] = factoryProducto(order.getDetails(), productosStock0, listaFaltaStock);
		//Payment payment = order.getPayment();

		Long descuentoTotal = getDescuento(order.getDiscounts()); // ??????????????????

		Long neto = 0l; //payment.getTotal() != null ? payment.getTotal().longValue() : 0;
		Long iva = 0l; //payment.getTax() != null ? payment.getTax().longValue() : 0;
		Double valor  = calculaNuevoTotal(order.getDetails(), productosStock0, listaFaltaStock);
		totalBoleta = sumatoria(valor.longValue(), order.getDiscounts());
		CarroCompras carroCompra = new CarroCompras(producto, descuentoTotal, neto, iva, totalBoleta);
		return carroCompra;
	}

	private Long sumatoria(Long valor, List<Discount> discounts) {
		
		String descuentosAplicables = 	integracionProps.getProperty("descuentosAplicables");
		for (Discount discount : discounts) {
			String codigoDescuento = discount.getCode();
			if (descuentosAplicables.indexOf(codigoDescuento) >= 0) {
				valor -= discount.getAmount() != null ? discount.getAmount().longValue() : 0l;
			}
		}
		return valor;
	}

	public Long getDescuento(List<Discount> discounts) {
		long totalDescuentos = 0l;
		if (discounts != null) {
			for (Discount discount : discounts) {
				String codigoDescuento = discount.getCode();
				String descuentosAplicables = 	integracionProps.getProperty("descuentosAplicables");

				if (descuentosAplicables.indexOf(codigoDescuento) >= 0)
					totalDescuentos += discount.getAmount().longValue();
			}
		}
		return totalDescuentos;
	}

	public Producto[] factoryProducto(List<Detail> details, List<String> productosStock0, List<ReconciliaDTO>listaFaltaStock) {
		Map<Object, Producto> mapSkus = new HashMap<Object,Producto>();
		HashSet<String> setProductosStock0 = productosStock0 != null ?new HashSet<String>(productosStock0) : new HashSet<String>();
		Map<String, ReconciliaDTO> mapFaltaStock = listaFaltaStock != null ? listaFaltaStock.stream().collect(Collectors.toMap(ReconciliaDTO::getCodigoProducto, Function.identity())) : new HashMap<String, ReconciliaDTO>();

		List<Producto> productos = new ArrayList<Producto>();
		for (Detail detail : details) {
			Object codigoProducto = detail.getProduct().getIntegrationCode().toString().trim();
			Long cantidad = detail.getQuantity().longValue();
			Long precioUnitario = detail.getUnitPrice().longValue();
			Long total = detail.getSubtotal().longValue();

			if (setProductosStock0.contains((String)codigoProducto))
				continue;
			if (mapFaltaStock.containsKey((String)codigoProducto)) {
				ReconciliaDTO dto = mapFaltaStock.get((String)codigoProducto);
				cantidad = dto.getCantidad().longValue();
				total = (long) (dto.getPrecioUnitario() * dto.getCantidad());
			}

			Descuento[] descuentos = factoryDescuentos(detail);

			Producto pAnterior = mapSkus.remove(codigoProducto);
			if (pAnterior != null) {
				cantidad += pAnterior.cantidad;
				total += pAnterior.total;
			} 

			Producto nProducto = new Producto(codigoProducto, cantidad, precioUnitario, total, descuentos);
			productos.add(nProducto);
			mapSkus.put(codigoProducto, nProducto);
			
		}
		return productos.toArray(new Producto[0]);
	}

	public Descuento[] factoryDescuentos(Detail detail) {
		String type = "DP";
		long valorDescuento = detail.getDiscount() != null ? detail.getDiscount().longValue() : 0;
		if (valorDescuento == 0)
			return null;
		Object codigoDescuento = "300000";
		String descripcionDescuento = "PEDIDOSYA";
		boolean aplicar = true;
		Descuento descuento = new Descuento(type, valorDescuento, codigoDescuento, descripcionDescuento, aplicar);
		return new Descuento[] { descuento };
	}

	public MedioPago[] factoryMedioPago(Payment payment) {
		List<MedioPago> medio = new ArrayList<MedioPago>();
		int formaPago = 3;
//		int monto = payment.getAmountNoDiscount().intValue();
		int monto = totalBoleta.intValue();
		medio.add(new MedioPago(formaPago, monto, 0, "0", "0", 0));
		return medio.toArray(new MedioPago[0]);
	}

	public Cliente factoryCliente(User user) {
		String nombres = user.getName();
		String apellidos = user.getLastName();
		String rut = user.getIdentityCard() != null && user.getIdentityCard().length() > 0 ? user.getIdentityCard() : "762114259";
		String mail = user.getEmail();
		return new Cliente (nombres, apellidos, rut, mail);
	}

	public DatosEntrega factoryDatosEntrega(Order order) {
		Address address = order.getAddress();
		String telefono = address.getPhone();
		String calle = address.getStreet();
		String numero = address.getDoorNumber();
		String dpto = address.getComplement();
		if (dpto != null)
			dpto = dpto.replace('|', ' ');
		String comuna = address.getArea();
		String region = address.getCity();
		String tipoEntrega = "1";
		java.util.Date registeredDate = order.getRegisteredDate();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String ruta = sdf.format(registeredDate);
		java.util.Date deliveryDate = order.getDeliveryDate();
		String fechaEntregaDesde = sdf.format(deliveryDate);
		java.util.Date entregaDate = new java.util.Date((deliveryDate.getTime()+10*60*1000l));
		String fechaEntregaHasta = sdf.format(entregaDate);
		return new DatosEntrega(telefono,calle, numero, dpto, comuna, region, tipoEntrega, ruta, fechaEntregaDesde, fechaEntregaHasta);
	}

	@SuppressWarnings("unchecked")
	public List<String> generaListaRemueve(Map<String, Object> map) {
		List<String> productosStock0 = (List<String>)map.get(ServiciosdeBus.PRODUCTOS_STOCK0_KEY);
		return productosStock0;
	}

	/**
	 * @param map
	 * @return
	 * a partir de map.PRODUCTOS_STOCK_PARCIAL_KEY hay que complementar el precio unitario y crear
	 * la lista
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public List<ReconciliaDTO> generaListaModifica(Map<String, Object> map) throws IOException {
		ServiciosdeBus serviciosdeBus = new ServiciosdeBus(PeyaController.getInstance().getApiClient());
		Order order = (Order) map.get(ServiciosdeBus.ORDER_KEY);
		List<ReconciliaDTO> listaFaltaStock = (List<ReconciliaDTO>)map.get(ServiciosdeBus.PRODUCTOS_SIN_STOCK_KEY);
		if (listaFaltaStock != null) {
			for (ReconciliaDTO dto : listaFaltaStock) {
				//TODO puede que se utilice el deatil.getUnitPrice() y no sirva este
				Product producto = serviciosdeBus.findProducto(dto.getCodigoProducto(), order.getDetails());
				if (producto != null)
					dto.setPrecioUnitario(producto.getPrice());
			}
		}
		return listaFaltaStock;
	}

	public Double calculaNuevoTotal(List<Detail>details, List<String> productosStock0, List<ReconciliaDTO> listaFaltaStock) {
		Double newTotal = 0d;
		HashSet<String> setProductosStock0 = productosStock0 != null ?new HashSet<String>(productosStock0) : new HashSet<String>();
		Map<String, ReconciliaDTO> mapFaltaStock = listaFaltaStock != null ? listaFaltaStock.stream().collect(Collectors.toMap(ReconciliaDTO::getCodigoProducto, Function.identity())) : new HashMap<String, ReconciliaDTO>();
		for (Detail detail : details) {
			Product prod = detail.getProduct();
			String codigo = prod.getIntegrationCode();
			if (setProductosStock0.contains(codigo))
				continue;
			if (mapFaltaStock.containsKey(codigo)) {
				ReconciliaDTO dto = mapFaltaStock.get(codigo);
				newTotal += (dto.getPrecioUnitario() * dto.getCantidad());
				continue;
			}
			newTotal += (prod.getPrice() * prod.getContentQuantity());
		}
		return newTotal;
	}

	public MonitorLogisticoRequest factoryMonitorLogisticoRequest(Map<String, Object> map){
		// TODO: falta definir lo que se envia al Monitor logistico
		Order order = (Order) map.get("order");
		integracionProps = (Properties) map.get("integracionProps");
		String idTransaccion = String.format("%d", order.getId());
		List<String> recetas = null;
		if (order.getAttachments() != null && !order.getAttachments().isEmpty()) {
			recetas = new ArrayList<String>();
			for (Attachment attachment: order.getAttachments())
				recetas.add(attachment.getUrl());
		}
		DatosEntrega datosEntrega = factoryDatosEntrega(order);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fechaCreacion = sdf.format(new java.util.Date());
		
		String numeroLocal = order.getRestaurant().getIntegrationCode();
		if (numeroLocal == null)
			throw new RuntimeException("Aun no estan asignados los numero de local");
		//Long numeroLocal = Long.valueOf(valor);
		Long costoDespacho = order.getPayment().getShippingNoDiscount().longValue();
		CarroCompras carroCompras;
		try {
			carroCompras = factoryCarroCompras(map);
		} catch (IOException e) {
			logger.error("factoryMonitorLogisticoRequest: creando carro compras", e);
			return null;
		}

		MedioPago[] medioPago = factoryMedioPago(order.getPayment());
		Cliente cliente = factoryCliente(order.getUser());

		logger.debug(String.format("factoryMonitorLogisticoRequest: idTransaccion:%s fecha:%s numeroLocal=%s costoDespacho:%s carroCompras:%s, medioPago=%s cliente=%s datosEntrega=%s",
				idTransaccion, fechaCreacion, numeroLocal,
				costoDespacho, carroCompras, medioPago[0],cliente, datosEntrega));
		
		MonitorLogisticoRequest request = new MonitorLogisticoRequest(idTransaccion, recetas);
		request.setCiudad(datosEntrega.comuna);
		request.setComuna(datosEntrega.region);
		request.setFechaCreacion(fechaCreacion);
		request.setEmail(cliente.mail);
		request.setExternalNumber("0");
		request.setNombre(cliente.nombres);
		request.setApellido(cliente.apellidos);
		request.setRegion(datosEntrega.comuna);
		request.setTelefono(datosEntrega.telefono);
		request.setState(datosEntrega.comuna);
		request.setDireccion(String.format("%s %s %s", datosEntrega.calle, datosEntrega.numero, datosEntrega.dpto!=null?datosEntrega.dpto:""));
		request.setTipoEnvio("RETIRO");
		request.setZipCode("00000");
		request.setClickCollect("1");
		request.setEventType("");
		request.setInternalNumber("");
		request.setCodigoComercio("KIOSKO");
		request.setRut("");
		request.setLatDireccion(1.0d);
		request.setLongDireccion(1.0d);
		request.setHomeType("");
		request.setInitialHour(datosEntrega.fechaEntregaDesde);
		request.setFinalHour(datosEntrega.fechaEntregaHasta);
		request.setTransactionCode("0");
		request.setTotalRemision(carroCompras.totalBoleta);
		request.setCurrencyIsocode("CLP");
		request.setCostoDespacho(order.getPayment().getShippingNoDiscount().longValue());
		request.setRequiereValidacionQF(Boolean.FALSE);
		request.setItems(factoryItemsML(numeroLocal, carroCompras.producto));
		request.setMediosPago(factoryMediosPagoML(medioPago));
		
		return request;
	}

	private MedioPagoML[] factoryMediosPagoML(MedioPago[] mediosPago) {
		List<MedioPagoML> mediosPagoML = null;
		if (mediosPago == null || mediosPago.length == 0)
			return null;
		
		mediosPagoML = new ArrayList<MedioPagoML>();
		for (MedioPago medioPago : mediosPago) {
			MedioPagoML medioPagoML = new MedioPagoML();
			medioPagoML.setCodCierre("RECETARIO");
			medioPagoML.setCodigoAutorizacion(medioPago.codigoAutorizacion);
			medioPagoML.setCodigoComercioTbk(medioPago.codigoComercioTbk);
			medioPagoML.setFormaPago(medioPago.formaPago);
			medioPagoML.setIdVenta("");
			medioPagoML.setMonto(medioPago.monto);
			medioPagoML.setObjUnicoTrx(medioPago.objUnicoTrx);
			medioPagoML.setTipoPago(medioPago.tipoPago);
			mediosPagoML.add(medioPagoML);
		}
		return mediosPagoML.toArray(new MedioPagoML[0]);
	}

	private ItemML[] factoryItemsML(String numeroLocal, Producto[] productos) {
		List<ItemML> items = null;
		if (productos == null || productos.length == 0)
			return null;
		
		items = new ArrayList<ItemML>();
		for (Producto producto : productos) {
			ItemML item = new ItemML(numeroLocal, producto.codigoProducto.toString(), producto.cantidad, producto.precioUnitario);
			items.add(item);
		}
		return items.toArray(new ItemML[0]);
	}

}
