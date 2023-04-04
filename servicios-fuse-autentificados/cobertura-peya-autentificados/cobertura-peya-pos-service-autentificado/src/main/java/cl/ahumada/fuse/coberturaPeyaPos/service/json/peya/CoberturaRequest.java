package cl.ahumada.fuse.coberturaPeyaPos.service.json.peya;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.coberturaPeyaPos.service.json.Imprimible;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoberturaRequest extends Imprimible implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 4096677004326196640L;
	@JsonProperty("referenceId")
	private final String referenceId = "Client Internal Reference";
	@JsonProperty("isTest")
	private final Boolean isTest = Boolean.TRUE;
	@JsonProperty("deliveryTime")
	private final String deliveryTime;
	@JsonProperty("notificationMail")
	private final String notificationMail = "test@ahumada.cl";
	@JsonProperty("volume")
	private final Float volume = 20.02f;
	@JsonProperty("weight")
	private final Float weight = 0.8f;
	@JsonProperty("items")
	private final Item[] items;
	@JsonProperty("waypoints")
	private Waypoints[] waypoints;
	
	@JsonCreator
	public CoberturaRequest(@JsonProperty("waypoints")Waypoints[] waypoints) {
		super();
		this.deliveryTime = initTime();
		this.items = initItems();
		this.waypoints = waypoints;
	}

	private String initTime() {
		String DEFAULT_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
		DateFormat DEFAULT_DATE_FORMATTER = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
		
		return String.format("%sZ", DEFAULT_DATE_FORMATTER.format(new java.util.Date()));
	}
	
	private Item[] initItems() {
		Item[] items = new Item[] { new Item() };
		return items;
	}

	public Waypoints[] getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(Waypoints[] waypoints) {
		this.waypoints = waypoints;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public Boolean getIsTest() {
		return isTest;
	}

	public String getDeliveryTime() {
		return deliveryTime;
	}

	public String getNotificationMail() {
		return notificationMail;
	}

	public Float getVolume() {
		return volume;
	}

	public Float getWeight() {
		return weight;
	}

	public Item[] getItems() {
		return items;
	}

}
