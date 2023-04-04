package cl.ahumada.fuse.coberturaPeyaPos.service.procesor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.PropertyInject;
import org.apache.log4j.Logger;

import cl.ahumada.fuse.coberturaPeyaPos.service.json.peya.TokenRequest;

/**
 * @author fernando
 *
 * Deja en el body el json con los datos con que se pide el token
 */
public class PreparaTokenPeya implements Processor {

	private Logger logger = Logger.getLogger(getClass());
    @PropertyInject(value = "peyaToken.clientId", defaultValue="courier_farmacias_ahumada_cl")
    private String clientId;
    @PropertyInject(value = "peyaToken.clientSecret", defaultValue="!!Z3Yltsie")
    private String clientSecret;
    @PropertyInject(value = "peyaToken.grantType", defaultValue="password")
    private String grantType;
    @PropertyInject(value = "peyaToken.username", defaultValue="courierapi@ahumada.cl")
    private String username;
    @PropertyInject(value = "peyaToken.password", defaultValue="FarmaciasAhumada$2021")
    private String password;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		TokenRequest request = new TokenRequest(getClientId(), getClientSecret(), getGrantType(), getUsername(), getPassword());
		exchange.getIn().setBody(request);
		logger.info(String.format("PreparaTokenPeya: request %s", request));
	}

}
