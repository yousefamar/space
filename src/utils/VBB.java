package utils;

public class VBB extends CollisionBoundary{

	private Vec3D[] vertices = new Vec3D[8];
	
	public VBB(Vec3D[] vertices) {
		this.vertices = vertices;
	}
}
