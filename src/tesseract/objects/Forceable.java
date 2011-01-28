package tesseract.objects;

import javax.vecmath.Vector3f;

/**
 * Objects that can have forces applied to them implement this interface.
 * 
 * @author Jesse Morgan
 */
public interface Forceable extends Physical {
	/**
	 * Apply a new force to this object.
	 * @param force The force to apply.
	 */
	void addForce(final Vector3f force);
	
	/**
	 * @return The inverse mass of the object.
	 */
	float getInverseMass();

	/**
	 * @return Get the velocity of the object.
	 */
	Vector3f getVelocity();
}
