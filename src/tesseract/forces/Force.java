package tesseract.forces;

import javax.vecmath.Vector3f;

import tesseract.objects.Forceable;

/**
 * Abstract Force class.
 * 
 * @author Jesse Morgan
 */
public abstract class Force {

	/**
	 * Calculate the force to apply to the give object.
	 * @param obj The given object.
	 * @return A vector describing the force.
	 */
	protected abstract Vector3f calculateForce(final Forceable obj);
	
	/**
	 * Apply this force to the given object.
	 * 
	 * @param obj The given object.
	 */
	public void applyForceTo(final Forceable obj) {
		obj.addForce(calculateForce(obj));
	}
}
