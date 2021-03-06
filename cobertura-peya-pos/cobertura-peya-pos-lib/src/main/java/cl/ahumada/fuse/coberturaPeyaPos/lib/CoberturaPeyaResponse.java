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
public class CoberturaPeyaResponse implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5808642008363515548L;
	@JsonProperty("codigo")
	Integer codigo;
	@JsonProperty("descripcion")
	String descripcion;
	@JsonProperty("farmacias")
	String[] farmacias;
    /**
     * @param codigo
     * @param descripcion
     * @param farmacias
     */
	@JsonCreator
    public CoberturaPeyaResponse(@JsonProperty("codigo")Integer codigo, 
                            @JsonProperty("descripcion")String descripcion, 
                            @JsonProperty("farmacias")String[] farmacias) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.farmacias = farmacias;
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
	
	@JsonIgnore
	public String getPipeResponse() {
		StringBuffer respuesta = new StringBuffer( String.format("%s|", codigo==0?"OK":"NOK"));
		if (codigo == 0)
			respuesta.append(String.format("%s", getPipeFarmacias()));
		else
			respuesta.append(String.format("%s|%s",getDescripcion(), getPipeFarmacias()));
		return respuesta.toString();
	}

	@JsonIgnore
	private String getPipeFarmacias() {
		StringBuffer sb = new StringBuffer();
		if (farmacias != null && farmacias.length > 0) {
			for (int i = 0; i<farmacias.length; i++) {
				if (i > 0) sb.append(',');
				sb.append(farmacias[i]);
			}
		}
		return sb.toString();
	}
	/**
     * @return the codigo
     */
    public Integer getCodigo() {
        return codigo;
    }
    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }
    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
