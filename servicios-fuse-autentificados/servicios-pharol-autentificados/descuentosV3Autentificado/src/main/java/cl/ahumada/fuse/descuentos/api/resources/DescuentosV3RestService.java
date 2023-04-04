package cl.ahumada.fuse.descuentos.api.resources;

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

@Path("/obtiene")
public class DescuentosV3RestService extends AutentificacionRestService {

	@EndpointInject(uri = "direct:start")
	ProducerTemplate producer;

	Logger logger = Logger.getLogger(getClass());


	@RolesAllowed({"pharol","ventas"})
	@POST
	@Path("/descuentos")
	@Consumes("application/json")
	@Produces("application/json; charset=UTF-8")
	public Object generaDescuento(@Context HttpHeaders headers, DescuentosRequest in_msg) {
		String roles[] = getRoles("generaDescuento", new Class[] {HttpHeaders.class, DescuentosRequest.class});
		logger.info(String.format("generaDescuento: in_msg: %s - %s", 
				in_msg.getClass().getSimpleName(), in_msg));
		if(isValidRequest(headers, roles))
			return producer.requestBody(in_msg, String.class);
		return getForbidden();
	}

}
