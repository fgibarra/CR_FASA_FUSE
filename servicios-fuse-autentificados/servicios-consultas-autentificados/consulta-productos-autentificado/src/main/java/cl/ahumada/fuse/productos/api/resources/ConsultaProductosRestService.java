package cl.ahumada.fuse.productos.api.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;

import javax.annotation.security.RolesAllowed;
import cl.ahumada.fuse.api.AutentificacionRestService;

@Path("/")
public class ConsultaProductosRestService extends AutentificacionRestService {

	@EndpointInject(uri = "direct:start")
	ProducerTemplate producer;

	Logger logger = Logger.getLogger(getClass());

	@RolesAllowed({"pharol","ventas"})
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    @Path("/detalle/buscar")
    public Object getProducto(@Context HttpHeaders headers, ConsultaProductoRequest in_msg) {
		String roles[] = getRoles("getProducto", new Class[] {HttpHeaders.class, ConsultaProductoRequest.class});
		logger.info(String.format("getProducto: in_msg: %s - %s", 
				in_msg.getClass().getSimpleName(), in_msg));
		if(isValidRequest(headers, roles))
			return producer.requestBody(in_msg, String.class);
		
		return getForbidden();
    }
	
}
