package cl.ahumada.fuse.stock.procesor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.json.stock.ConsultaStockResponse;
import cl.ahumada.fuse.json.stock.Local;
import cl.ahumada.fuse.json.stock.PharolError;
import cl.ahumada.fuse.json.stock.Stock;
import cl.ahumada.fuse.utils.Constantes;

public class TransformaSPaResponse implements Processor {

	private Logger logger = Logger.getLogger(getClass());

	@Override
	public void process(Exchange exchange) throws Exception {
		Object respuesta = exchange.getIn().getBody();
		Object response = null;
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (respuesta instanceof List) {
			@SuppressWarnings("unchecked")
			List<List<Object>> resultado = (List<List<Object>>) respuesta;
			logger.info(String.format("process: recupera resultado %d objetos",(resultado!=null?resultado.size(): 0)));

			if (resultado != null && resultado.size() > 0) {
				// Viene: empresa, local, producto, stock
				List<Local> datosLocales = new ArrayList<Local>();
				Map<Long, Local> mapLocales = new HashMap<Long, Local>();

				for (int i=0; i<resultado.size(); i++) {
					List<Object> fila = resultado.get(i);
					Long numeroLocal = Constantes.toLong(fila.get(0));
					Long codigoProducto = Constantes.toLong(fila.get(1));
					Long cantidad = Constantes.toLong(fila.get(2));
					logger.info(String.format("process: i=%d numeroLocal=%d codigoProducto=%d cantidad=%d",
							i, numeroLocal,codigoProducto,cantidad));
					Stock stock = new Stock(codigoProducto, cantidad);
					/*
					if (fila.size() > 3)
						stock.setSeccion(Constantes.toLong(fila.get(3)));
					if (fila.size() > 4)
						stock.setNombreProducto((String)fila.get(4));
					*/
					Stock stocks[] = new Stock[1];
					stocks[0] = stock;
					Local local = mapLocales.get(numeroLocal);
					if (local == null) {
						local = new Local(numeroLocal, stocks);
						datosLocales.add(local);
						mapLocales.put(numeroLocal, local);
					} else
						local.addStock(stock);

				}
				response = new ConsultaStockResponse(datosLocales.toArray(new Local[0]));
			} else {
				response = new PharolError("ERROR_BB_DD", "Error al recuperar los datos");
			}
			logger.info("process: responde:"+response.toString());
		} else {
			response = new PharolError("ERROR_BB_DD", "Error al recuperar los datos");
		}
		map.put("resp", response);
        exchange.getIn().setBody(map);
	}

}
