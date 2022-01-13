package cl.ahumada.fuse.excedentes.api.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.excedentes.api.resources.json.A01Request;
import cl.ahumada.fuse.excedentes.api.resources.json.A01Response;
import cl.ahumada.fuse.excedentes.api.resources.json.C01Request;
import cl.ahumada.fuse.excedentes.api.resources.json.C01Response;
import cl.ahumada.fuse.excedentes.api.resources.json.C02Request;
import cl.ahumada.fuse.excedentes.api.resources.json.C02Response;


@Path("/excedentes")
public class ExcedentesRestService {

	@EndpointInject(uri = "direct:start")
	ProducerTemplate producer;

	Logger logger = Logger.getLogger(getClass());

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    @Path("/autorizar")
    public A01Response getVenta(A01Request in_msg) {
		logger.info(String.format("getVenta: in_msg: %s - %s", 
				in_msg.getClass().getSimpleName(), in_msg));
        return (A01Response) producer.requestBodyAndHeader(in_msg, "operacion", "autorizacion");
    }

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    @Path("/confirmar")
    public C01Response getConfirmacion(C01Request in_msg) {
		logger.info(String.format("getConfirmacion: in_msg: %s - %s", 
				in_msg.getClass().getSimpleName(), in_msg));
        return (C01Response) producer.requestBodyAndHeader(in_msg,"operacion", "confirmacion");
    }

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    @Path("/anular")
    public C02Response getAnulacion(C02Request in_msg) {
		logger.info(String.format("getAnulacion: in_msg: %s - %s", 
				in_msg.getClass().getSimpleName(), in_msg));
        return (C02Response) producer.requestBodyAndHeader(in_msg, "operacion", "anulacion");
    }
}
