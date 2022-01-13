package cl.ahumada.fuse.descuentos.procesor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.PropertyInject;
import org.apache.log4j.Logger;

public class LeeArchivo implements Processor {

    @PropertyInject(value = "archivo", defaultValue="")
    private String filePath;
	private Logger logger = Logger.getLogger(getClass());

	@Override
	public void process(Exchange exchange) throws Exception {
		exchange.getIn().setBody(getArchivo());
	}

	private Object getArchivo() {
		logger.info(String.format("LeeArchivo.getArchivo: filePath=%s", filePath));
        String content = "";
        
        try
        {
            content = new String ( Files.readAllBytes( Paths.get(filePath) ) );
        } 
        catch (IOException e) 
        {
            logger.error("LeeArchivo.getArchivo", e);
        }
 
        return content;
	}

}
