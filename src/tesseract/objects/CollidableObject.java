package tesseract.objects;

import javax.vecmath.Vector3f;

public abstract class CollidableObject extends PhysicalObject implements Collidable {

	public CollidableObject(Vector3f position) {
		super(position);
	}

	public CollisionInfo calculateCollision(Physical obj) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasCollision(Physical obj) {
		// TODO Auto-generated method stub
		return false;
	}

	public void resolveCollision(Physical obj, CollisionInfo collision) {
		// TODO Auto-generated method stub
		
	}

}
