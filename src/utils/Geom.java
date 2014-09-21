package utils;

public class Geom {

	public static boolean isPointUnderOrOnPlane(Vec3D vec, Plane3D plane) {
		return dotPlane(vec, plane)<=0;
	}
	
	public static boolean isPointOnPlane(Vec3D vec, Plane3D plane) {
		return dotPlane(vec, plane)==0;
	}
	
	public static float dist(Vec3D vec1, Vec3D vec2) {
		return mag(dir(vec1, vec2));
	}
	
	public static float dist(Vec3D vec, Plane3D plane) {
		return FastMath.abs(dot(plane.normal, vec))/mag(plane.normal);
	}
	
	public static Vec3D porjPointPlane(Vec3D vec, Plane3D plane) {
		return sub(vec, scale(plane.normal, (dot(vec, plane.normal) - dot(plane.point, plane.normal)) / mag(plane.normal)));
	}

	public static Vec3D getShortestVectorPointPlane(Vec3D vec, Plane3D plane) {
		return dir(vec, unit(scale(plane.normal, -1)).scale(dist(vec, plane)));
	}
	
	/**
	 * Checks if a line segment intersects a plane.
	 * @param vec1 Start of line segment
	 * @param vec2 End of line segment
	 * @param plane Point on the plane
	 * @param n Normal of the plane
	 * @return True if the line segment intersects the plane, else false.
	 */
	public static boolean doesLineSegIntersectPlane(Vec3D vec1, Vec3D vec2, Plane3D plane) {
		float dot1 = dotPlane(vec1, plane), dot2 = dotPlane(vec2, plane);
		return ((dot1>0 && dot2<0)||(dot1<0 && dot2>0));
	}
	
	/**
	 * Returns the normal to a plane defined by three points.
	 * @param vec1
	 * @param vec2
	 * @param vec3
	 * @return Normal relative to v2->v1 and v2->v3.
	 */
	public static Vec3D normal(Vec3D vec1, Vec3D vec2, Vec3D vec3) {
		return cross(dir(vec2, vec1), dir(vec2, vec3));
	}
	
	public static Vec3D unit(Vec3D vec) {
		float mag = Geom.mag(vec);
		return new Vec3D(vec.x/mag, vec.y/mag, vec.z/mag);
	}

	public static Vec3D add(Vec3D vec1, Vec3D vec2) {
		return new Vec3D(vec1.x + vec2.x, vec1.y + vec2.y, vec1.z + vec2.z);
	}
	
	public static Vec3D sub(Vec3D vec1, Vec3D vec2) {
		return new Vec3D(vec1.x - vec2.x, vec1.y - vec2.y, vec1.z - vec2.z);
	}
	
	public static Vec3D dir(Vec3D vec1, Vec3D vec2) {
		return new Vec3D(vec2.x - vec1.x, vec2.y - vec1.y, vec2.z - vec1.z);
	}

	public static Vec3D scale(Vec3D vec, float amount) {
		return new Vec3D(vec.x*amount, vec.y*amount, vec.z*amount);
	}

	public static float dot(Vec3D vec1, Vec3D vec2) {
		return (vec1.x*vec2.x + vec1.y*vec2.y + vec1.z*vec2.z);
	}
	
	public static float mag(Vec3D vec) {
		return (float) Math.sqrt(vec.x*vec.x + vec.y*vec.y + vec.z*vec.z);
	}
	
	public static float mag(Vec2D vec) {
		return (float) Math.sqrt(vec.x*vec.x + vec.y*vec.y);
	}
	
	public static float angle(Vec3D vec1, Vec3D vec2) {
		return (float) Math.acos((dot(vec1, vec2)/(mag(vec1)*mag(vec2))));
	}
	
	/**
	 * Substitutes a point into the vector equation of a plane.
	 * @param vec Vector
	 * @param plane Point on the plane
	 * @param n Normal of plane
	 * @return 0 If the point is on the plane, else and a positive or negative number depending on which side the point is on.
	 */
	public static float dotPlane(Vec3D vec, Plane3D plane) {
		return dot(dir(plane.point,vec),plane.normal);
	}

	public static Vec3D cross(Vec3D v1, Vec3D v2) {
		return new Vec3D((v1.y*v2.z - v1.z*v2.y), (v1.z*v2.x - v1.x*v2.z), (v1.x*v2.y - v1.y*v2.x));
	}
}
