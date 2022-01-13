package cl.ahumada.fuse.descuentos.api.resources.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cl.ahumada.fuse.utils.Constantes;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemReq implements Serializable {

	/**
	 *
	 */
	@JsonIgnore
	private static final long serialVersionUID = -9126538270757049620L;

	// para el request
	@JsonProperty("itemCode")
	public final String itemCode;
	public final Integer department;
	public final Boolean discountableFlag;
	@JsonProperty("quantitySold")
	public final Integer quantitySold;
	@JsonProperty("extendedPrice")
	public final Integer extendedPrice;
	@JsonProperty("extendedSecondPrice")
	public final Integer extendedSecondPrice;
	public final Integer familyCode1;
	public final Integer familyCode2;
	public final Integer familyCode3;
	public final Boolean itemVoid;
	public final Integer manufacturerCode;
	public final Boolean minimunOrderFlag;
	public final Integer mixMatchCode;
	public final Integer poolCode;
	public final Integer pricePerUnitWeight;
	@JsonProperty("structureCode")
	public final String structureCode;
	public final Integer weightSold;

	@JsonCreator
	public ItemReq(@JsonProperty("itemCode") String itemCode, @JsonProperty("quantitySold") Integer quantitySold,
			@JsonProperty("extendedPrice") Integer extendedPrice) {
		super();
		this.itemCode = itemCode.replace(".0", "");
		this.quantitySold = quantitySold;
		this.extendedPrice = extendedPrice;
		this.extendedSecondPrice = extendedPrice;
		String valor = Constantes.rellena(itemCode.replace(".0", ""), 10, '0');
		this.structureCode = String.format("%sffffffffffffffffffffffffffffffff00000000000000000000000000000000000000", valor);
		this.department = 1;
		this.discountableFlag = Boolean.FALSE;
		this.familyCode1 = 0;
		this.familyCode2 = 0;
		this.familyCode3 = 0;
		this.itemVoid = Boolean.FALSE;
		this.manufacturerCode = 0;
		this.minimunOrderFlag = Boolean.TRUE;
		this.mixMatchCode = 0;
		this.poolCode = 0;
		this.pricePerUnitWeight = 0;
		this.weightSold = 0;
	}

	public String getItemCode() {
		return itemCode;
	}

	public Integer getQuantitySold() {
		return quantitySold;
	}

	public Integer getExtendedPrice() {
		return extendedPrice;
	}

	public Integer getExtendedSecondPrice() {
		return extendedSecondPrice;
	}

	public String getStructureCode() {
		return structureCode;
	}

}
