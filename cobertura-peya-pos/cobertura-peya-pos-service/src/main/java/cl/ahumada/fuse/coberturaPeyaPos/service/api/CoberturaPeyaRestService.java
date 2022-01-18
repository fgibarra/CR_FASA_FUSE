package cl.ahumada.fuse.coberturaPeyaPos.service.api;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.coberturaPeyaPos.lib.CoberturaPeyaRequest;
import cl.ahumada.fuse.coberturaPeyaPos.lib.CoberturaPeyaResponse;
import cl.ahumada.fuse.coberturaPeyaPos.service.dto.ActionsDTO;
import cl.ahumada.fuse.coberturaPeyaPos.service.utils.JacksonFunctions;



@Path("/obtiene")
public class CoberturaPeyaRestService extends JacksonFunctions {
    @EndpointInject(uri="direct:start")
    ProducerTemplate producer;

    Logger logger = Logger.getLogger(getClass());

    @POST
    @Path("/cobertura")
    @Consumes("application/json")
    @Produces("application/json; charset=UTF-8")
    public CoberturaPeyaResponse recuperaCobertura(CoberturaPeyaRequest request) {
        logger.info(String.format("recuperaCobertura: in_msg: %s", request));

        // crear request para google maps, oracle
        Map<String, Object> mapGMaps = new HashMap<String, Object>();
        Map<String, Object> mapOracle = new HashMap<String, Object>();
        
        mapGMaps.put("direccion", String.format("%s %s",request.getCalle(), request.getNumero()));
        mapGMaps.put("comuna", request.getComuna());

        mapOracle.put("farmacia", request.getFarmacias());

        ActionsDTO apis = new ActionsDTO();
        apis.addServicioOracle("Oracle", mapOracle);
        apis.addServicioGMaps("GMaps", mapGMaps);

        String xml = java2xml(apis);
        
        // preparar header para respuestas de actions paralelos
		Map<String,Object> responses = new HashMap<String,Object>();
        
        return  (CoberturaPeyaResponse)producer.requestBodyAndHeader(xml, "responses", responses);
    }
}
