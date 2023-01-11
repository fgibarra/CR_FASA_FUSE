package cl.ahumada.fuse.stock.api.resources;

/*
import java.io.IOException;
import java.security.Principal;
import java.util.StringTokenizer;

import javax.annotation.security.RolesAllowed;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import org.jboss.resteasy.util.Base64;
*/
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;

@Path("/")
public class ConsultaStockRestService {

	@EndpointInject(uri = "direct:start")
	ProducerTemplate producer;

	Logger logger = Logger.getLogger(getClass());

//	@RolesAllowed("pharol")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    @Path("/tienda/buscar")
	
    public Response getStock(@Context HttpHeaders headers, ConsultaStockRequest in_msg) {
		
		if (isValidRequest(headers)) {
			logger.info(String.format("getStock_v0.0.2: in_msg: %s - %s", 
					in_msg.getClass().getSimpleName(), in_msg));
			
			
	        return producer.requestBody(in_msg, Response.class);
        }
		return getForbidden();
    }

	private Response getForbidden() {
		Response response = Response.serverError().status(403).build();
		return response;
	}
	
	private boolean isValidRequest(HttpHeaders headers) {
		boolean result = false;
		/*
		String encodedUserPassword = headers.getHeaderString("Authorization");
		logger.info(String.format("encodedUserPassword: %s ", encodedUserPassword!=null?encodedUserPassword:"ES NULO"));
		if (encodedUserPassword == null) {
			// ACCESS DENIED
			logger.info("generar un ACCESS DENIED");
			return false;
		}
		// Decode username and password
		logger.info(String.format("encodedUserPassword: %s ", encodedUserPassword));
		String usernameAndPassword;
		try {
			usernameAndPassword = new String(Base64.decode(encodedUserPassword));
		} catch (IOException e) {
			logger.error(String.format("en decode: encodedUserPassword: %s",encodedUserPassword), e);
			return false; //SERVER_ERROR;
		}

		logger.info(String.format("usernameAndPassword: %s ", usernameAndPassword));
		// Split username and password tokens
		final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
		final String username = tokenizer.nextToken();
		final String password = tokenizer.nextToken();

		logger.info(String.format("username: %s password: %s", username, password));
		try {
			result = action(username, password);
		} catch (Exception e) {
			logger.error("en action", e);
		}
		*/
		return result;
	}
	/*
    private boolean action(String username, String password) throws Exception {
    	logger.info(String.format("action: username: %s password: %s", username, password));
        LoginContext loginContext = new LoginContext("karaf", callbacks -> {
           for (Callback callback : callbacks) {
               if (callback instanceof NameCallback) {
                   ((NameCallback) callback).setName(username);
               } else if (callback instanceof PasswordCallback) {
                   ((PasswordCallback) callback).setPassword(password.toCharArray());
               } else {
                   throw new UnsupportedCallbackException(callback);
               }
           }
        });
        loginContext.login();
        Subject subject = loginContext.getSubject();
        for (Principal p : subject.getPrincipals()) {
        	logger.info(String.format("getName: %s", p.getName()));
        }
        return (loginContext.getSubject() != null);
    }
    */
}
