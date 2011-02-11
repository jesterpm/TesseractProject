package tesseract.objects;

import java.util.List;

import javax.vecmath.Vector3f;

/**
 * This class is the an abstract parent class for forceable objects.
 * 
 * @author Jesse Morgan
 */
public abstract class ForceableObject 
	extends PhysicalObject implements Forceable {
	/**
	 * The inverse of the object's mass.
	 */
	protected float myInverseMass;
	
	/**
	 * Object's velocity.
	 */
	private Vector3f myVelocity;
	
	/**
	 * Sum of all the forces affecting this object.
	 */
	private Vector3f myForces;
	
	/**
	 * Construct a new ForceableObject.
	 * 
	 * @param position Initial Position.
	 * @param mass Initial Mass.
	 */
	public ForceableObject(final Vector3f position, final float mass) {
		super(position);
		
		myInverseMass = 1 / mass;
		myVelocity = new Vector3f(0, 0, 0);
		myForces = new Vector3f(0, 0, 0);
	}

	/**
	 * @return The inverse mass of the object.
	 */
	public float getInverseMass() {
		return myInverseMass;
	}
	
	/**
	 * @return Get the velocity of the object.
	 */
	public Vector3f getVelocity() {
		return myVelocity;
	}

	/**
	 * Apply a new force to this object.
	 * @param force The force to apply.
	 */
	public void addForce(final Vector3f force) {
		myForces.add(force);
	}
	
	/**
	 * Update the state of the forceable object.
	 * 
	 * @param duration The length of time that has passed.
	 * @return A list of new objects to add to the world.
	 */
	public List<PhysicalObject> updateState(final float duration) {
		List<PhysicalObject> children = super.updateState(duration);
		
		// The force vector now becomes the acceleration vector.
		myForces.scale(myInverseMass);
		myPosition.scaleAdd(duration, myVelocity, myPosition);
		myPosition.scaleAdd(duration * duration / 2, myForces, myPosition);
		myVelocity.scaleAdd(duration, myForces, myVelocity);
		
		// The force vector is cleared.
		myForces.x = 0;
		myForces.y = 0;
		myForces.z = 0;
		
		updateTransformGroup();
		
		return children;
	}

}
