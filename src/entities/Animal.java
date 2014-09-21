package entities;

import core.World;

public abstract class Animal extends LivingEntity {

	public Animal(World world, float x, float y, float z, int yaw, int pitch, int roll) {
		super(world, x, y, z, yaw, pitch, roll);
	}

}
