package gfx;

import utils.Vec3D;

public class Camera {

	public Vec3D eyeVec = new Vec3D(0F, 0F, 0F);
	public Vec3D lookVec = new Vec3D(0F, 0F, 0F);
	public Vec3D upVec = new Vec3D(0F, 1F, 0F);

	public void setVectors(float eyeX, float eyeY, float eyeZ, float lookX,
			float lookY, float lookZ, float upX, float upY, float upZ) {
		eyeVec.setVec(eyeX, eyeY, eyeZ);
		lookVec.setVec(lookX, lookY, lookZ);
		upVec.setVec(upX, upY, upZ);
	}
}
