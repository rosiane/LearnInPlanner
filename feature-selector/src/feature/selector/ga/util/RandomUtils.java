package feature.selector.ga.util;

import java.util.Random;

public class RandomUtils {
	private static Random random = new Random();

	public static int nextInt(int max) {
		return random.nextInt() * max;
	}

}
