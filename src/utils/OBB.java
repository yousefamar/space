package utils;

public class OBB extends CollisionBoundary {

	private Vec3D[] vertices = new Vec3D[8];
	private Plane3D[] faces = new Plane3D[6];
	private int yaw = 0, pitch = 0, roll = 0;
	public Vec3D centre = new Vec3D(0, 0, 0);
	public float halfWidth, halfHeight, halfDepth;
	public Vec3D unitX, unitY, unitZ;
	public float eventHorizonRadius;
	
	public OBB(float width, float height, float depth) {
		this(0, 0, 0, width, height, depth, 0, 0, 0);
	}
	
	public OBB(Vec3D centre, float width, float height, float depth, int yaw, int pitch, int roll) {
		this(centre.x, centre.y, centre.z, width, height, depth, yaw, pitch, roll);
	}
	
	public OBB(float x, float y, float z, float width, float height, float depth, int yaw, int pitch, int roll) {
		this.halfWidth = width/2;
		this.halfHeight = height/2;
		this.halfDepth = depth/2;
		this.eventHorizonRadius = Geom.mag(new Vec3D(halfWidth, halfHeight, halfDepth));
		
		/* Vertices mapped in conventional OpenGL order: x,y,z starting bottom left and going CCW. */
		vertices[0] = new Vec3D(x-halfWidth, y-halfHeight, z-halfDepth);
		vertices[1] = new Vec3D(x+halfWidth, y-halfHeight, z-halfDepth);
		vertices[2] = new Vec3D(x+halfWidth, y+halfHeight, z-halfDepth);
		vertices[3] = new Vec3D(x-halfWidth, y+halfHeight, z-halfDepth);
		vertices[4] = new Vec3D(x-halfWidth, y-halfHeight, z+halfDepth);
		vertices[5] = new Vec3D(x+halfWidth, y-halfHeight, z+halfDepth);
		vertices[6] = new Vec3D(x+halfWidth, y+halfHeight, z+halfDepth);
		vertices[7] = new Vec3D(x-halfWidth, y+halfHeight, z+halfDepth);

		faces[0] = new Plane3D(vertices[0], vertices[1], vertices[2]); //front
		faces[1] = new Plane3D(vertices[5], vertices[4], vertices[7]); //back
		faces[2] = new Plane3D(vertices[3], vertices[2], vertices[6]); //top
		faces[3] = new Plane3D(vertices[4], vertices[5], vertices[1]); //bottom
		faces[4] = new Plane3D(vertices[1], vertices[5], vertices[6]); //right
		faces[5] = new Plane3D(vertices[4], vertices[0], vertices[3]); //left
		
		this.updatePosition(x, y, z);
		this.updateRotation(yaw, pitch, roll);
	}
	
	public void updatePosition(Vec3D vec) {
		updatePosition(vec.x, vec.y, vec.z);
	}
	
	public void updatePosition(float x, float y, float z) {
		for (Vec3D vertex : vertices)
			vertex.translate(Geom.dir(centre, new Vec3D(x, y, z)));
		this.centre.setVec(x, y, z);
		unitX = Geom.unit(Geom.dir(vertices[1], vertices[0]));
		unitY = Geom.unit(Geom.dir(vertices[1], vertices[2]));
		unitZ = Geom.unit(Geom.dir(vertices[1], vertices[5]));
	}
	
	public void updateRotation(int yaw, int pitch, int roll) {
		for (Vec3D vertex : vertices)
			vertex.translate(Geom.scale(centre, -1)).rotateY(-this.yaw).rotateX(-this.pitch).rotateZ(-this.roll).rotateZ(roll).rotateX(pitch).rotateY(yaw).translate(centre);
		this.yaw = yaw;
		this.pitch = pitch;
		this.roll = roll;
		unitX = Geom.unit(Geom.dir(vertices[1], vertices[0]));
		unitY = Geom.unit(Geom.dir(vertices[1], vertices[2]));
		unitZ = Geom.unit(Geom.dir(vertices[1], vertices[5]));
	}
	
	private boolean containsPoint(Vec3D vec) {
		for (Plane3D face : faces)
			if (!Geom.isPointUnderOrOnPlane(vec, face))
				return false;
		return true;
	}
	
	//TODO: Consider allowing vertices to be public.
	public Vec3D[] getVertices() {
		return vertices;
	}
}