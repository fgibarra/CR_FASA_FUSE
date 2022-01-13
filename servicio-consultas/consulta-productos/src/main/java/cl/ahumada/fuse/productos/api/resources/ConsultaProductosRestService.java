package cl.ahumada.fuse.productos.api.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;

@Path("/")
public class ConsultaProductosRestService {

	@EndpointInject(uri = "direct:start")
	ProducerTemplate producer;

	Logger logger = Logger.getLogger(getClass());

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    @Path("/detalle/buscar")
    public String getProducto(ConsultaProductoRequest in_msg) {
		logger.info(String.format("getProducto: in_msg: %s - %s", 
				in_msg.getClass().getSimpleName(), in_msg));
        return producer.requestBody(in_msg, String.class);
    }
	
}
