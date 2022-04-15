package cl.ahumada.fuse.requesterPeya.lib.monitorLogistico;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemML implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8700525699106691882L;
	@JsonProperty("EAN1")
	public String ean1;
	@JsonProperty("EAN2")
	public String ean2;
	@JsonProperty("EAN3")
	public String ean3;
	@JsonProperty("EAN4")
	public String ean4;
	@JsonProperty("EAN5")
	public String ean5;
	@JsonProperty("fk_department")
	public String fkDepartment;
	@JsonProperty("material_sku")
	public String materialSku;
	@JsonProperty("material_name")
	public String materialName;
	@JsonProperty("image_url")
	public String imageUrl;
	@JsonProperty("order_quantity")
	public Long orderQuantity;
	@JsonProperty("fk_plant")
	public String fkPlant;
	@JsonProperty("fk_status")
	public String fkStatus;
	@JsonProperty("stock_availability")
	public Integer stock_availability;
	@JsonProperty("precio_unitario")
	public Long precioUnitario;
	@JsonProperty("descuento_total")
	public Long descuentoTotal;
	@JsonProperty("iva")
	public Long iva;
	@JsonProperty("tipo_felicitacion")
	public String tipoFelicitacion;
	@JsonProperty("mensaje_felicitacion")
	public String mensajeFelicitacion;
	@JsonProperty("descuentos")
	public DescuentoML descuentos[];

	@JsonCreator
	public ItemML(@JsonProperty("fk_plant")String fkPlant,
			@JsonProperty("EAN1")String ean1, 
			@JsonProperty("order_quantity")Long orderQuantity, 
			@JsonProperty("precio_unitario")Long precioUnitario) {
		super();
		this.ean1 = ean1;
		this.materialSku = ean1;
		this.orderQuantity = orderQuantity;
		this.precioUnitario = precioUnitario;
		this.ean2 = "";
		this.ean3 = "";
		this.ean4 = "";
		this.ean5 = "";
		this.fkDepartment = "0000";
		this.materialName = "RECETARIO MAGISTRAL ECOMMERCE";
		this.imageUrl = "";
		this.fkPlant = fkPlant;
		this.fkStatus = "0";
		this.stock_availability = 0;
		this.descuentoTotal = 0l;
		this.iva = 0l;
		this.tipoFelicitacion = "";
		this.mensajeFelicitacion = "";
	}

	public String getEan1() {
		return ean1;
	}

	public void setEan1(String ean1) {
		this.ean1 = ean1;
	}

	public String getEan2() {
		return ean2;
	}

	public void setEan2(String ean2) {
		this.ean2 = ean2;
	}

	public String getEan3() {
		return ean3;
	}

	public void setEan3(String ean3) {
		this.ean3 = ean3;
	}

	public String getEan4() {
		return ean4;
	}

	public void setEan4(String ean4) {
		this.ean4 = ean4;
	}

	public String getEan5() {
		return ean5;
	}

	public void setEan5(String ean5) {
		this.ean5 = ean5;
	}

	public String getFkDepartment() {
		return fkDepartment;
	}

	public void setFkDepartment(String fkDepartment) {
		this.fkDepartment = fkDepartment;
	}

	public String getMaterialSku() {
		return materialSku;
	}

	public void setMaterialSku(String materialSku) {
		this.materialSku = materialSku;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Long getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(Long orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public String getFkPlant() {
		return fkPlant;
	}

	public void setFkPlant(String fkPlant) {
		this.fkPlant = fkPlant;
	}

	public String getFkStatus() {
		return fkStatus;
	}

	public void setFkStatus(String fkStatus) {
		this.fkStatus = fkStatus;
	}

	public Integer getStock_availability() {
		return stock_availability;
	}

	public void setStock_availability(Integer stock_availability) {
		this.stock_availability = stock_availability;
	}

	public Long getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(Long precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public Long getDescuentoTotal() {
		return descuentoTotal;
	}

	public void setDescuentoTotal(Long descuentoTotal) {
		this.descuentoTotal = descuentoTotal;
	}

	public Long getIva() {
		return iva;
	}

	public void setIva(Long iva) {
		this.iva = iva;
	}

	public String getTipoFelicitacion() {
		return tipoFelicitacion;
	}

	public void setTipoFelicitacion(String tipoFelicitacion) {
		this.tipoFelicitacion = tipoFelicitacion;
	}

	public String getMensajeFelicitacion() {
		return mensajeFelicitacion;
	}

	public void setMensajeFelicitacion(String mensajeFelicitacion) {
		this.mensajeFelicitacion = mensajeFelicitacion;
	}

	public DescuentoML[] getDescuentos() {
		return descuentos;
	}

	public void setDescuentos(DescuentoML[] descuentos) {
		this.descuentos = descuentos;
	}

}
