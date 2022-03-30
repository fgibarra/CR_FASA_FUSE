package cl.ahumada.fuse.requesterPeya.monitorLogistico.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.requesterPeya.lib.monitorLogistico.MonitorLogisticoRequest;

@Path("/")
public class MonitorLogisticoMockRestService {

	@EndpointInject(uri = "direct:start")
	ProducerTemplate producer;

	Logger logger = Logger.getLogger(getClass());

	@POST
	@Consumes(MediaType.APPLICATION_JSON+"; charset=UTF-8")
	@Produces(MediaType.APPLICATION_JSON+"; charset=UTF-8")
    @Path("/procesar")

	public Response procesar(MonitorLogisticoRequest in_msg) {
		logger.info(String.format("MonitorLogisticoMockRestService.procesar: in_msg: %s - %s", 
				in_msg != null ? in_msg.getClass().getSimpleName() : "NULO", in_msg));
		CamelContext camelContext = producer.getCamelContext();

		// partir el proceso batch
		ProducerTemplate procesoBatch = camelContext.createProducerTemplate();
		Exchange exchange = ExchangeBuilder.anExchange(camelContext)
				.withBody(in_msg).build();
		logger.info(String.format("MonitorLogisticoMockRestService.procesar: activa seda:espera body:%s", 
				exchange.getIn().getBody()));
		procesoBatch.asyncSend("seda:espera", exchange);
		

		return Response.ok().build();
	}
}
