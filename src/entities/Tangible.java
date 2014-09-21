package entities;

import utils.CollisionBoundary;

public interface Tangible {

	/**
	 * @return Returns mass that affects elastic collisions and enables gravity.
	 * A mass of -1 denotes immobility.
	 */
	public float getMass();
	public CollisionBoundary getBoundingBox();
}