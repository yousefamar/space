package utils;

public class Vec3D {

	public float x;
	public float y;
	public float z;

	public Vec3D(float x, float y, float z) {
		setVec(x, y, z);
	}

	public void setVec(Vec3D vec) {
		setVec(vec.x, vec.y, vec.z);
	}
	
	public void setVec(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec3D translate(Vec3D vec) {
		return this.translate(vec.x, vec.y, vec.z);
	}

	public Vec3D translate(float x, float y, float z) {
		this.setVec(this.x + x, this.y + y, this.z + z);
		return this;
	}
	
	public Vec3D rotateX(int angle) {
		float ca = FastMath.cos(angle);
		float sa = FastMath.sin(angle);
		this.setVec(x, ca*y+sa*z, ca*z-sa*y);
		return this;
	}

	public Vec3D rotateY(int angle) {
		float ca = FastMath.cos(angle);
		float sa = FastMath.sin(angle);
		this.setVec(ca*x-sa*z, y, sa*x+ca*z);
		return this;
	}

	public Vec3D rotateZ(int angle) {
		float ca = FastMath.cos(angle);
		float sa = FastMath.sin(angle);
		this.setVec(ca*x+sa*y, sa*x+ca*y, z);
		return this; 
	}

	public Vec3D rotateA(Vec3D axis, int angle) {
		// TODO: Understand the math to implement this.
		float ca = FastMath.cos(angle);
		float sa = FastMath.sin(angle);
		this.setVec(x, ca*y+sa*z, ca*z-sa*y);
		return this;
	}

	public Vec3D scale(float amount) {
		this.setVec(x*amount, y*amount, z*amount);
		return this;
	}
	
	public Vec3D scaleX(float amount) {
		this.setVec(x*amount, y, z);
		return this;
	}

	public Vec3D scaleY(float amount) {
		this.setVec(x, y*amount, z);
		return this;
	}
	
	public Vec3D scaleZ(float amount) {
		this.setVec(x, y, z*amount);
		return this;
	}
	
	public Vec3D normalise() {
		float mag = Geom.mag(this);
		this.setVec(x/mag, y/mag, z/mag);
		return this;
	}

	public Vec3D reset() {
		this.setVec(0, 0, 0);
		return this;
	}
	
	public String toString() {
		return "("+x+", "+y+", "+z+")";
	}
}