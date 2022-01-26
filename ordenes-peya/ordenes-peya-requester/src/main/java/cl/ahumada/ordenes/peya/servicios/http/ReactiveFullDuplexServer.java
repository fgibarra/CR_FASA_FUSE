package cl.ahumada.ordenes.peya.servicios.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.hc.core5.function.Supplier;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpConnection;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.URIScheme;
import org.apache.hc.core5.http.impl.Http1StreamListener;
import org.apache.hc.core5.http.impl.bootstrap.AsyncServerBootstrap;
import org.apache.hc.core5.http.impl.bootstrap.HttpAsyncServer;
import org.apache.hc.core5.http.message.BasicHttpResponse;
import org.apache.hc.core5.http.message.RequestLine;
import org.apache.hc.core5.http.message.StatusLine;
import org.apache.hc.core5.http.nio.AsyncServerExchangeHandler;
import org.apache.hc.core5.http.nio.support.BasicResponseProducer;
import org.apache.hc.core5.http.nio.support.ImmediateResponseExchangeHandler;
import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.reactor.ListenerEndpoint;
import org.apache.hc.core5.util.TimeValue;
import org.apache.log4j.Logger;

import com.pedidosya.reception.sdk.ApiClient;

import cl.ahumada.ordenes.peya.servicios.http.json.StatusResponse;


public class ReactiveFullDuplexServer extends Thread {


    Integer port;
    Integer timeout;
    ApiClient apiClient;
    Properties datosConsulta;
    final IOReactorConfig config;
    HttpAsyncServer server;
    Future<ListenerEndpoint> future;
    ListenerEndpoint listenerEndpoint;

    Logger logger = Logger.getLogger(getClass());

    /**
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws IOException
     * 
     */
    public ReactiveFullDuplexServer(ApiClient apiClient, Properties integracionProps)
            throws InterruptedException, ExecutionException, IOException {
        this.apiClient = apiClient;
        String portStr = System.getProperty("peya.httpserver.port");
        if (portStr == null)
            portStr = integracionProps.getProperty("peya.httpserver.port", "8180");

        this.port = Integer.valueOf(portStr);
        this.timeout = Integer.valueOf(integracionProps.getProperty("peya.httpserver.timeOut", "15"));

        this.config = IOReactorConfig.custom().setSoTimeout(timeout, TimeUnit.SECONDS).setTcpNoDelay(true).build();
    }
    
