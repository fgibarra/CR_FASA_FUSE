package cl.ahumada.fuse.pedidos.api.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("/boleta")
public class PedidosRestService {

	@EndpointInject(uri = "direct:start")
	ProducerTemplate producer;

	Logger logger = Logger.getLogger(getClass());

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
    public String uploadFile(MultipartFormDataInput in_msg) {
		logger.info(String.format("getStock: in_msg: %s - %s", 
				in_msg.getClass().getSimpleName(), in_msg));
        return producer.requestBody(in_msg, String.class);
    }
	
}
