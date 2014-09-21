package physics;

import core.*;
import terrain.Tile;
import utils.*;
import entities.*;

public class PhysicsEngine {
	// TODO: Consider incorporation OBB-AABB collision detection.
	
	private World world;

	public PhysicsEngine(World world) {
		this.world = world;
	}
	
	public void tick() {
		for (Entity entity : world.entityList)
			if (entity instanceof Tangible && ((Tangible) entity).getMass()>0) {
				entity.speed.scaleX(0.5F).scaleZ(0.5F); //General friction depends on surge scale.
				entity.speed.y -= 0.05F;
				if(entity.speed.y<-2)
					entity.speed.y =-2;
				entity.speed.y -= 0.05F;
				if(Geom.mag(entity.speed)<0.001F)
					entity.speed.reset();
			}
		
		for (int i = 0; i < world.entityList.size(); i++) {
			for (int j = i+1; j < world.entityList.size(); j++) {
				Entity entity1 = world.entityList.get(i), entity2 = world.entityList.get(j);
				if(collideEntities(entity1, entity2)) {
					entity1.onCollisionWithEntity(entity2);
					entity2.onCollisionWithEntity(entity1);
				}
			}
		}
		
		for (Entity entity : world.entityList) {
			if (!(entity instanceof Tangible))
				continue;
			AABB aabb = (AABB) ((Tangible) entity).getBoundingBox();
			
			boolean inGround = false;
			int y = FastMath.floor(aabb.centre.y-aabb.halfHeight);
			for (int x = FastMath.floor(aabb.centre.x-aabb.halfWidth); x <= FastMath.floor(aabb.centre.x+aabb.halfWidth); x++) {
				for (int z = FastMath.floor(aabb.centre.z-aabb.halfWidth); z <= FastMath.floor(aabb.centre.z+aabb.halfWidth); z++) {
					Tile tile = world.getTile(x, y, z);
					//TODO: Consider tiles as single static objects.
					if(tile!=null && tile.id != 0)
						inGround = true;
				}
			}
			
			//TODO: X and Z terrain collision detection.
			if(inGround) {
				entity.setPosition(entity.position.x, y+1+aabb.halfHeight, entity.position.z);
				entity.speed.y = 0;
				entity.onGround = true;
			}
		}
	}

	private boolean collideEntities(Entity entity1, Entity entity2) {
		if (!(entity1 instanceof Tangible) || !(entity2 instanceof Tangible))
			return false;
		
		Tangible tanEntity1 = (Tangible) entity1, tanEntity2 = (Tangible) entity2;
		Vec3D[] projVecs = getProjVecs(tanEntity1.getBoundingBox(), tanEntity2.getBoundingBox());
		if(projVecs == null)
			return false;
		
		float mass1 = tanEntity1.getMass(), mass2 = tanEntity2.getMass();
		boolean isEntity1Rigid = mass1<=0, isEntity2Rigid = mass2<=0;
		mass1 = isEntity1Rigid?1:mass1;
		mass2 = isEntity2Rigid?1:mass2;
		
		Vec3D escape1 = (isEntity1Rigid&&!isEntity2Rigid)?new Vec3D(0, 0, 0):Geom.scale(projVecs[0], -(mass2/mass1));
		Vec3D escape2 = (isEntity2Rigid&&!isEntity1Rigid)?new Vec3D(0, 0, 0):Geom.scale(projVecs[1], -(mass1/mass2));
		entity1.setPosition(Geom.add(entity1.position, escape1));
		entity2.setPosition(Geom.add(entity2.position, escape2));
		entity1.speed.translate(escape1);
		entity2.speed.translate(escape2);
		return true;
	}

	private Vec3D[] getProjVecs(CollisionBoundary bb1, CollisionBoundary bb2) {
		AABB aabb1 = (AABB) bb1, aabb2 = (AABB) bb2;
		Vec3D projVec1 = new Vec3D(0, 0, 0), projVec2 = new Vec3D(0, 0, 0);
		
		float minX1 = aabb1.centre.x - aabb1.halfWidth, minX2 = aabb2.centre.x - aabb2.halfWidth;
		float maxX1 = aabb1.centre.x + aabb1.halfWidth, maxX2 = aabb2.centre.x + aabb2.halfWidth;
		float projX = 0;
		if ((maxX1 > minX2) && (maxX1 < maxX2))
			projX = maxX1 - minX2;
		else if ((maxX2 > minX1) && (maxX2 < maxX1))
			projX = minX1 - maxX2;
		if(projX == 0)
			return null;
		
		float minZ1 = aabb1.centre.z - aabb1.halfWidth, minZ2 = aabb2.centre.z - aabb2.halfWidth;
		float maxZ1 = aabb1.centre.z + aabb1.halfWidth, maxZ2 = aabb2.centre.z + aabb2.halfWidth;
		float projZ = 0;
		if ((maxZ1 > minZ2) && (maxZ1 < maxZ2))
			projZ = maxZ1 - minZ2;
		else if ((maxZ2 > minZ1) && (maxZ2 < maxZ1))
			projZ = minZ1 - maxZ2;
		if(projZ == 0)
			return null;
		
		float minY1 = aabb1.centre.y - aabb1.halfHeight, minY2 = aabb2.centre.y - aabb2.halfHeight;
		float maxY1 = aabb1.centre.y + aabb1.halfHeight, maxY2 = aabb2.centre.y + aabb2.halfHeight;
		float projY = 0;
		if ((maxY1 > minY2) && (maxY1 < maxY2))
			projY = maxY1 - minY2;
		else if ((maxY2 > minY1) && (maxY2 < maxY1))
			projY = minY1 - maxY2;
		if(projY == 0)
			return null;
		
		//TODO: Create entity attribute or method for step height.
		if((projX < projY) && (projX < projZ)) {
			projVec1.x = projX;
			projVec2.x = -projX;
		} else if((projY < projX) && (projY < projZ)) {
			projVec1.y = projY;
			projVec2.y = -projY;
		} else if((projZ < projX) && (projZ < projY)) {
			projVec1.z = projZ;
			projVec2.z = -projZ;
		}
		return new Vec3D[]{projVec1, projVec2};
	}
}