package utils;

public class Plane3D {

	public Vec3D point, normal;

	public Plane3D(Vec3D point, Vec3D normal) {
		this.point = point;
		this.normal = normal;
	}
	
	public Plane3D(Vec3D v1, Vec3D v2, Vec3D v3) {
		this.point = v2;
		this.normal = Geom.normal(v1, v2, v3);
	}
	
}
