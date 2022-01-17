package cl.ahumada.fuse.coberturaPeyaPos.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

	public void addServicio(String id, Object nombre) {
		servicio.add(new Servicio(id, nombre));
	}
	
	public static class Servicio implements Serializable {
		/**
		 * 
		 */
		@JsonIgnore
		private static final long serialVersionUID = 5111761545485911319L;
		@JacksonXmlProperty(isAttribute = true)
		private String id;

		@JsonProperty("data")
		private Object data;
		
		@JsonCreator
		public Servicio(String id, Object data) {
			super();
			this.id = id;
			this.data = data;
		}
		
		public String getId() {
			return id;
		}


		public void setId(String id) {
			this.id = id;
		}

		public Object getData() {
			return data;
		}

		public void setData(Object data) {
			this.data = data;
		}


	}
}
