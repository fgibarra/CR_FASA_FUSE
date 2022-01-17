package cl.ahumada.fuse.coberturaPeyaPos.service.api;

import cl.ahumada.fuse.coberturaPeyaPos.lib.CoberturaPeyaRequest;
import cl.ahumada.fuse.coberturaPeyaPos.lib.CoberturaPeyaResponse;
import cl.ahumada.fuse.coberturaPeyaPos.service.dto.ActionsDTO;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.xml.stream.XMLOutputFactory;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;



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
        
        // crear request para google maps
        Map<String, Object> mapGMaps = new HashMap<String, Object>();
        mapGMaps.put("gmaps.direccion.cliente", String.format("%s %s",request.getCalle(), request.getNumero()));
        mapGMaps.put("gmaps.comuna.cliente", request.getComuna());
        Map<String, Object> mapOracle = new HashMap<String, Object>();
        mapOracle.put("sp.farmacias", request.getFarmacias());
        
        
        ActionsDTO apis = new ActionsDTO();
        apis.addServicio("Oracle", mapOracle);
        apis.addServicio("GMaps", mapGMaps);

        String xml = "";
		XmlMapper objectMapper = new XmlMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		try {
			java.io.ByteArrayOutputStream outStream = new java.io.ByteArrayOutputStream();
			XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
			xmlOutputFactory.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, new Boolean(true));
			objectMapper.writeValue(xmlOutputFactory.createXMLStreamWriter(outStream), apis);
			xml = outStream.toString();
		} catch (Exception e) {
			logger.error("java2Xml", e);
		}

        return  (CoberturaPeyaResponse)producer.requestBody(xml);
    }
}
