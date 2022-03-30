package cl.ahumada.fuse.requesterPeya.pedidosv4.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;

@Path("/detalle")
public class PedidosMockRestService {


	@EndpointInject(uri = "direct:start")
	ProducerTemplate producer;

	Logger logger = Logger.getLogger(getClass());

	@POST
	@Consumes(MediaType.APPLICATION_JSON+"; charset=UTF-8")
	@Produces(MediaType.APPLICATION_JSON+"; charset=UTF-8")
    @Path("/generar")

	public Response procesar(String in_msg) {
		logger.info(String.format("PedidosV4MockRestService.procesar: in_msg: %s - %s", 
				in_msg.getClass().getSimpleName(), in_msg));

		

		return Response.ok().build();
	}
}
