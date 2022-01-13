package cl.ahumada.proxy.tcp.coberturaPeyaPos;

import cl.ahumada.fuse.coberturaPeyaPos.lib.CoberturaPeyaRequest;
import cl.ahumada.fuse.coberturaPeyaPos.lib.CoberturaPeyaResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import proxy.tcp.lib.threads.ProcesoRequestClienteImpl;
import proxy.tcp.lib.utils.JSonUtilities;
import proxy.tcp.lib.utils.Utils;

public class CoberturaPeyaApirest extends ProcesoRequestClienteImpl {

    @Override
    public byte[] getRespuesta(String data) {
        logger.debug(String.format("getRespuesta: data: %s", data!=null ? Utils.getInstance().dumpHexadecimal(data.getBytes()): "ES NULO"));
        if (data != null && data.length() > 0) {

			CoberturaPeyaRequest request = new CoberturaPeyaRequest(data);
			logger.debug(String.format("getRespuesta: request: %s", request));
	
			return request.isValidRequest() ? generaRespuestaCoberturaPeya(request) : "NOK|DATOS ENVIADOS INVALIDOS".getBytes();
		}
		
		return "NOK".getBytes();
    }
    

    private byte[] generaRespuestaCoberturaPeya(CoberturaPeyaRequest request) {
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(ProxyMain.getInstance().getEndpoint());
		postMethod.setRequestHeader("Content-type", "application/json");


		InputStream stream = new ByteArrayInputStream(request.toString().getBytes());
		postMethod.setRequestEntity(new InputStreamRequestEntity(stream));

		try {
			logger.debug(String.format("request para CoberturaPeya: request.getURI()=%s",
										(postMethod.getURI()!=null?postMethod.getURI().toString(): "NULA")));
			StringBuffer sb = new StringBuffer();
			int status = httpClient.executeMethod(postMethod);
			BufferedReader reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream(), postMethod.getResponseCharSet()));
			String line = reader.readLine();
			while (line != null) {
				logger.debug(line);
				sb.append(line);
				line = reader.readLine();
			}
			postMethod.releaseConnection();
			
			if (status >= 300) {
				logger.error(String.format("Status Code: %d Status Text: %s Staus Line: %s\n%s",postMethod.getStatusCode(), 
								postMethod.getStatusText(), postMethod.getStatusLine(), sb.toString()));

				return "NOK|PROBLEMA CONEXION SERVICIO FUSE".getBytes();
			}
			return generaRespuesta(sb.toString());
		} catch (Exception e) {
			logger.error("enviaSms",e);
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				;
			}
		}
		return null;
    }

	private byte[] generaRespuesta(String recibido) {
		logger.info(String.format("generaRespuesta: recibido desde fuse:\n%s", recibido));
		String jsonString = String.format ("{\"CoberturaPeyaResponse\": %s}", recibido);
		try {
			CoberturaPeyaResponse response = (CoberturaPeyaResponse)JSonUtilities.getInstance().json2java(jsonString, CoberturaPeyaResponse.class);
			logger.info(String.format("generaRespuesta: recibido de fuse:\n%s", response.toString()));
			return response.getPipeResponse().getBytes();
		} catch (Exception e) {
			logger.error("generaRespuesta", e);
		}

		return "NOK|DATOS DESDE FUSE INVALIDOS".getBytes();
	}
}
