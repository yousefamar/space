package utils;

public class Vec2D {

	public float x;
	public float y;

	public Vec2D(float x, float y) {
		setVec(x, y);
	}

	public void setVec(Vec2D vec) {
		setVec(vec.x, vec.y);
	}
	
	public void setVec(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vec2D translate(Vec2D vec) {
		return this.translate(vec.x, vec.y);
	}

	public Vec2D translate(float x, float y) {
		this.setVec(this.x + x, this.y + y);
		return this;
	}
	
	public Vec2D rotateZ(int angle) {
		float ca = FastMath.cos(angle);
		float sa = FastMath.sin(angle);
		this.setVec(ca*x+sa*y, sa*x+ca*y);
		return this; 
	}

	public Vec2D rotate(Vec2D point, int angle) {
		// TODO: Code this.
		return this;
	}

	public Vec2D scale(float amount) {
		this.setVec(x*amount, y*amount);
		return this;
	}
	
	public Vec2D scaleX(float amount) {
		this.setVec(x*amount, y);
		return this;
	}

	public Vec2D scaleY(float amount) {
		this.setVec(x, y*amount);
		return this;
	}
	
	public Vec2D normalise() {
		float mag = Geom.mag(this);
		this.setVec(x/mag, y/mag);
		return this;
	}

	public Vec2D reset() {
		this.setVec(0, 0);
		return this;
	}
	
	public String toString() {
		return "("+x+", "+y+")";
	}
}
