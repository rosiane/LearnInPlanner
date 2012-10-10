package feature.selector.ga;

import java.io.IOException;

public interface FitnessFunction {

	public double evaluate(Chromosome chromosome) throws IOException;
}
