package physics;

import core.World;
import utils.*;
import entities.*;

public class AdvancedPhysicsEngine {

	private World world;

	public AdvancedPhysicsEngine(World world) {
		this.world = world;
	}
	
	public void tick() {
		for (int i = 0; i < world.entityList.size(); i++) {
			for (int j = i+1; j < world.entityList.size(); j++) {
				Entity entity1 = world.entityList.get(i), entity2 = world.entityList.get(j);
				if (!(entity1 instanceof Tangible) || !(entity2 instanceof Tangible))
					continue;
				Tangible tanEntity1 = (Tangible) entity1, tanEntity2 = (Tangible) entity2;
				if(areEntitiesCloseEnoughToCollide(tanEntity1, tanEntity2)) {
					//TODO: Check if a preferred axis alignment (y) exists (optimisation).
					Vec3D[] projVecs = getProjectionVectors((OBB)tanEntity1.getBoundingBox(), (OBB)tanEntity2.getBoundingBox(), entity1.speed, entity2.speed);
					if (projVecs != null) {
						collideOBBs(tanEntity1, tanEntity2, projVecs[0], projVecs[1]);
						//TODO: Notify entities of collision.
					}
				}
			}
		}
	}
	
	private boolean areEntitiesCloseEnoughToCollide(Tangible tanEntity1, Tangible tanEntity2) {
		return Geom.dist(((OBB)tanEntity1.getBoundingBox()).centre, ((OBB)tanEntity2.getBoundingBox()).centre) <= (((OBB)tanEntity1.getBoundingBox()).eventHorizonRadius+((OBB)tanEntity2.getBoundingBox()).eventHorizonRadius);
	}

	/**
	 * @param obb1
	 * @param obb2
	 * @return An array of respective projection vectors closest to incident speeds or null if there is no collision. 
	 */
	private Vec3D[] getProjectionVectors(OBB obb1, OBB obb2, Vec3D speed1, Vec3D speed2) {
		Vec3D t = Geom.dir(obb1.centre, obb2.centre);

		if(speed1.x==0 && speed1.y==0 && speed1.z==0)
			speed1 = Geom.scale(speed2, -0.01F);
		else if(speed2.x==0 && speed2.y==0 && speed2.z==0)
			speed2 = Geom.scale(speed1, -0.01F);
		
		// TODO: Calculate these one by one.
		Vec3D[] l = { obb1.unitX, obb1.unitY, obb1.unitZ, obb2.unitX, obb2.unitY, obb2.unitZ,
					Geom.cross(obb1.unitX, obb2.unitX), Geom.cross(obb1.unitX, obb2.unitY), Geom.cross(obb1.unitX, obb2.unitZ),
					Geom.cross(obb1.unitY, obb2.unitX), Geom.cross(obb1.unitY, obb2.unitY), Geom.cross(obb1.unitY, obb2.unitZ),
					Geom.cross(obb1.unitZ, obb2.unitX), Geom.cross(obb1.unitZ, obb2.unitY), Geom.cross(obb1.unitZ, obb2.unitZ) };
		
		float minAngle1 = Float.MAX_VALUE, minAngle2 = Float.MAX_VALUE;
		Vec3D proj1 = new Vec3D(0, 0, 0), proj2 = new Vec3D(0, 0, 0);
		for (Vec3D sepAxis : l) {
			float obb1ProjMag = FastMath.abs(Geom.dot((Geom.scale(obb1.unitX, obb1.halfWidth)), sepAxis))
								+ FastMath.abs(Geom.dot((Geom.scale(obb1.unitY, obb1.halfHeight)), sepAxis))
								+ FastMath.abs(Geom.dot((Geom.scale(obb1.unitZ, obb1.halfDepth)), sepAxis));
			float obb2ProjMag = FastMath.abs(Geom.dot((Geom.scale(obb2.unitX, obb2.halfWidth)), sepAxis))
								+ FastMath.abs(Geom.dot((Geom.scale(obb2.unitY, obb2.halfHeight)), sepAxis))
								+ FastMath.abs(Geom.dot((Geom.scale(obb2.unitZ, obb2.halfDepth)), sepAxis));
			float sumProjMag = obb1ProjMag + obb2ProjMag;
			if (FastMath.abs(Geom.dot(t, sepAxis)) > sumProjMag)
				return null;
			Vec3D testProj1 = Geom.scale(sepAxis, obb1ProjMag), testProj2 = Geom.scale(sepAxis, obb2ProjMag);
			float angle1 = Geom.angle(speed1, testProj1), angle2 = Geom.angle(speed2, testProj2);
			if(angle1 < minAngle1) {
				minAngle1 = angle1;
				proj1 = testProj1;
			}
			if(angle2 < minAngle2) {
				minAngle2 = angle2;
				proj2 = testProj2;
			}
		}
		return new Vec3D[]{proj1, proj2};
	}