    public void run() {
        this.server = AsyncServerBootstrap.bootstrap().setIOReactorConfig(config)
                .setStreamListener(new Http1StreamListener() {
                    @Override
                    public void onRequestHead(final HttpConnection connection, final HttpRequest request) {
                        // System.out.println(connection.getRemoteAddress() + " " + new
                        // RequestLine(request));
                        logger.info(String.format(
                                "onRequestHead: getRemoteAddress: %s RequestLine(request):%s request.getRequestUri(): %s",
                                connection.getRemoteAddress(), new RequestLine(request), request.getRequestUri()));
                        setParametrosURI(request.getRequestUri());
                    }

                    @Override
                    public void onResponseHead(final HttpConnection connection, final HttpResponse response) {
                        // System.out.println(connection.getRemoteAddress() + " " + new
                        // StatusLine(response));
                        logger.info(String.format("onResponseHead: getRemoteAddress: %s StatusLine(request):%s",
                                connection.getRemoteAddress(), new StatusLine(response)));
                    }

                    @Override
                    public void onExchangeComplete(final HttpConnection connection, final boolean keepAlive) {
                        if (keepAlive) {
                            System.out.println(
                                    connection.getRemoteAddress() + " exchange completed (connection kept alive)");
                            logger.info(String.format(
                                    "onExchangeComplete: getRemoteAddress: %s  exchange completed (connection kept alive)",
                                    connection.getRemoteAddress()));
                        } else {
                            System.out
                                    .println(connection.getRemoteAddress() + " exchange completed (connection closed)");
                            logger.info(String.format(
                                    "onExchangeComplete: getRemoteAddress: %s exchange completed (connection closed)",
                                    connection.getRemoteAddress()));
                        }
                    }

                }).register("/status", new Supplier<AsyncServerExchangeHandler>() {
                	
                    @Override
                    public AsyncServerExchangeHandler get() {
                        logger.info("reconoce path /status");
                        String json = getStatusResponse();
                        return new ImmediateResponseExchangeHandler(new BasicResponseProducer(
                                new BasicHttpResponse(200), json, ContentType.APPLICATION_JSON));
                        /*
                         * nunca responde, falta algo para que suelte la respuesta /* import
                         * org.apache.hc.core5.http.protocol.HttpContext; import
                         * org.apache.hc.core5.reactive.ReactiveRequestProcessor; import
                         * org.apache.hc.core5.reactive.ReactiveServerExchangeHandler; import
                         * org.reactivestreams.Publisher;
                         * 
                         * 
                         * return new ReactiveServerExchangeHandler(new ReactiveRequestProcessor() {
                         * 
                         * @Override public void processRequest( final HttpRequest request, final
                         * EntityDetails entityDetails, final ResponseChannel responseChannel, final
                         * HttpContext context, final Publisher<ByteBuffer> requestBody, final
                         * Callback<Publisher<ByteBuffer>> responseBodyFuture ) throws HttpException,
                         * IOException { if (new BasicHeader(HttpHeaders.EXPECT,
                         * HeaderElements.CONTINUE).equals(request.getHeader(HttpHeaders.EXPECT))) {
                         * responseChannel.sendInformation(new BasicHttpResponse(100), context);
                         * logger.info("processRequest: se cumple el if"); } StringEntity stringEntity =
                         * new StringEntity("{ \"codigo\": 0}", ContentType.APPLICATION_JSON);
                         * logger.info(String.format("processRequest: enviando response: %s",
                         * stringEntity));
                         * 
                         * responseChannel.sendResponse( new BasicHttpResponse(200), //new
                         * BasicEntityDetails(-1, ContentType.APPLICATION_OCTET_STREAM), stringEntity,
                         * context);
                         * 
                         * // Simply using the request publisher as the response publisher will // cause
                         * the server to echo the request body. responseBodyFuture.execute(requestBody);
                         * 
                         * } });
                         */
                    }

                }).register("/confirma", new Supplier<AsyncServerExchangeHandler>() {

					@Override
					public AsyncServerExchangeHandler get() {
                        String json = getConfirmaResponse();
                        return new ImmediateResponseExchangeHandler(new BasicResponseProducer(
                                new BasicHttpResponse(200), json, ContentType.APPLICATION_JSON));
					}
                	
                })
                .create();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // System.out.println("HTTP server shutting down");
                logger.info("Runtime.getRuntime().addShutdownHook-run: HTTP server shutting down");
                server.close(CloseMode.GRACEFUL);
            }
        });

        server.start();
        try {
			future = server.listen(new InetSocketAddress(port), URIScheme.HTTP);
			listenerEndpoint = future.get();
			// System.out.print("Listening on " + listenerEndpoint.getAddress());
			logger.info(String.format("Listening on %s", listenerEndpoint.getAddress()));
			server.awaitShutdown(TimeValue.ofDays(Long.MAX_VALUE));
		} catch (InterruptedException e) {
			logger.warn("", e);
		} catch (ExecutionException e) {
			logger.error("", e);
		}

    }

    protected void setParametrosURI(String requestUri) {
        logger.info(String.format("setParametrosURI: requestUri %s", requestUri));
        if (isRequestStatus(requestUri) || isRequestConfirma(requestUri)) {
            String data = null;
            String uries[] = {"/status?", "/confirma?"};
            for (int i=0; i<uries.length; i++) {
            	if (requestUri.indexOf(uries[i]) >= 0) {
            		data = requestUri.substring(uries[i].length());
            		logger.info(String.format("setParametrosURI: data [%s]", data));
            		break;
            	}
            }
            if (data !=null && !data.isEmpty()) {
            	data = data.replace('&', '\n');
	            try {
	                datosConsulta = new Properties();
	                datosConsulta.load(new ByteArrayInputStream(data.getBytes()));
	                for (Object key : datosConsulta.keySet()) {
	                	logger.info(String.format("setParametrosURI: key:[%s] data:[%s]",key, (String) datosConsulta.get(key)));
	                }
	            } catch (IOException e) {
	                logger.error(String.format("setParametrosURI: data: %s", data), e);
	            }
            }
        }

    }

    private boolean isRequestStatus(String requestUri) {
    	return requestUri != null && requestUri.indexOf("/status?") >= 0;
	}

	private boolean isRequestConfirma(String requestUri) {
    	return requestUri != null && requestUri.indexOf("/confirma?") >= 0;
	}

	protected String getStatusResponse() {
        Long numeroOrden = Long.valueOf((String) datosConsulta.get("orden"));
        logger.info(String.format("getJsonResponse: numeroOrden:%d", numeroOrden));

        String json = getStatusOrden(numeroOrden);//
        logger.info(String.format("getJsonResponse: json %s", json));
        return json;
    }

	protected String getConfirmaResponse() {
        Long numeroOrden = Long.valueOf((String) datosConsulta.get("orden"));
        Boolean confirma = Integer.valueOf((String) datosConsulta.get("confirma")) == 1;
        logger.info(String.format("getJsonResponse: numeroOrden:%d confirmar %b", numeroOrden, confirma));

        String json = confirmaOrden(numeroOrden, confirma);//
        logger.info(String.format("getJsonResponse: json %s", json));
        return json;
    }

    
    private String getStatusOrden(Long numeroOrden) {
        String message = "ERROR en SDK PEYA";
        logger.info(String.format("getStatusOrden: numeroOrden:%d", numeroOrden));
        
        StatusResponse statusResponse = new StatusResponse(numeroOrden, message);
        
        // TODO
        // determinar el estado y generar el message a responder
        
        return statusResponse.toString();
    }
    
    private String confirmaOrden(Long numeroOrden, Boolean confirma) {
    	// TODO
    	logger.debug(String.format("confirmaOrden: numeroOrden=%d confirma=%b", numeroOrden, confirma));
    	return "{ \"status\": 0, \"message\": \"OK\"}";
    }
}
