package cl.ahumada.fuse.coberturaPeyaPos.service.procesor.aggregate;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.log4j.Logger;

public class SetAggregationStrategy implements AggregationStrategy {

	Logger logger = Logger.getLogger(getClass());

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        String body = newExchange.getIn().getBody(String.class);
        logger.info(String.format("SetAggregationStrategy:oldExchange %s NULO", 
        		(oldExchange == null?"":"NO")));
        if (oldExchange == null) {
            newExchange.getIn().setHeader("aggregate", Integer.valueOf(1));
            logger.info(String.format("SetAggregationStrategy:header aggregate: %s CamelSplitIndex=%d body [%s]", 
            		newExchange.getIn().getHeader("aggregate"),
            		newExchange.getProperty("CamelSplitIndex", Integer.class),
            		body));
            return newExchange;
        } else {
        	oldExchange.getIn().setHeader("aggregate", Integer.valueOf(2));
        	body = oldExchange.getIn().getBody(String.class);
            logger.info(String.format("SetAggregationStrategy:header aggregate: %s CamelSplitIndex_old=%d CamelSplitIndex_new=%d body [%s]", 
            		oldExchange.getIn().getHeader("aggregate"),
            		oldExchange.getProperty("CamelSplitIndex", Integer.class),
            		newExchange.getProperty("CamelSplitIndex", Integer.class),
            		body));
            return oldExchange;
        }
	}

}
