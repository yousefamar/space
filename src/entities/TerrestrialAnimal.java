package entities;

import core.World;

public abstract class TerrestrialAnimal extends Animal implements Tangible {

	public TerrestrialAnimal(World world, float x, float y, float z, int yaw, int pitch,	int roll) {
		super(world, x, y, z, yaw, pitch, roll);
	}
	
	public void jump() {
		//TODO: Code jump.
	}
}
