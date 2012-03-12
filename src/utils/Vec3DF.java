package utils;

public class Vec3DF {

	public float x;
	public float y;
	public float z;

	public Vec3DF(float x, float y, float z) {
		setVec(x, y, z);
	}

	public void setVec(Vec3DF vec) {
		setVec(vec.x, vec.y, vec.z);
	}
	
	public void setVec(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec3DF translate(Vec3DF vec) {
		return this.translate(vec.x, vec.y, vec.z);
	}

	public Vec3DF translate(float x, float y, float z) {
		this.setVec(this.x + x, this.y + y, this.z + z);
		return this;
	}
	
	public Vec3DF rotateX(int angle) {
		float ra = (float) Math.toRadians(angle);
		float cx = (float) Math.cos(ra);
		float sx = (float) Math.sin(ra);
		this.setVec(x, cx*y+sx*z, cx*z-sx*y);
		return this;
	}

	public Vec3DF rotateY(int angle) {
		float ra = (float) Math.toRadians(angle);
		float cy = (float) Math.cos(ra);
		float sy = (float) Math.sin(ra);
		this.setVec(cy*x-sy*z, y, sy*x+cy*z);
		return this;
	}

	public Vec3DF rotateZ(int angle) {
		float ra = (float) Math.toRadians(angle);
		float cz = (float) Math.cos(ra);
		float sz = (float) Math.sin(ra);
		this.setVec(cz*x+sz*y, sz*x+cz*y, z);
		return this; 
	}

	public Vec3DF rotateA(Vec3DF axis, int angle) {
		float ra = (float) Math.toRadians(angle);
		float cx = (float) Math.cos(ra);
		float sx = (float) Math.sin(ra);
		this.setVec(x, cx*y+sx*z, cx*z-sx*y);
		return this;
	}

	public Vec3DF scale(float amount) {
		this.setVec(x*amount, y*amount, z*amount);
		return this;
	}
	
	public Vec3DF scaleX(float amount) {
		this.setVec(x*amount, y, z);
		return this;
	}

	public Vec3DF scaleY(float amount) {
		this.setVec(x, y*amount, z);
		return this;
	}
	
	public Vec3DF scaleZ(float amount) {
		this.setVec(x, y, z*amount);
		return this;
	}
}
