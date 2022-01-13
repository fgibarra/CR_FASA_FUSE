package cl.ahumada.fuse.coberturaPeyaPos.service.api;

import cl.ahumada.fuse.coberturaPeyaPos.lib.CoberturaPeyaRequest;
import cl.ahumada.fuse.coberturaPeyaPos.lib.CoberturaPeyaResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;



@Path("/obtiene")
public class CoberturaPeyaRestService {
    @EndpointInject(uri="direct:start")
    ProducerTemplate producer;

    Logger logger = Logger.getLogger(getClass());

    @POST
    @Path("/cobertura")
    @Consumes("application/json")
    @Produces("application/json; charset=UTF-8")
    public CoberturaPeyaResponse recuperaCobertura(CoberturaPeyaRequest request) {
        logger.info(String.format("recuperaCobertura: in_msg: %s", request));

        // test
        CoberturaPeyaResponse response = new CoberturaPeyaResponse(Integer.valueOf(0), "OK", new String[]{"0001","0072"});
        return response;
        
        //return producer.requestBody(request, CoberturaPeyaResponse.class);
    }
}
