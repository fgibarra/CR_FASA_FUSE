package cl.ahumada.ordenes.peya.main;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.pedidosya.reception.sdk.ApiClient;
import com.pedidosya.reception.sdk.exceptions.ApiException;

import cl.ahumada.ordenes.peya.module.Requester;
import cl.ahumada.ordenes.peya.module.Token;
import cl.ahumada.ordenes.peya.servicios.http.ReactiveFullDuplexServer;

public class PeyaController {

    private static final String version = "2.0.0 (26-01-2022)";
    protected static String log4jConfigFile = "pedidosYa_log4j.properties";
    private static Logger logger = Logger.getLogger(PeyaController.class);


    public static void main(String[] args) throws ApiException, IOException, InterruptedException, ExecutionException {
    	Properties properties = new Properties();
    	properties.load(PeyaController.class.getClassLoader().getResourceAsStream("pedidosYa_log4j.properties"));
    	PropertyConfigurator.configure(properties);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy-HH:mm:ss.SSS");
        logger.info("[" + sdf.format(new Date()) + "] PeyaController - " +
                    version + " is running");

		Properties integracionProps = new Properties();
		try (final java.io.InputStream stream =
				PeyaController.class.getClassLoader().getResourceAsStream("integracion.properties")){
					if (stream == null) throw new RuntimeException("stream es nulo");
					integracionProps.load(stream);
				}
    	String propAmbiente = System.getProperty("peya.requester.ambiente");
    	if (propAmbiente == null)
    		propAmbiente = integracionProps.getProperty("peya.requester.ambiente");
        logger.info(String.format("[ %s ] PeyaController - %s  is running a %s",
        		sdf.format(new Date()), version, propAmbiente));
        Token token = new Token();

        ApiClient apiClient = new ApiClient(token.getCentralizedConnection(propAmbiente));
        //////////////////////////////////////////////////////////////////////
        /*
        PartnerEvents partnerEvents = new PartnerEvents(apiClient);
        partnerEvents.getInitialization();
        partnerEvents.start();
        */
        //////////////////////////////////////////////////////////////////////

        //MantenedorStock mantenedorStock = new MantenedorStock(apiClient);
        //mantenedorStock.actualizaStock();

        //////////////////////////////////////////////////////////////////////
        ReactiveFullDuplexServer httpServer = new ReactiveFullDuplexServer(apiClient, integracionProps);
        httpServer.start();
        
        Requester requester = new Requester (apiClient);
        requester.start();
    }
}
