package planner.util;

import java.math.BigDecimal;

import javaff.planning.State;

public class LearnInPlannerState {

	private State state;

	public LearnInPlannerState(State state) {
		this.state = state;
	}

	public BigDecimal getHValue() {
		BigDecimal hValue = state.getHValue(); // RPL Heuristic
		// TODO implementar cálculo da heurística considerando a MLP
		return hValue;
	}

	public State getState() {
		return state;
	}
}
