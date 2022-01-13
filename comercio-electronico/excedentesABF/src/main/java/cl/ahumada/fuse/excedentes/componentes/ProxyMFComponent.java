package cl.ahumada.fuse.excedentes.componentes;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.log4j.Logger;

public class ProxyMFComponent extends DefaultComponent {

	private Logger logger = Logger.getLogger(getClass());

	public ProxyMFComponent() {
		super();
		logger.info("ProxyMFComponent:Creating ProxyMF Camel Component");
	}

	public ProxyMFComponent(CamelContext context) {
		super(context);
		logger.info(String.format("ProxyMFComponent:Creating ProxyMF Camel Component : %s", context.getName()));
	}

	@Override
	protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
		logger.info("ProxyMFComponent: Creating ProxyMF Camel Endpoint");
        ProxyMFEndpoint packtEndpoint = new ProxyMFEndpoint(uri, this);
        setProperties(packtEndpoint, parameters);
        return packtEndpoint;
	}

}
