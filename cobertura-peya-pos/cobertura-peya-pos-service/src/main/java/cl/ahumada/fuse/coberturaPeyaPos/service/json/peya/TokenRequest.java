package cl.ahumada.fuse.coberturaPeyaPos.service.json.peya;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.coberturaPeyaPos.service.json.Imprimible;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenRequest extends Imprimible implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 6314667750042716019L;
	@JsonProperty("client_id")
	private String clientId;
	@JsonProperty("client_secret")
	private String clientSecret;
	@JsonProperty("grant_type")
	private String grantType;
	@JsonProperty("username")
	private String username;
	@JsonProperty("password")
	private String password;
	
	@JsonCreator
	public TokenRequest(@JsonProperty("client_id")String clientId, 
			@JsonProperty("client_secret")String clientSecret, 
			@JsonProperty("grant_type")String grantType, 
			@JsonProperty("username")String username, 
			@JsonProperty("password")String password) {
		super();
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.grantType = grantType;
		this.username = username;
		this.password = password;
	}

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
	

}
