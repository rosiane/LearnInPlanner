package common;

import java.util.Random;

public class RandomUtils {
	private static Random random = new Random();

	public static int nextInt(int max) {
		return (int) (nextDouble() * max);
	}

	public static double nextDouble() {
		return random.nextDouble();
	}
}
