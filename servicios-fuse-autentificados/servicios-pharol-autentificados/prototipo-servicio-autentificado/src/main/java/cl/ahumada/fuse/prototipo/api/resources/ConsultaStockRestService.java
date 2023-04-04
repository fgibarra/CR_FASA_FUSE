package cl.ahumada.fuse.prototipo.api.resources;

import javax.annotation.security.RolesAllowed;
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

import cl.ahumada.fuse.api.AutentificacionRestService;
import cl.ahumada.fuse.json.stock.ConsultaStockRequest;


@Path("/")
public class ConsultaStockRestService extends AutentificacionRestService {

	private Logger logger = Logger.getLogger(getClass());

	@EndpointInject(uri = "direct:start")
	ProducerTemplate producer;

	@RolesAllowed({"pharol","ventas"})
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    @Path("/prueba")
    public Object getStock(@Context HttpHeaders headers, ConsultaStockRequest in_msg) {
		String roles[] = getRoles("getStock", new Class[] {HttpHeaders.class, ConsultaStockRequest.class});
		logger.info(String.format("getStock: in_msg: %s - %s", 
				in_msg.getClass().getSimpleName(), in_msg));
				
		boolean result = isValidRequest(headers, roles);
		if (result)
			return String.format("{ \"codigo\": \"%s\"}",result);
			//return producer.requestBody(in_msg, String.class);
		
		return getForbidden();
    }

}
