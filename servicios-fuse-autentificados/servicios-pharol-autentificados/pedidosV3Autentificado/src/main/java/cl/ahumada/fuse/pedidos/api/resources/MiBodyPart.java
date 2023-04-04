package cl.ahumada.fuse.pedidos.api.resources;

import java.io.ByteArrayInputStream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public class MiBodyPart {
	MultivaluedMap<String, String> headers;
	Object body;
	String name;
	
	public MiBodyPart(String[] header, String data) {
		headers = new MultivaluedHashMap<String, String>();
		for (String hdr : header) {
			String split[] = hdr.split(":");
			headers.add(split[0], split[1].trim());
			if ("Content-Type".equals(split[0])) {
				if (split[1].contains(MediaType.APPLICATION_JSON))
					body = data;
				else
					body = new ByteArrayInputStream(data.getBytes());
			}
			else if ("Content-Disposition".equals(split[0])) {
				String valores[] = split[1].split(";");
				for (String v : valores) {
					if (v.trim().startsWith("name")) {
						String v2[] = v.split("=");
						name = v2[1].replace("\"", "");
						break;
					}
				}
				
			}
		}
		
	}

	public String getName() {
		return name;
	}

	public MultivaluedMap<String, String> getHeaders() {
		return headers;
	}

	public Object getBody() {
		return body;
	}

}
