package cl.ahumada.fuse.coberturaPeyaPos.lib;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;



@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoberturaPeyaRequest implements Serializable {

    /**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = -3092076061498354652L;
	@JsonProperty("idTransaccion")
	String idTransaccion;
    @JsonProperty("calle")
	String calle;
    @JsonProperty("numero")
	String numero;
    @JsonProperty("comuna")
	String comuna;
    @JsonProperty("farmacias")
	String farmacias[];

    /**
     * 
     */
    @JsonCreator
    public CoberturaPeyaRequest(@JsonProperty("idTransaccion")String idTransaccion,
    @JsonProperty("calle")String calle,
    @JsonProperty("numero")String numero,
    @JsonProperty("comuna")String comuna,
    @JsonProperty("farmacias")String farmacias[]) {
        this.idTransaccion = idTransaccion;
        this.calle = calle;
        this.numero = numero;
        this.comuna = comuna;
        this.farmacias = farmacias;
    }

    public CoberturaPeyaRequest(String data) {
        parsea(data);
    }
    
    public void parsea(String data) {
        if (data != null && data.length() > 0) {

        	StringTokenizer st = new StringTokenizer(data, "|");
        	if (st.hasMoreTokens())
        		this.idTransaccion = st.nextToken();
        	if (st.hasMoreTokens())
        		this.calle = st.nextToken();
        	if (st.hasMoreTokens())
        		this.numero = st.nextToken();
        	if (st.hasMoreTokens())
        		this.comuna = st.nextToken();
        	String valor = null;
        	if (st.hasMoreTokens())
        		valor = st.nextToken();
        	if (valor != null) {
        		List<String> f = new ArrayList<String>();
        		st = new StringTokenizer(valor, ",");
        		while (st.hasMoreTokens())
        			f.add(st.nextToken());
        		this.farmacias = f.toArray(new String[0]);
        	}
        }
    }

    public boolean isValidRequest() {
        if (calle == null || numero == null || comuna == null || farmacias == null) {
            return false;
        }
        return true;
    }
    
	@Override
	@JsonIgnore
	public String toString() {
		try {
			ObjectMapper mapper = new ObjectMapper();
		    mapper.setSerializationInclusion(Include.NON_NULL);
		    return mapper.writeValueAsString(this);
		} catch (Exception e) {
			return String.format("No pudo serializar %s", this.getClass().getSimpleName());
		}
	}

    /**
     * @return the idTransaccion
     */
    public String getIdTransaccion() {
        return idTransaccion;
    }

    /**
     * @param idTransaccion the idTransaccion to set
     */
    public void setIdTransaccion(String idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    /**
     * @return the calle
     */
    public String getCalle() {
        return calle;
    }

    /**
     * @param calle the calle to set
     */
    public void setCalle(String calle) {
        this.calle = calle;
    }

    /**
     * @return the numero
     */
    public String getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * @return the comuna
     */
    public String getComuna() {
        return comuna;
    }

    /**
     * @param comuna the comuna to set
     */
    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    /**
     * @return the farmacias
     */
    public String[] getFarmacias() {
        return farmacias;
    }

    /**
     * @param farmacias the farmacias to set
     */
    public void setFarmacias(String[] farmacias) {
        this.farmacias = farmacias;
    }
    
}
