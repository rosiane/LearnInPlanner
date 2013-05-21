package planner.heuristic.rpl;

import java.math.BigDecimal;

import planner.javaff.data.TotalOrderPlan;


public class Solution {
	private BigDecimal value;
	private TotalOrderPlan relaxedPlan;

	/**
	 * @return the value
	 */
	public BigDecimal getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(BigDecimal value) {
		this.value = value;
	}

	/**
	 * @return the relaxedPlan
	 */
	public TotalOrderPlan getRelaxedPlan() {
		return relaxedPlan;
	}

	/**
	 * @param relaxedPlan
	 *            the relaxedPlan to set
	 */
	public void setRelaxedPlan(TotalOrderPlan relaxedPlan) {
		this.relaxedPlan = relaxedPlan;
	}

}
