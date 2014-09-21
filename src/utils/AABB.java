package utils;

public class AABB extends CollisionBoundary{

	public Vec3D centre;
	public float halfWidth, halfHeight;
	
	public AABB(Vec3D centre, float width, float height) {
		this.centre = centre;
		this.halfWidth = width/2;
		this.halfHeight = height/2;
	}

	public void setCentre(Vec3D centre) {
		this.centre.setVec(centre.x, centre.y, centre.z);
	}
}
