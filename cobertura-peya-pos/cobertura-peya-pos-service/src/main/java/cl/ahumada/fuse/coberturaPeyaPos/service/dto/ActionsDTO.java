package cl.ahumada.fuse.coberturaPeyaPos.service.dto;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ActionsDTO implements Serializable {

	/**
	 */
	@JsonIgnore
	private static final long serialVersionUID = -1885209424314682336L;
	
	@JacksonXmlElementWrapper(useWrapping = false /*localName = "servicio"*/)
	private List<Servicio> servicio = new ArrayList<Servicio>();

	public List<Servicio> getServicio() {
		return servicio;
	}

	public void setServicio(List<Servicio> servicio) {
		this.servicio = servicio;
	}

	
	public void addServicioOracle(String id, Map<String, Object> farmacias) {
		servicio.add(new Servicio(id, farmacias, null, null));
	}
	
	public void addServicioGMaps(String id, Map<String, Object> data) {
		Object comuna = data.get("comuna");
		Object direccion = data.get("direccion");
		servicio.add(new Servicio(id, null, comuna, direccion));
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Servicio implements Serializable {
		/**
		 * 
		 */
		@JsonIgnore
		private static final long serialVersionUID = 5111761545485911319L;
		@JacksonXmlProperty(isAttribute = true)
		private String id;

		@JacksonXmlProperty(localName = "farmacias")
		@JsonProperty("farmacia")
		private Object farmacia;
		
		@JsonProperty("comuna")
		private Object comuna;
		
		@JsonProperty("direccion")
		private Object direccion;
		
		public Servicio() {
			super();
		}

		@JsonCreator
		public Servicio(String id, Object farmacia, Object comuna, Object direccion) {
			super();
			this.id = id;
			this.farmacia = farmacia;
			this.comuna = comuna;
			this.direccion = direccion;
		}
		
		@Override
		public String toString() {
			try {
				StringWriter stringWriter = new StringWriter();
				JAXBContext jaxbContext = JAXBContext.newInstance(getClass());
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				jaxbMarshaller.marshal(this,stringWriter);
				return stringWriter.toString();
			} catch (Exception e) {
				return "No pudo convertir a XML";
			}
			
		}
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public Object getFarmacia() {
			return farmacia;
		}

		public void setFarmacia(Object farmacia) {
			this.farmacia = farmacia;
		}

		public Object getComuna() {
			return comuna;
		}

		public void setComuna(Object comuna) {
			this.comuna = comuna;
		}

		public Object getDireccion() {
			return direccion;
		}

		public void setDireccion(Object direccion) {
			this.direccion = direccion;
		}

	}
}
