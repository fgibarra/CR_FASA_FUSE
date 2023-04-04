package cl.ahumada.fuse.stock.api.resources.json;

import java.io.Serializable;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Local implements Serializable, Comparable<Local>  {

	/**
	 *
	 */
	@JsonIgnore
	private static final long serialVersionUID = 1L;

	@JsonProperty("numero_local")
	public long numeroLocal;
	@JsonProperty("stock")
	public Stock stock[];

	private Integer indiceCumplimiento;

	@JsonCreator
	public Local(@JsonProperty("numero_local")Long numeroLocal, @JsonProperty("stock")Stock[] stock) {
		this.numeroLocal = numeroLocal;
		this.stock = stock;
		this.indiceCumplimiento = 0;
	}

	@JsonIgnore
	public void addStock(Stock newStock) {
		Stock newstock[] = Arrays.copyOf(stock, stock.length+1);
		newstock[newstock.length - 1] = newStock;
		stock = newstock;
	}

	@JsonIgnore
	public Integer getIndiceCumplimiento() {
		return indiceCumplimiento;
	}

	@JsonIgnore
	public void add() {
		add(1);
	}

	@JsonIgnore
	public void add(int i) {
		indiceCumplimiento = Integer.valueOf(indiceCumplimiento.intValue()+i);
	}

	@JsonIgnore
	public int compareTo(Local o) {
		// para mayor a menor
		return o.getIndiceCumplimiento() - this.getIndiceCumplimiento();
	}
}
