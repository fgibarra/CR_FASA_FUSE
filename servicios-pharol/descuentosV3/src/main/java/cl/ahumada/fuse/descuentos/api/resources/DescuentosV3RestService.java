package cl.ahumada.fuse.descuentos.api.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;

@Path("/obtiene")
public class DescuentosV3RestService {

	@EndpointInject(uri = "direct:start")
	ProducerTemplate producer;

	Logger logger = Logger.getLogger(getClass());


	@POST
	@Path("/descuentos")
	@Consumes("application/json")
	@Produces("application/json; charset=UTF-8")
	public String generaDescuento(DescuentosRequest in_msg) {
		logger.info(String.format("generaDescuento: in_msg: %s - %s", 
				in_msg.getClass().getSimpleName(), in_msg));
        return producer.requestBody(in_msg, String.class);
	}

}
