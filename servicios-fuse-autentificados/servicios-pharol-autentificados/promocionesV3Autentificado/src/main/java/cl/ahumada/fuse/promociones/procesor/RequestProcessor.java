package cl.ahumada.fuse.promociones.procesor;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;

/**
 * @author fernando
 *
 *	Prepara al Camel para que use el cxf:client
 */
public class RequestProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
        exchange.setPattern(ExchangePattern.InOut);
        Message inMessage = exchange.getIn();

        // set the operation name
        inMessage.setHeader(CxfConstants.OPERATION_NAME, "getCartEntry");
        // using the proxy client API
        inMessage.setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, Boolean.FALSE);

        
        inMessage.setHeader(Exchange.ACCEPT_CONTENT_TYPE, "*");
        inMessage.setHeader(Exchange.CONTENT_TYPE, "application/json");
        
        //inMessage.setBody(t);
	}

}
