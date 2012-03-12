package gfx;

import utils.Vec3DF;

public class Camera {

	public Vec3DF eyeVec = new Vec3DF(0F, 0F, 0F);
	public Vec3DF lookVec = new Vec3DF(0F, 0F, 0F);
	public Vec3DF upVec = new Vec3DF(0F, 1F, 0F);

	public void setVectors(float eyeX, float eyeY, float eyeZ, float lookX,
			float lookY, float lookZ, float upX, float upY, float upZ) {
		eyeVec.setVec(eyeX, eyeY, eyeZ);
		lookVec.setVec(lookX, lookY, lookZ);
		upVec.setVec(upX, upY, upZ);
	}
}
