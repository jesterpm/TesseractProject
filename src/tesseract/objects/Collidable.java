package tesseract.objects;

public interface Collidable extends Physical {

	boolean hasCollision(final Physical obj);
	
	CollisionInfo calculateCollision(final Physical obj);
	
	void resolveCollision(final Physical obj, final CollisionInfo collision);
}
