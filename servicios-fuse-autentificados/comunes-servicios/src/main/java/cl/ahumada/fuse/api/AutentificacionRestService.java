package cl.ahumada.fuse.api;

import java.io.IOException;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.StringTokenizer;

import javax.annotation.security.RolesAllowed;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.ws.rs.core.HttpHeaders;

import org.apache.log4j.Logger;
import org.jboss.resteasy.util.Base64;
import javax.ws.rs.core.Response;


public class AutentificacionRestService {

	protected Logger logger = Logger.getLogger(getClass());

    //======================================================================================================
	protected String[] getRoles(String methodName, Class<?>[] parameterTypes) {
		String roles[] = null;
		try {
			Method m = getClass().getMethod(methodName, parameterTypes);
			RolesAllowed r = m.getAnnotation(RolesAllowed.class);
			roles = r.value();
			logger.info(String.format("roles.len=%d", roles.length));
		} catch (NoSuchMethodException | SecurityException e) {
			StringBuffer sb = new StringBuffer();
			for (Class<?> clazz : parameterTypes)
				sb.append(clazz.getCanonicalName()).append(',');
			if (sb.length()>0)
				sb.setLength(sb.length()-1);
			logger.error(String.format("getRoles: methodName=%s parameterTypes= %s",methodName, sb.toString()), e);
		}
		return roles;
	}
	
    //======================================================================================================
	protected boolean isValidRequest(HttpHeaders headers, String roles[]) {
		boolean result = false;
		
		String encodedUserPassword = headers.getHeaderString("Authorization");
		logger.info(String.format("encodedUserPassword: %s ", encodedUserPassword!=null?encodedUserPassword:"ES NULO"));
		if (encodedUserPassword == null) {
			// ACCESS DENIED
			logger.info("generar un ACCESS DENIED");
			return false;
		}
		// Decode username and password
		logger.info(String.format("encodedUserPassword recivida en header: %s ", encodedUserPassword));
		if (encodedUserPassword.startsWith("Basic "))
			encodedUserPassword = encodedUserPassword.substring("Basic ".length());
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
			
			result = action(username, password, roles);
		} catch (Exception e) {
			logger.error("en action", e);
		}
		
		return result;
	}

    //======================================================================================================
    protected boolean action(String username, String password, String[] roles) throws Exception {
    	logger.info(String.format("action: username: %s password: %s", username, password));
		StringBuffer sb = new StringBuffer();
		for (String x : roles)
			sb.append(x).append(',');
		logger.info(String.format("action roles in: %s len=%d", sb.toString(), roles.length));
    	
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
        sb = new StringBuffer();
        for (Principal p : subject.getPrincipals()) {
        	sb.append(p.getName()).append(',');
        }
    	logger.info(String.format("action: roles en karaf: %s", sb.toString()));
    	
        if (loginContext.getSubject() != null) {
        	for (String role : roles) {
            	logger.info(String.format("action: role in: %s", role));
        		if (sb.toString().contains(role))
        			return true;
        	}
        }
        return false;
    }

    //======================================================================================================
	protected Response getForbidden() {
		Response response = Response.serverError().status(403).build();
		return response;
	}
	
}
