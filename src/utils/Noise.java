package utils;

public class Noise {
	
	public static double triLerpaDerp(int seed, double x, double y, double z){
		int xi = (int)x;
		int yi = (int)y;
		int zi = (int)z;
		double muX = x-xi;
		double muY = y-yi;
		double muZ = z-zi;
		return lerp(biLerp(smooth(seed, xi, yi, zi), smooth(seed, xi+1, yi, zi), smooth(seed, xi, yi+1, zi), smooth(seed, xi+1, yi+1, zi), muX, muY),
					biLerp(smooth(seed, xi, yi, zi+1), smooth(seed, xi+1, yi, zi+1), smooth(seed, xi, yi+1, zi+1), smooth(seed, xi+1, yi+1, zi+1), muX, muY), muZ);
	}
	
	public static double triLerp(double cs[][][], double muX, double muY, double muZ){
		return lerp(biLerp(cs[0][0][0], cs[1][0][0], cs[0][1][0], cs[1][1][0], muX, muY),
					biLerp(cs[0][0][1], cs[1][0][1], cs[0][1][1], cs[1][1][1], muX, muY), muZ);
	}
	
	public static double biLerp(double c00, double c10, double c01, double c11, double muX, double muY){
		return lerp(lerp(c00, c10, muX), lerp(c01, c11, muX), muY);
	}
	
	public static double lerp(double x0, double x1, double mu){
		return x0+(x1-x0)*mu;
	}
	
	public static double smooth(int seed, int x, int y, int z){
		double edges = (noise(seed,x-1,y-1,z)+noise(seed,x+1,y-1,z)+noise(seed,x-1,y+1,z)+noise(seed,x+1,y+1,z)
				+noise(seed,x,y-1,z-1)+noise(seed,x-1,y,z-1)+noise(seed,x+1,y,z-1)+noise(seed,x,y+1,z-1)
				+noise(seed,x,y-1,z+1)+noise(seed,x-1,y,z+1)+noise(seed,x+1,y,z+1)+noise(seed,x,y+1,z+1))/16;
		double corners = (noise(seed,x-1,y-1,z-1)+noise(seed,x+1,y-1,z-1)+noise(seed,x-1,y+1,z-1)+noise(seed,x+1,y+1,z-1)
				+noise(seed,x-1,y-1,z+1)+noise(seed,x+1,y-1,z+1)+noise(seed,x-1,y+1,z+1)+noise(seed,x+1,y+1,z+1))/32;
		double sides = (noise(seed,x,y-1,z)+noise(seed,x-1,y,z)+noise(seed,x+1,y,z)+noise(seed,x,y+1,z)+noise(seed,x,y,z-1)+noise(seed,x,y,z+1))/8;
		return edges + corners + sides + noise(seed,x,y,z)/4;
	}
	
	/**
	 * Generate a linearly congruent random number in the range [-1.0, 1.0) implicitly.
	 * Primes and generators taken from libnoise as the original large ones made patterns.
	 */
	public static double noise(int seed, int x, int y, int z){
		int n = (1619*x + 31337*y + 6971*z + 1013*seed)&0x7FFFFFFF;
		n = (n>>13)^n;
		return (((n*(n*n*60493+19990303)+1376312589)&0x7FFFFFFF)/1073741824.0) - 1;
	}
}
