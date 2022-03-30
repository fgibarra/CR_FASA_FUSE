package cl.ahumada.peya.requester.main;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.pedidosya.reception.sdk.ApiClient;
import com.pedidosya.reception.sdk.exceptions.ApiException;

import cl.ahumada.peya.requester.apirest.ReactiveFullDuplexServer;
import cl.ahumada.peya.requester.peyasdk.Requester;
import cl.ahumada.peya.requester.peyasdk.Token;


public class PeyaController {

    private static final String version = "2.0.0 (08-02-2022)";
    protected static String log4jConfigFile = "pedidosYa_log4j.properties";
    private static Logger logger = Logger.getLogger(PeyaController.class);
    
    static PeyaController instance = null;
    Token token = null;
    ApiClient apiClient = null;
    Requester requester = null;
    ReactiveFullDuplexServer httpServer = null;
    
    public static PeyaController getInstance() {
    	if (instance == null)
    		instance = new PeyaController();
    	return instance;
    }
    
	public static void main(String[] args) throws ApiException, IOException, InterruptedException, ExecutionException  {
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
        
        // Inicializar el Http Server
        getInstance().setHttpServer(new ReactiveFullDuplexServer(integracionProps));
        
        // Inicializar el peya sdk
        getInstance().setToken(new Token());
        getInstance().setApiClient(new ApiClient(getInstance().getToken().getCentralizedConnection(propAmbiente)));
        getInstance().setRequester(new Requester (getInstance().getApiClient()));
        
        // Partir lectura de ordenes
        getInstance().getRequester().getOrders();
	}

	//---------------------------------------------------------------------------------------------------------------
	// Getters y Setters
	//---------------------------------------------------------------------------------------------------------------
	
	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public ApiClient getApiClient() {
		return apiClient;
	}

	public void setApiClient(ApiClient apiClient) {
		this.apiClient = apiClient;
	}

	public Requester getRequester() {
		return requester;
	}

	public void setRequester(Requester requester) {
		this.requester = requester;
	}

	public ReactiveFullDuplexServer getHttpServer() {
		return httpServer;
	}

	public void setHttpServer(ReactiveFullDuplexServer httpServer) {
		this.httpServer = httpServer;
	}

}
