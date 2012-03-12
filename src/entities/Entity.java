package entities;

import gfx.Camera;

import java.util.ArrayList;

import utils.Vec3DF;

public class Entity {

	public static ArrayList<Entity> entityList = new ArrayList<Entity>();

	public float x;
	public float y;
	public float z;
	public int yaw;
	public int pitch;
	public int roll;

	public Entity() {
		this(0, 0, 0, 0, 0, 0);
	}

	public Entity(float x, float y, float z) {
		this(x, y, z, 0, 0, 0);
	}

	public Entity(float x, float y, float z, int yaw, int pitch, int roll) {
		setPosition(x, y, z);
		setRotation(yaw, pitch, roll);
		entityList.add(this);
	}

	public void setPosition(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void setRotation(int yaw, int pitch) {
		this.setRotation(yaw, pitch, 0);
	}

	public void setRotation(int yaw, int pitch, int roll) {
		while (yaw > 179)
			yaw -= 360;
		while (yaw < -180)
			yaw += 360;
		if (pitch > 89)
			pitch = 89;
		else if (pitch < -89)
			pitch = -89;
		while (roll > 179)
			roll -= 360;
		while (yaw < -180)
			roll += 360;
		this.yaw = yaw;
		this.pitch = pitch;
		this.roll = roll;
	}

	public void moveRel(Camera cam, float fwd, float strafe, float hover) {
		Vec3DF posVec = new Vec3DF(x, y, z);
		if (fwd != 0) {
			this.setPosition(posVec.translate((cam.lookVec.x-x)*fwd,(cam.lookVec.y-y)*fwd,(cam.lookVec.z-z)*fwd));
		} if (strafe != 0) {
			double ry = Math.toRadians(yaw);
			this.setPosition((float)(x-Math.cos(ry)*strafe), y, (float)(z-Math.sin(ry)*strafe));
		} if (hover != 0)
			this.setPosition(posVec.translate(0,hover,0));
		//TODO: Take roll into account
	}

	private void setPosition(Vec3DF posVec) {
		this.setPosition(posVec.x, posVec.y, posVec.z);
	}

	public double getDistToEntity(Entity entity) {
		return Math.sqrt(Math.pow((entity.x - x), 2)
				+ Math.pow((entity.y - y), 2) + Math.pow((entity.z - z), 2));
	}
}