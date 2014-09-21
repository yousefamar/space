package utils;

public class FastMath {

	private static float sinBuffer[] = loadSinBuffer();
	private static float cosBuffer[] = loadCosBuffer();

	// TODO: Investigate modulus operator for negative numbers.
	
	public static float sin(int degree) {
		try {
			return sinBuffer[degree];
		} catch (ArrayIndexOutOfBoundsException e) {
			while (degree < 0)
				degree += 360;
			while (degree > 359)
				degree -= 360;
			return sinBuffer[degree];
		}
	}
	
	public static float cos(int degree) {
		try {
			return cosBuffer[degree];
		} catch (ArrayIndexOutOfBoundsException e) {
			while (degree < 0)
				degree += 360;
			while (degree > 359)
				degree -= 360;
			return cosBuffer[degree];
		}
	}

	private static float[] loadSinBuffer() {
		float sinBuffer[] = new float[360];
		for (int i = 0; i < sinBuffer.length; i++)
			sinBuffer[i] = (float) Math.sin(Math.toRadians(i));
		return sinBuffer;
	}

	private static float[] loadCosBuffer() {
		float cosBuffer[] = new float[360];
		for (int i = 0; i < cosBuffer.length; i++)
			cosBuffer[i] = (float) Math.cos(Math.toRadians(i));
		return cosBuffer;
	}
	
	public static float abs(float num) {
		if (num<0) num*=-1;
		return num;
	}

	public static int floor(float num) {
		return (int) num;
	}

	public static String mergeCoords(int x, int y, int z) {
		return x+""+y+""+z;
	}
}
