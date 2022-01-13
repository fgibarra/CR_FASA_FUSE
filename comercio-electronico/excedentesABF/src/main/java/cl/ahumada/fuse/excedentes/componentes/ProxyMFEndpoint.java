package cl.ahumada.fuse.excedentes.componentes;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.log4j.Logger;

public class ProxyMFEndpoint extends DefaultEndpoint {

	private Logger logger = Logger.getLogger(getClass());

	public ProxyMFEndpoint() {
		super();
		logger.info("ProxyMFEndpoint:Creating ProxyMFEndpoint ...");
	}

	public ProxyMFEndpoint(String endpointUri, Component component) {
		super(endpointUri, component);
		logger.info(String.format("ProxyMFEndpoint:Creating ProxyMFEndpoint %s", endpointUri));
	}

	@Override
	public Consumer createConsumer(Processor processor) throws Exception {
		return new ProxyMFConsumer(this, processor);
	}

	@Override
	public Producer createProducer() throws Exception {
		return new ProxyMFProducer(this);
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

}
