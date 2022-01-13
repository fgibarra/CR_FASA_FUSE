package cl.ahumada.fuse.pedidos.api.resources;

import java.io.IOException;
import java.lang.reflect.Type;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.util.GenericType;

public class MiInputPart implements InputPart {

	private Object body;
	private MultivaluedMap<String, String> headers;
	
	public MiInputPart(MiBodyPart miBodyPart) {
		headers = miBodyPart.getHeaders();
		body = miBodyPart.getBody();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBody(GenericType<T> arg0) throws IOException {
		return (T) body;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBody(Class<T> arg0, Type arg1) throws IOException {
		return (T) body;
	}

	@Override
	public String getBodyAsString() throws IOException {
		if (body != null) return body.toString();
		return null;
	}

	@Override
	public MultivaluedMap<String, String> getHeaders() {
		return headers;
	}

	@Override
	public MediaType getMediaType() {
		String mediaType = headers.getFirst("Content-Type");
		return new MediaType(mediaType, null);
	}

	@Override
	public boolean isContentTypeFromMessage() {
		if (headers.getFirst("Content-Type").contains("json"))
			return false;
		return true;
	}

}
