package cl.ahumada.fuse.excedentes.componentes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultProducer;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.utils.Constantes;

public class ProxyMFProducer extends DefaultProducer implements Producer {

	private Logger logger = Logger.getLogger(getClass());
	private String server;
	private Integer port;
	
	public ProxyMFProducer(Endpoint endpoint) {
		super(endpoint);
		StringTokenizer st = new StringTokenizer(endpoint.getEndpointUri(), ":");
		st.nextToken();
		st.nextToken();
		this.server = st.nextToken();
		this.port = Integer.valueOf(st.nextToken());
		logger.info(String.format("ProxyMFProducer: Creating ProxyMF Producer getEndpointUri=%s server=%s port=%d",
				endpoint.getEndpointUri(), server, port));
	}

	@Override
	public void process(Exchange exchange) throws Exception {
        logger.info("ProxyMFProducer: Processing exchange");
        String input = exchange.getIn().getBody(String.class);
        logger.info(String.format("ProxyMFProducer: Get input[%s]:\n%s",input, Constantes.dumpHexadecimal(input.getBytes())));
        logger.info(String.format("ProxyMFProducer: Connecting to socket on %s:%d",server,port));
        Socket socket = new Socket(server, port);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.print(input);
        out.flush();
        String fromServer = in.readLine();
        logger.info(String.format("ProxyMFProducer: Get reply from server: %s", fromServer));
        logger.info("ProxyMFProducer: Populating the exchange");
        socket.close();
        exchange.getIn().setBody(fromServer, String.class);
	}

}
