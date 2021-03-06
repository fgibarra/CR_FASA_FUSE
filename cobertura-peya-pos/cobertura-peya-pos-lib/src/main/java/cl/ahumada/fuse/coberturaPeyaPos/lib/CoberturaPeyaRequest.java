package cl.ahumada.fuse.coberturaPeyaPos.lib;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;



@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoberturaPeyaRequest implements Serializable {

    /**
	 * 
	 */
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

            String partes[] = data.split("|");
            if (partes.length > 0)
                this.idTransaccion = partes[0];
    
            if (partes.length > 1)
                this.calle = partes[1];
    
            if (partes.length > 2)
                this.numero = partes[2];
    
            if (partes.length > 3)
                this.comuna = partes[3];
    
            if (partes.length > 4) {
                if (partes[4].indexOf(',') >= 0) {
                    this.farmacias =  partes[4].split(",");
                } else {
                    this.farmacias = new String[1];
                    this.farmacias[0] = partes[4];
                }
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
