package cl.ahumada.fuse.pedidos.api.resources;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

public class MiMultipartFormDataInput implements MultipartFormDataInput {

	Logger logger = Logger.getLogger(getClass());
	protected MultivaluedMap<String, String> httpHeaders;
	protected String boundary;
	protected Map<String, InputPart> formData;
	protected Map<String, List<InputPart>> formDataMap;
	protected List<InputPart> parts;
	
	public MiMultipartFormDataInput(MultivaluedMap<String, String> httpHeaders) {
		super();
		this.httpHeaders = httpHeaders;
		if (httpHeaders != null) {
			StringBuffer sb = new StringBuffer("httpHeaders\n");
			for (String key : httpHeaders.keySet()) {
				sb.append(String.format("%s=%s", key, httpHeaders.get(key))).append('\n');
			}
			logger.info(sb.toString());
			
			String valores[] = httpHeaders.get("content-type").get(0).split(";");
			String b[] = valores[1].split("=");
			this.boundary = b[1];
			logger.info(String.format("boundary=%s", boundary));
			
			formData = new HashMap<String, InputPart>();
			formDataMap = new HashMap<String, List<InputPart>>();
			parts = new ArrayList<InputPart>();
		}
		
	}

	private boolean recibioEOF = false;
	public void parse(InputStream inputStream) throws IOException {
		String buffer = new String(IOUtils.toByteArray(inputStream));
		//logger.info(String.format("parse: lee %d bytes\n%s", buffer.length(),buffer));
		BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buffer.getBytes())));
		String linea = br.readLine();
		//logger.info(String.format("1: |%s|%s|", linea, boundary));
		if (linea.length() < 1)
			throw new RuntimeException("No lee del byteArray");
		
		//logger.info("parse: parte con parseo");
		// primera linea despues del bundary es el header del inputPart
		// segunda es el content type
		// tercera es un espacio
		// resto hasta el siguiente boundary es el contenido
		InputPartDTO dto = null;
		recibioEOF = false;
		while ((dto=parseInputPart(br)) != null) {
			formData.put(dto.getName(), dto.getInputPart());
			parts.add(dto.getInputPart());
			
			List<InputPart> lista = formDataMap.get(dto.name);
			if (lista == null) {
				lista = new ArrayList<InputPart>();
				lista.add(dto.getInputPart());
				formDataMap.put(dto.getName(), lista);
			} else {
				lista.add(dto.getInputPart());
			}
		}
	}

	private InputPartDTO parseInputPart(BufferedReader br) throws IOException {
		if (recibioEOF)
			return null;
		String header = br.readLine();
		String contentType = br.readLine();
		if (contentType != null && contentType.length() > 1)
			br.readLine(); // skip
		else
			contentType = String.format("Content-Type: %s", MediaType.APPLICATION_JSON);
		
		// ahora viene lo bueno, leer hasta el boundary
		StringBuffer sb = new StringBuffer();
		boolean sigaLeyendo = true;
		int boundaryLength = boundary.length();
		do {
			int cc = br.read();
			if (cc != -1) {
				sb.append((char)cc);
				if (sb.length() > boundaryLength && sb.toString().endsWith(boundary)) {
					sb.setLength(sb.length() - boundaryLength);
					sigaLeyendo = false;
				}
			} else 
				sigaLeyendo = false;
		} while (sigaLeyendo);
		
		// leer el \n
		String eof = br.readLine();
		if ("--".equals(eof)) {
			recibioEOF = true;
			sb.setLength(sb.length()-2);
			//logger.info(String.format("MiMultipartFormDataInput: ultimo part:\n%s",sb.toString()));
		}
		
		// sb tiene el contentido
		
		MiBodyPart miBodyPart = new MiBodyPart(new String[] {header, contentType}, sb.toString());
		InputPart inputPart = new MiInputPart(miBodyPart);
		
		InputPartDTO dto = new InputPartDTO(miBodyPart.getName(), inputPart);
		return dto;
	}

	@Override
	public List<InputPart> getParts() {
		return parts;
	}

	@Override
	public Map<String, InputPart> getFormData() {
		return formData;
	}

	@Override
	public Map<String, List<InputPart>> getFormDataMap() {
		return formDataMap;
	}

	@Override
	public <T> T getFormDataPart(String key, org.jboss.resteasy.util.GenericType<T> arg1) throws IOException {
		return (T) formData.get(key);
	}

	@Override
	public String getPreamble() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getFormDataPart(String arg0, Class<T> arg1, Type arg2) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	private class InputPartDTO {
		String name;
		InputPart inputPart;
		
		public InputPartDTO(String name, InputPart inputPart) {
			super();
			this.name = name;
			this.inputPart = inputPart;
		}
		
		public String getName() {
			return name;
		}

		public InputPart getInputPart() {
			return inputPart;
		}
		
	}

}
