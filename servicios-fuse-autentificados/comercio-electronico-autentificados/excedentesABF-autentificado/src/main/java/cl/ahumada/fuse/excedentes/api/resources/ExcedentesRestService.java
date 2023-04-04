package cl.ahumada.fuse.excedentes.api.resources;

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

import cl.ahumada.fuse.excedentes.api.resources.json.A01Request;
import cl.ahumada.fuse.excedentes.api.resources.json.A01Response;
import cl.ahumada.fuse.excedentes.api.resources.json.C01Request;
import cl.ahumada.fuse.excedentes.api.resources.json.C01Response;
import cl.ahumada.fuse.excedentes.api.resources.json.C02Request;
import cl.ahumada.fuse.excedentes.api.resources.json.C02Response;
import javax.annotation.security.RolesAllowed;
import cl.ahumada.fuse.api.AutentificacionRestService;


@Path("/excedentes")
public class ExcedentesRestService extends AutentificacionRestService {

	@EndpointInject(uri = "direct:start")
	ProducerTemplate producer;

	Logger logger = Logger.getLogger(getClass());

	@RolesAllowed({"pharol","ventas"})
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    @Path("/autorizar")
    public Object getVenta(@Context HttpHeaders headers, A01Request in_msg) {
		String roles[] = getRoles("getVenta", new Class[] {HttpHeaders.class, A01Request.class});
		logger.info(String.format("getVenta: in_msg: %s - %s", 
				in_msg.getClass().getSimpleName(), in_msg));
		if(isValidRequest(headers, roles))
			return (A01Response) producer.requestBodyAndHeader(in_msg, "operacion", "autorizacion");
		
		return getForbidden();
    }

	@RolesAllowed({"pharol","ventas"})
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    @Path("/confirmar")
    public Object getConfirmacion(@Context HttpHeaders headers, C01Request in_msg) {
		String roles[] = getRoles("getConfirmacion", new Class[] {HttpHeaders.class, C01Request.class});
		logger.info(String.format("getConfirmacion: in_msg: %s - %s", 
				in_msg.getClass().getSimpleName(), in_msg));
		if(isValidRequest(headers, roles))
			return (C01Response) producer.requestBodyAndHeader(in_msg,"operacion", "confirmacion");
		
		return getForbidden();
    }

	@RolesAllowed({"pharol","ventas"})
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    @Path("/anular")
    public Object getAnulacion(@Context HttpHeaders headers, C02Request in_msg) {
		String roles[] = getRoles("getAnulacion", new Class[] {HttpHeaders.class, C02Request.class});
		logger.info(String.format("getAnulacion: in_msg: %s - %s", 
				in_msg.getClass().getSimpleName(), in_msg));
		if(isValidRequest(headers, roles))
			return (C02Response) producer.requestBodyAndHeader(in_msg, "operacion", "anulacion");
		
		return getForbidden();
    }
}
