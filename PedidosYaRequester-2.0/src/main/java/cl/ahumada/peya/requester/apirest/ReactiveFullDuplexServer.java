package cl.ahumada.peya.requester.apirest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.hc.core5.function.Supplier;
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

import cl.ahumada.peya.requester.servicios.ServiciosdeBus;

public class ReactiveFullDuplexServer extends Thread {

    Integer port;
    Integer timeout;
    Properties datosConsulta;
    IOReactorConfig config;
    HttpAsyncServer server;
    Future<ListenerEndpoint> future;
    ListenerEndpoint listenerEndpoint;

    Logger logger = Logger.getLogger(getClass());

	public ReactiveFullDuplexServer(Properties integracionProps) 
            throws InterruptedException, ExecutionException, IOException {
		super();
		this.datosConsulta = integracionProps;
        String portStr = System.getProperty("peya.apirest.port");
        if (portStr == null)
            portStr = integracionProps.getProperty("port", "8180");

        this.port = Integer.valueOf(portStr);
        this.timeout = Integer.valueOf(integracionProps.getProperty("timeOut", "15"));
	}
	
	
	public void run() {

        this.config = IOReactorConfig.custom().setSoTimeout(timeout, TimeUnit.SECONDS).setTcpNoDelay(true).build();

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

                }).register("/peyaRequester", new Supplier<AsyncServerExchangeHandler>() {
                    @Override
                    public AsyncServerExchangeHandler get() {
                        logger.info("reconoce path /peyaRequester");
                        
                        // invoca callback para informar a PEYA confirma/rechaza
                        callbackPeya();
                        
                        return new ImmediateResponseExchangeHandler(new BasicResponseProducer(
                                new BasicHttpResponse(200)));
                        /*
                         * nunca responde, falta algo para que suelte la respuesta 
                        /* import
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

                }).create();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // System.out.println("HTTP server shutting down");
                logger.info("Runtime.getRuntime().addShutdownHook-run: HTTP server shutting down");
                server.close(CloseMode.GRACEFUL);
            }
        });

        server.start();
        future = server.listen(new InetSocketAddress(port), URIScheme.HTTP);
        try {
			listenerEndpoint = future.get();
			// System.out.print("Listening on " + listenerEndpoint.getAddress());
			logger.info(String.format("Listening on %s", listenerEndpoint.getAddress()));
			server.awaitShutdown(TimeValue.ofDays(Long.MAX_VALUE));
		} catch (Exception e) {
			logger.error("ReactiveFullDuplexServer: start", e);
		}
	}

    protected void setParametrosURI(String requestUri) {
        logger.info(String.format("setParametrosURI: requestUri %s", requestUri));
        if (requestUri != null && requestUri.length() >= "/peyaRequester?".length()) {
            String data = requestUri.substring("/peyaRequester?".length());
            logger.info(String.format("setParametrosURI: data %s", data));
            data = data.replace('&', '\n');
            try {
                datosConsulta = new Properties();
                datosConsulta.load(new ByteArrayInputStream(data.getBytes()));
                logger.info(String.format("setParametrosURI: numeroOrden %s, resultado=%s", 
                		(String) datosConsulta.get(ServiciosdeBus.NUMERO_ORDEN),
                		(String) datosConsulta.getProperty(ServiciosdeBus.CONFIRMA)));
            } catch (IOException e) {
                logger.error(String.format("setParametrosURI: data: %s", data), e);
            }
        }

    }
    
    /**
     * resume el proceso de la orden
     */
    protected void callbackPeya() {
		// continua el proceso de la orden que esta respondiendo el monitor
        logger.info(String.format("callbackPeya: numeroOrden %s, resultado=%s", 
        		(String) datosConsulta.get(ServiciosdeBus.NUMERO_ORDEN),
        		(String) datosConsulta.getProperty(ServiciosdeBus.CONFIRMA)));
		ServiciosdeBus.callbackPeya(datosConsulta);
	}


}