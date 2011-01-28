package tesseract.objects;

import javax.vecmath.Vector3f;

public class CollidableObject extends PhysicalObject implements Collidable {

	public CollidableObject(Vector3f position) {
		super(position);
	}

	@Override
	public CollisionInfo calculateCollision(Physical obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCollision(Physical obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void resolveCollision(Physical obj, CollisionInfo collision) {
		// TODO Auto-generated method stub
		
	}

}
