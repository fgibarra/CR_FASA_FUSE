package cl.ahumada.fuse.promociones.api.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;

/**
 * @author fernando
 *
 * Recibe el request exponiendo el apirest
 * 
 *  Deja el request en el body
 */
@Path("/lista")
public class PromocionesV3RestService {

	@EndpointInject(uri = "direct:start")
	ProducerTemplate producer;

	Logger logger = Logger.getLogger(getClass());

	@POST
	@Path("/buscar")
	@Consumes("application/json")
	@Produces("application/json; charset=UTF-8")
	public String consultaPromocion(PromocionesRequest in_msg) {
		logger.info(String.format("consultaPromocion: in_msg: %s - %s", 
				in_msg.getClass().getSimpleName(), in_msg));
        return producer.requestBody(in_msg, String.class);
	}
}
