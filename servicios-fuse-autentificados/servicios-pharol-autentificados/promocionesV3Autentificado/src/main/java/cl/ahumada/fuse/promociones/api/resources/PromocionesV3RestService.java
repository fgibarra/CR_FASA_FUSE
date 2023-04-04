package cl.ahumada.fuse.promociones.api.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;

import javax.annotation.security.RolesAllowed;
import cl.ahumada.fuse.api.AutentificacionRestService;

/**
 * @author fernando
 *
 * Recibe el request exponiendo el apirest
 * 
 *  Deja el request en el body
 */
@Path("/lista")
public class PromocionesV3RestService extends AutentificacionRestService {

	@EndpointInject(uri = "direct:start")
	ProducerTemplate producer;

	Logger logger = Logger.getLogger(getClass());

	@RolesAllowed({"pharol","ventas"})
	@POST
	@Path("/buscar")
	@Consumes("application/json")
	@Produces("application/json; charset=UTF-8")
	public Object consultaPromocion(@Context HttpHeaders headers, PromocionesRequest in_msg) {
		String roles[] = getRoles("consultaPromocion", new Class[] {HttpHeaders.class, PromocionesRequest.class});
		logger.info(String.format("consultaPromocion: in_msg: %s - %s", 
				in_msg.getClass().getSimpleName(), in_msg));
		if(isValidRequest(headers, roles))
			return producer.requestBody(in_msg, String.class);
		return getForbidden();
	}
}
