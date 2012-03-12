package entities;

public class Player extends Entity{

	public Player() {
		super(0, 0, 0, 0, 0, 0);
	}

	public Player(float x, float y, float z, int yaw, int pitch, int roll) {
		this();
	}

	public Player(float x, float y, float z) {
		this();
	}
}
