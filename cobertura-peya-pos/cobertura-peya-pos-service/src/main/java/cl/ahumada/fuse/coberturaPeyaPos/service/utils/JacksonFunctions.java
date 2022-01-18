package cl.ahumada.fuse.coberturaPeyaPos.service.utils;

import java.io.Reader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLOutputFactory;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import cl.ahumada.fuse.coberturaPeyaPos.service.dto.ActionsDTO;
import cl.ahumada.fuse.coberturaPeyaPos.service.dto.ActionsDTO.Servicio;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class JacksonFunctions {
	
	protected Logger logger = Logger.getLogger(getClass());
	
	public Object json2java(String jsonString, Class<?> response) throws Exception {
		logger.info("convertir a Class ="+response);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);

		try {
			return mapper.readValue(jsonString, response);
		} catch (Exception e) {
			logger.error("json2java: json\n"+jsonString);
			throw e;
		}
	}

	public Object json2java(String jsonString, Class<?> response, boolean UNWRAP_ROOT_VALUE) throws Exception {
		//logger.debug(jsonString.substring(0, jsonString.length()>500?500:jsonString.length()));
		logger.info("convertir Class ="+response);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, UNWRAP_ROOT_VALUE);

		try {
			return mapper.readValue(jsonString, response);
		} catch (Exception e) {
			logger.error(String.format("json2java:ERROR al convertir a clase %s de json\n%s",response,jsonString), e);
			throw e;
		}
	}

	public Object json2java(Reader rd, Class<?>response) throws Exception {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(rd, response);
		} catch (Exception e) {
			logger.error("json2java", e);
			throw e;
		}
	}

	public String java2json (Object data) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		return mapper.writeValueAsString(data);
	}
	
	@SuppressWarnings("deprecation")
	public String java2xml(Object data) {
		XmlMapper objectMapper = new XmlMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		try {
			java.io.ByteArrayOutputStream outStream = new java.io.ByteArrayOutputStream();
			XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
			xmlOutputFactory.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, new Boolean(true));
			objectMapper.writeValue(xmlOutputFactory.createXMLStreamWriter(outStream), data);
			return outStream.toString();
		} catch (Exception e) {
			logger.error("java2Xml", e);
		}
		return null;
	}
	/*
	 * esto no funciona !!!!!
	 */
	public Object xml2Java(String xml, Class<?> response) throws Exception {
		if (xml == null || xml.isEmpty())
			return xml;
		
		XmlMapper xmlMapper = new XmlMapper();
		Object obj = null;
		try {
			obj = xmlMapper.readValue(xml.getBytes(), response);
		} catch (Exception e) {
			logger.error(String.format("xml2java: Error al convertir xml [%s]", xml), e);
			throw e;
		}
		return obj;
	}

	/*
	 * esto no funciona !!!!!
	 */
	public Servicio xml2java(Element nodo) {
		try {
			JAXBContext jc = JAXBContext.newInstance( "cl.ahumada.fuse.coberturaPeyaPos.service.dto" );
			Unmarshaller u = jc.createUnmarshaller();
			JAXBElement<ActionsDTO.Servicio> jaxb = u.unmarshal(nodo, ActionsDTO.Servicio.class);
			Servicio obj = jaxb.getValue();
			logger.info(String.format("dumpNodo: clase: %s [%s]", obj.getClass().getName(), obj.getId()));
			return obj;
			
		} catch (JAXBException e) {
			logger.error(String.format("xml2java: Error al convertir xml [%s]", nodo.getNodeName()), e);
			return null;
		}
	}
	
	
	public String dumpNodo(Element nodo) throws JAXBException {
		StringBuffer sb = new StringBuffer();
		sb.append(nodo.toString()).append('\n');
		sb.append("nodeName=").append(nodo.getNodeName());
		NamedNodeMap nnm = nodo.getAttributes();
		int j = nnm.getLength();
        for (int k = 0; k < j; k++) {
            Node nod = nnm.item(k);

            sb.append(" atributo:").append(nod.getNodeName()).append('=').append(nod.getNodeValue()).append('\n');
        }
		
		NodeList lista = nodo.getChildNodes();
		visitChildNodes(sb, lista);
		return sb.toString();
	}
	
	private void visitChildNodes(StringBuffer sb, NodeList lista) {
		for (int i = 0; i < lista.getLength(); i++) {
			Node child = lista.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				sb.append("nodeName=").append(child.getNodeName()).append(" valor:").append(child.getTextContent()).append('\n');
			}
			if (child.hasChildNodes()) {
				visitChildNodes(sb, child.getChildNodes());
			}
		}

	}
}
