package entities;

import core.World;
import utils.*;

public abstract class Entity {

	protected World world;
	public Vec3D position = new Vec3D(0, 0, 0);
	public Vec3D speed = new Vec3D(0, 0, 0);
	public int yaw, pitch, roll;
	public boolean onGround;

	public String model;
	
	public Entity(World world) {
		this(world, 0, 0, 0, 0, 0, 0);
	}

	public Entity(World world, float x, float y, float z) {
		this(world, x, y, z, 0, 0, 0);
	}

	public Entity(World world, float x, float y, float z, int yaw, int pitch, int roll) {
		this.world = world;
		this.setPosition(x, y, z);
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
		this.setRotation(yaw, pitch, roll);
	}
	
	public void tick() {
		setPosition(Geom.add(position, speed));
	}

	public void setPosition(float x, float y, float z) {
		position.setVec(x, y, z);
	}

	public void setRotation(int yaw, int pitch) {
		this.setRotation(yaw, pitch, 0);
	}

	protected void setRotation(int yaw, int pitch, int roll) {
		while (yaw < 0) yaw += 360;
		while (yaw > 359) yaw -= 360;
		while (pitch < 0) pitch += 360;
		while (pitch > 359) pitch -= 360;
		while (roll < 0) roll += 360;
		while (roll > 359) roll -= 360;
		this.yaw = yaw;
		this.pitch = pitch;
		this.roll = roll;
	}

	public void moveRel(float fwd, float strafe) {
		//TODO: Come up with a more stable algorithm for this.
		//TODO: Think of a way of combining vectors.
		float cy = FastMath.cos(yaw);
		float sy = FastMath.sin(yaw);
		Vec3D surge = new Vec3D(-sy*fwd - cy*strafe, 0, cy*fwd - sy*strafe).normalise().scale(0.1F);//The scale depends on general friction.
		speed.translate(surge);
		//TODO: Take roll into account.
	}

	public void setPosition(Vec3D posVec) {
		this.setPosition(posVec.x, posVec.y, posVec.z);
	}

	public double getDistToEntity(Entity entity) {
		float dX = (entity.position.x - position.x), dY = (entity.position.y - position.y), dZ = (entity.position.z - position.z);
		return Math.sqrt(dX*dX + dY*dY + dZ*dZ);
	}
	
	public void onCollisionWithEntity(Entity entity) {
	}
}