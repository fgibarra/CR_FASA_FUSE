package cl.ahumada.fuse.excedentes.componentes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.camel.impl.DefaultExchange;
import org.apache.log4j.Logger;

public class ProxyMFConsumer extends DefaultConsumer {
	private Logger logger = Logger.getLogger(getClass());    
	private ServerSocket serverSocket;
	private String server;
	private Integer port;

	public ProxyMFConsumer(Endpoint endpoint, Processor processor) throws Exception {
		super(endpoint, processor);
		logger.info(String.format("ProxyMFConsumer:Creating ProxyMFConsumer getEndpointUri=%s", endpoint.getEndpointUri()));
		StringTokenizer st = new StringTokenizer(endpoint.getEndpointUri(), ":");
		st.nextToken();
		st.nextToken();
		this.server = st.nextToken();
		this.port = Integer.valueOf(st.nextToken());
        serverSocket = new ServerSocket(port);
        logger.info("ProxyMFConsumer:Creating ProxyMF Consumer ...");
	}

    @Override
    protected void doStart() throws Exception {
        logger.info("ProxyMFConsumer:Starting ProxyMF Consumer ...");
        new Thread(new AcceptThread()).start();
        super.doStart();
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();
        logger.info("ProxyMFConsumer:Stopping ProxyMF Consumer ...");
        if (serverSocket != null) {
            serverSocket.close();
        }
    }

    class AcceptThread implements Runnable {

        @Override
        public void run() {
            while (true) {
                // create the exchange
                Exchange exchange = new DefaultExchange(getEndpoint(), ExchangePattern.InOut);
                Socket clientSocket = null;
                try {
                    clientSocket = serverSocket.accept();
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String inputLine = in.readLine();
                    if (inputLine != null) {
                        logger.info(String.format("Get input line: %s", inputLine));
                        exchange.getIn().setBody(inputLine, String.class);
                        // send the exchange to the next processor
                        getProcessor().process(exchange);
                        // get out message
                        String response = exchange.getOut().getBody(String.class);
                        if (response == null) {
                            response = exchange.getIn().getBody(String.class);
                        }
                        if (response != null) {
                            out.println(response);
                        }
                    }
                } catch (Exception e) {
                    exchange.setException(e);
                } finally {
                    if (clientSocket != null) {
                        try {
                            clientSocket.close();
                        } catch (Exception e) {
                            // nothing to do
                        }
                    }
                }
            }
        }

    }
}
