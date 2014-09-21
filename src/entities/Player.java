package entities;

import utils.AABB;
import utils.CollisionBoundary;
import core.World;

public class Player extends TerrestrialAnimal{

	private AABB aabb;
	
	public Player(World world) {
		super(world, 0, 20, 0, 0, 0, 0);
		aabb = new AABB(position, 1, 3);
	}

	//TODO: This is unsafe; code in another sub-method for ticking.
	public void tick() {
		super.tick();
		aabb.setCentre(position);

		//if (position.y>10)
			//acceleration.translate(0, -0.2F, 0);
		// TODO: Collision based on chunk quadtrees.
		//if (onGround()) {
		//	setPosition(position.x, ((int)(position.y))+getHeight()/2F, position.z);
		//	acceleration.scaleY(0);
		//}
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
	
	public void jump() {
		if(onGround) {
			speed.translate(0, 0.5F, 0);
			onGround = false;
		}
	}

	public void decend() {
		speed.translate(0, -1F, 0);		
	}

	@Override
	public float getMass() {
		return 1;
	}

	@Override
	public CollisionBoundary getBoundingBox() {
		return aabb;
	}
}