	/* NB: Collision detection will only work on convex shapes. */
	//TODO: Check visibility; check redundancy.
	public boolean areOBBsColliding(OBB obb1, OBB obb2) {
		Vec3D t = Geom.dir(obb1.centre, obb2.centre);
		
		// TODO: Calculate these one by one.
		Vec3D[] l = { obb1.unitX, obb1.unitY, obb1.unitZ, obb2.unitX, obb2.unitY, obb2.unitZ,
					Geom.cross(obb1.unitX, obb2.unitX), Geom.cross(obb1.unitX, obb2.unitY), Geom.cross(obb1.unitX, obb2.unitZ),
					Geom.cross(obb1.unitY, obb2.unitX), Geom.cross(obb1.unitY, obb2.unitY), Geom.cross(obb1.unitY, obb2.unitZ),
					Geom.cross(obb1.unitZ, obb2.unitX), Geom.cross(obb1.unitZ, obb2.unitY), Geom.cross(obb1.unitZ, obb2.unitZ) };
		
		for (Vec3D sepAxis : l)
			if (FastMath.abs(Geom.dot(t, sepAxis)) > FastMath.abs(Geom.dot((Geom.scale(obb1.unitX, obb1.halfWidth)), sepAxis))
												+ FastMath.abs(Geom.dot((Geom.scale(obb1.unitY, obb1.halfHeight)), sepAxis))
												+ FastMath.abs(Geom.dot((Geom.scale(obb1.unitZ, obb1.halfDepth)), sepAxis))
												+ FastMath.abs(Geom.dot((Geom.scale(obb2.unitX, obb2.halfWidth)), sepAxis))
												+ FastMath.abs(Geom.dot((Geom.scale(obb2.unitY, obb2.halfHeight)), sepAxis))
												+ FastMath.abs(Geom.dot((Geom.scale(obb2.unitZ, obb2.halfDepth)), sepAxis)))
				return false;
		return true;
	}
	
	public void collideOBBs(Tangible entity1, Tangible entity2, Vec3D proj1, Vec3D proj2) {
		float mass1 = entity1.getMass(), mass2 = entity2.getMass();
		boolean isEntity1Rigid = mass1==-1, isEntity2Rigid = mass2==-1;
		mass1 = isEntity1Rigid?1:mass1;
		mass2 = isEntity2Rigid?1:mass2;

		try {
			Vec3D escape1 = (isEntity1Rigid&&!isEntity2Rigid)?new Vec3D(0, 0, 0):Geom.scale(proj1, -(mass2/mass1));
			Vec3D escape2 = (isEntity2Rigid&&!isEntity1Rigid)?new Vec3D(0, 0, 0):Geom.scale(proj2, -(mass1/mass2));
			((Entity)entity1).setPosition(Geom.add(((Entity)entity1).position, escape1));
			((Entity)entity2).setPosition(Geom.add(((Entity)entity2).position, escape2));
			((Entity)entity1).speed.translate(escape1);
			((Entity)entity2).speed.translate(escape2);
		} catch (ArithmeticException e) {
			System.err.println("Error: Invalid mass value for a tangible entity.");
			System.exit(1);
		}
	}
}
