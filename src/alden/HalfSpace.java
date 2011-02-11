package alden;

import javax.vecmath.Vector3f;

@SuppressWarnings("restriction")
public class HalfSpace extends CollidableObject {
	protected Vector3f normal;
	// Right-hand side of the plane equation: Ax + By + Cz = D
	protected float intercept;

	public HalfSpace(Vector3f position, Vector3f normal) {
		super(Float.POSITIVE_INFINITY);
		this.position.set(position);
		this.normal = new Vector3f(normal);
		this.normal.normalize();
		intercept = this.normal.dot(position);
	}

/*	public CollisionInfo calculateCollision(Particle particle) {
		if (Math.signum(normal.dot(particle.getPosition()) - intercept) == Math.signum(normal.dot(particle.getPreviousPosition()) - intercept))
			return null;
		
		Vector3f projection = new Vector3f();
		projection.scaleAdd(-1, position, particle.getPosition());
		float penetration = -projection.dot(normal);
		return new CollisionInfo(normal, penetration);
	}*/
}
