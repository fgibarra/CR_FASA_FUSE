package cl.ahumada.fuse.utils;

import java.io.Reader;

import javax.xml.stream.XMLOutputFactory;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class JSonUtilities {

	private static JSonUtilities instance;
	protected Logger logger = Logger.getLogger(getClass());
	
	public static JSonUtilities getInstance() {
		if (instance == null) instance = new JSonUtilities();
		return instance;
	}

	public JSonUtilities() {
		super();
	}

	public Object json2java(String jsonString, Class<?> response) throws Exception {
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
		logger.debug("convertir Class ="+response);
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
	
	public String java2Xml(Object obj) {
		XmlMapper objectMapper = new XmlMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		try {
			java.io.ByteArrayOutputStream outStream = new java.io.ByteArrayOutputStream();
			XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
			xmlOutputFactory.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, new Boolean(true));
			objectMapper.writeValue(xmlOutputFactory.createXMLStreamWriter(outStream), obj);
			return outStream.toString();
		} catch (Exception e) {
			logger.error("java2Xml", e);
			return "No pudo convertir a xml";
		}
	}
}
