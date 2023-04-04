package cl.ahumada.fuse.pedidos.api.resources;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Providers;

import org.apache.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

public class PedidosMessageProvider implements MessageBodyReader<MultipartFormDataInput> {

	@Context
	private Providers providers;
	Logger logger = Logger.getLogger(getClass());

	@Override
	public boolean isReadable(Class<?> clase, Type tipo, Annotation[] annotation, MediaType mediaType) {
		logger.info(String.format("PedidosMessageReader.isReadable: clase: %s tipo: %s mediaType: %s",
				clase, tipo, mediaType));
		return true;
	}

	@Override
	public MultipartFormDataInput readFrom(Class<MultipartFormDataInput> clase, Type tipo, Annotation[] annotation,
			MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream inputStream)
			throws IOException, WebApplicationException {
		logger.info(String.format("PedidosMessageReader.readFrom: clase: %s tipo: %s mediaType: %s",
				clase, tipo, mediaType));
		if (annotation != null) {
			StringBuffer sb = new StringBuffer(String.format("Annotation[%d]\n",annotation.length));
			for (Annotation key : annotation) {
				sb.append(String.format("%s", key)).append('\n');
			}
			logger.info(sb.toString());
		}

		MiMultipartFormDataInput mfdi = new MiMultipartFormDataInput(httpHeaders);
		mfdi.parse(inputStream);
		
		for (InputPart inputPart : mfdi.getParts())
			logger.info(String.format("PedidosMessageReader.readFrom: part_name: %s part_madiaType %s",
					inputPart.getHeaders().getFirst("name") ,inputPart.getMediaType()));
		
		return mfdi;
	}

}
