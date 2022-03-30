package cl.ahumada.peya.requester.main;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.pedidosya.reception.sdk.ApiClient;
import com.pedidosya.reception.sdk.exceptions.ApiException;
import com.pedidosya.reception.sdk.models.Address;
import com.pedidosya.reception.sdk.models.Attachment;
import com.pedidosya.reception.sdk.models.Detail;
import com.pedidosya.reception.sdk.models.Discount;
import com.pedidosya.reception.sdk.models.Order;
import com.pedidosya.reception.sdk.models.Payment;
import com.pedidosya.reception.sdk.models.Product;
import com.pedidosya.reception.sdk.models.Restaurant;
import com.pedidosya.reception.sdk.models.User;

import cl.ahumada.peya.requester.apirest.ReactiveFullDuplexServer;
import cl.ahumada.peya.requester.peyasdk.Requester;
import cl.ahumada.peya.requester.peyasdk.Token;
import cl.ahumada.peya.requester.peyasdk.threads.ConfirmaCallback;
import cl.ahumada.peya.requester.peyasdk.threads.ProcesaOrden;


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
    
	/**
	 * @param args
	 * @throws ApiException
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * 
	 * 1.- Confugura logger, lee configuracion de integracion.properties. Ambiente desarrollo o produccion desde 
	 * variable ambiental: peya.requester.ambiente.
	 * 
	 * 2.- Activa http server
	 * 3.- Activa la recepcion de ordenes.
	 * 
	 */
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
        
        // Inicializar el Http Server en un thread independiente
        ReactiveFullDuplexServer server = new ReactiveFullDuplexServer(integracionProps);
        getInstance().setHttpServer(server);
        server.start();
        
        // Inicializar el peya sdk
        getInstance().setToken(new Token());
        getInstance().setApiClient(new ApiClient(getInstance().getToken().getCentralizedConnection(propAmbiente)));
        getInstance().setRequester(new Requester (getInstance().getApiClient()));
        
        // Partir lectura de ordenes
        //getInstance().getRequester().getOrders();
        
        /* DEBUG */
        Order order = createDebugOrder();
        
		final ProcesaOrden procesaOrder = new ProcesaOrden(order);
		procesaOrder.procesa(new ConfirmaCallback() {
			@Override
			public void onConfirmaEnd(Map<String, Object> map, Integer confirma) {
				// Invocado despues que se recibe respuesta desde el monitor logistico
				// Si confirma == 0, ==> stock 0 para todos los productos de la orden
				// confirma == 1, ==> se puedeconfirmar la orden sin modificarla
				// confirma == 2, ==> hay que reconciliar la orden
				if (confirma == 0) {
					procesaOrder.rechazaOrden(map);
				} else if (confirma == 1) {
					procesaOrder.confirmaOrden(map);
				} else if (confirma == 2) {
					procesaOrder.reconciliaOrden(map);
				}
			}
		});
        /* */
	}

	private static Order createDebugOrder() {
		Order order = new Order();
        order.setId(1l);
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1l);
        restaurant.setIntegrationCode("001");
        order.setRestaurant(restaurant);
        List<Detail> details = new ArrayList<Detail>();
        Detail detail = new Detail();
        Product product1 = new Product();
        product1.setDescription("producto 1");
        product1.setId(1l);
        product1.setIntegrationCode("62051008");
        product1.setPrice(2000d);
        detail.setProduct(product1);
        detail.setQuantity(20);
        detail.setUnitPrice(100d);
        detail.setTotal(2000d);
        detail.setSubtotal(1000d);
        detail.setDiscount(10d);
        details.add(detail);
        order.setDetails(details);
        List<Discount> discounts = new ArrayList<Discount>();
        Discount discount = new Discount();
        discount.setCode("PROMOTION");
        discount.setAmount(12d);
        discounts.add(discount);
        order.setDiscounts(discounts);
        Payment payment = new Payment();
        order.setPayment(payment);
        payment.setShippingNoDiscount(100d);
        User user = new User();
        order.setUser(user);
        user.setName("Jorge Pedrero");
        user.setLastName("Inostrosa");
        user.setEmail("email@gmail.com");
        user.setIdentityCard("123456789");
        Address address = new Address();
        address.setPhone("999999999");
        address.setStreet("Pablo Picasso");
        address.setDoorNumber("1305");
        address.setArea("Huechuraba");
        address.setCity("RM");
        order.setAddress(address);
        order.setRegisteredDate(new Date());
        order.setDeliveryDate(new Date());
        
        List<Attachment> attachments = new ArrayList<Attachment>();
        order.setAttachments(attachments);
        Attachment attachment = new Attachment();
        attachments.add(attachment);
        attachment.setUrl("url to receta");
        
		return order;
	}
	/* */
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
