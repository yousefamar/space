package entities;

import utils.*;
import core.World;

public class Box extends Entity implements Tangible {
	
	private AABB aabb;
	
	public Box(World world) {
		super(world, -4, 5, -4, 0, 0, 0);
		model = "cube";
		setRotation(0, 10);
		aabb = new AABB(position, 3, 3);
	}
	
	public void tick() {
		super.tick();
		aabb.setCentre(position);
	}

	@Override
	public float getMass() {
		return -1;
	}

	@Override
	public CollisionBoundary getBoundingBox() {
		return aabb;
	}
}
