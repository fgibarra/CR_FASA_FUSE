package cl.ahumada.fuse.descuentos.api.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;

import javax.annotation.security.RolesAllowed;
import cl.ahumada.fuse.api.AutentificacionRestService;

@Path("/")
public class DescuentosV4RestService extends AutentificacionRestService {

	@EndpointInject(uri = "direct:start")
	ProducerTemplate producer;

	Logger logger = Logger.getLogger(getClass());

	@RolesAllowed({"pharol","ventas"})
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    @Path("/obtiene/descuentos")

    public Object getPedido(@Context HttpHeaders headers, DescuentosRequest in_msg) {
		String roles[] = getRoles("getPedido", new Class[] {HttpHeaders.class, DescuentosRequest.class});
		logger.info(String.format("getPedido: in_msg: %s - %s", 
				in_msg.getClass().getSimpleName(), in_msg));
		if(isValidRequest(headers, roles))
			return producer.requestBody(in_msg, String.class);
		return getForbidden();
    }
}
