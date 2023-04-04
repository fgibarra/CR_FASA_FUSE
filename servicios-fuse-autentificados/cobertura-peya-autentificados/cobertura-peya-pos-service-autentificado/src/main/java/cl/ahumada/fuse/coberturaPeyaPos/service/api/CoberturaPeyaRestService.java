package cl.ahumada.fuse.coberturaPeyaPos.service.api;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.coberturaPeyaPos.lib.CoberturaPeyaRequest;
import cl.ahumada.fuse.coberturaPeyaPos.lib.CoberturaPeyaResponse;
import cl.ahumada.fuse.coberturaPeyaPos.service.dto.ActionsDTO;
import cl.ahumada.fuse.coberturaPeyaPos.service.utils.JacksonFunctions;

import javax.annotation.security.RolesAllowed;
import cl.ahumada.fuse.api.AutentificacionRestService;


/**
 * @author fernando
 * Expone la url: cxf/ESB/coberturaPeya/obtiene/cobertura
 * 
 * Deja preparado map en [header.resultados]
 * Deja en el body un xml con el xpath para hacer el split paralelo con los datos recibidos desde el cliente
 * 
 */
@Path("/obtiene")
public class CoberturaPeyaRestService extends AutentificacionRestService {
    @EndpointInject(uri="direct:start")
    ProducerTemplate producer;

    Logger logger = Logger.getLogger(getClass());

	@RolesAllowed({"pharol","ventas"})
    @POST
    @Path("/cobertura")
    @Consumes("application/json")
    @Produces("application/json; charset=UTF-8")
    public Object recuperaCobertura(@Context HttpHeaders headers, CoberturaPeyaRequest request) {
		String roles[] = getRoles("recuperaCobertura", new Class[] {HttpHeaders.class, CoberturaPeyaRequest.class});
        logger.info(String.format("recuperaCobertura: in_msg: %s", request));

        if(isValidRequest(headers, roles)) {
	        // crear request para google maps, oracle
	        Map<String, Object> mapGMaps = new HashMap<String, Object>();
	        Map<String, Object> mapOracle = new HashMap<String, Object>();
	        
	        mapGMaps.put("direccion", String.format("%s %s",request.getCalle(), request.getNumero()));
	        mapGMaps.put("comuna", request.getComuna());
	
	        mapOracle.put("farmacia", request.getFarmacias());
	
	        ActionsDTO apis = new ActionsDTO();
	        apis.addServicioOracle("Oracle", mapOracle);
	        apis.addServicioGMaps("GMaps", mapGMaps);
	        apis.addServicioToken("Token");
	        String xml = JacksonFunctions.getInstance().java2xml(apis);
	        
	        // preparar header para respuestas de actions paralelos
			Map<String,Object> responses = new HashMap<String,Object>();
	        
	        return  (CoberturaPeyaResponse)producer.requestBodyAndHeader(xml, "responses", responses);
        }
		return getForbidden();
        
    }
}