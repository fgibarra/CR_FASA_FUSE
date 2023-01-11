package cl.ahumada.fuse.stock.procesor;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.stock.api.resources.ConsultaStockRequest;
import cl.ahumada.fuse.stock.api.resources.ConsultaStockResponse;
import cl.ahumada.fuse.stock.api.resources.json.Local;
import cl.ahumada.fuse.stock.api.resources.json.Stock;
import cl.ahumada.fuse.utils.json.JSonUtilities;

public class GeneraRespuestaWsConsultaStock implements Processor {

	Logger logger = Logger.getLogger(getClass());
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		Map<String, Object> map = (Map<String, Object>) exchange.getIn().getBody();
		Object stockBd = map.get("resp");
		if (stockBd instanceof ConsultaStockResponse) {
			logger.info(String.format("process: stockBd(%s):\n%s",stockBd!=null?stockBd.getClass().getSimpleName():"NULO",
					stockBd!=null?stockBd.toString() : "NO RECUPERO STOCK BD"));
			ConsultaStockRequest request = (ConsultaStockRequest)exchange.getIn().getHeader("inStockPedido");
			logger.info(String.format("process: inStockPedido request(%d):\n%s",
					request!=null?request.local.length :"NO RECUPERO REQUEST",
					request!=null?request.toString() : "NO RECUPERO REQUEST"));

			// Ordenar stock de acuerdo al stock pedido
			// 1.- generar estructuras de trabajo
			// 2.- calcular el indice de cumplimiento en stockBD
			// 2.1.- todos los productos requeridos tengan stock = numero de productos solicitados puntos
			// 2.2._ si tiene un indice=numero de productos solicitados agregar el valor de los excedentes
			// 3.- ordenar de mayor a menor por indice de cumplimiento
			// 4.- dejar resultado ordenado en el message

			// 1.-
			Map<Long, Local> mapStockBD = new HashMap<Long, Local>();
			for (Local local : ((ConsultaStockResponse)stockBd).local) {
				mapStockBD.put(local.numeroLocal, local);
			}

			// 2.-
			for (Local localRequest : request.local) {
				logger.info(String.format("numeroLocal=%d", localRequest.numeroLocal));
				Local localStock = mapStockBD.get(localRequest.numeroLocal);
				// para todo el stock requerido
				int numProductosSolicitados = localRequest.stock.length;
				logger.info(String.format("numeroLocal=%d numProductosSolicitados=%d",
						localRequest.numeroLocal, numProductosSolicitados));
				for (Stock stockRequest : localRequest.stock) {
					long codProductoRequest = stockRequest.codigoProducto;
					long cantidadRequest = stockRequest.cantidad;
					logger.info(String.format("codProductoRequest=%d cantidadRequest=%d",
							codProductoRequest, cantidadRequest));
					// buscar producto en stock
					Stock stockBD = findProducto(codProductoRequest, localStock.stock);

					if (stockBD != null) {
						logger.info(String.format("stockBD.codigoProducto=%d cantidadRequest=%d stockBD.cantidad=%d",
								stockBD.codigoProducto, cantidadRequest, stockBD.cantidad));

						if (cantidadRequest > stockBD.cantidad) {
							localStock.add((int)(stockBD.cantidad - cantidadRequest));
						}
					} else  {
						logger.info(String.format("codProductoRequest %d NOT FOUND", codProductoRequest));
						localStock.add((int)(0 - cantidadRequest));
					}
				}
			}

			// 3.-
			logger.info(String.format("process: stockBd con indices:\n%s", stockBd!=null?stockBd.toString() : "NO RECUPERO STOCK BD"));

			List<Local> locales4orden = Arrays.asList(((ConsultaStockResponse)stockBd).local);
			Collections.sort(locales4orden);
			Local [] localOrdenado = new Local[locales4orden.size()];
			localOrdenado = locales4orden.toArray(localOrdenado);
			((ConsultaStockResponse)stockBd).setLocal(localOrdenado);
			logger.info(String.format("process: stockBd con indices ordenados:\n%s", stockBd!=null?stockBd.toString() : "NO RECUPERO STOCK BD"));
		}
		
        exchange.getIn().setBody(JSonUtilities.getInstance().java2json(stockBd));
	}

	private Stock findProducto(Long codigo, Stock[] stock) {
		for (Stock s : stock) {
			if (codigo == s.codigoProducto)
				return s;
		}
		return null;
	}
}
