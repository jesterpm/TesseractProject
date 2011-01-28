package tesseract;

import javax.vecmath.Vector3f;

/**
 * Objects that can have forces applied to them implement this interface.
 * 
 * @author Jesse Morgan
 */
public interface Forceable {
	public void addForce(final Vector3f force);
}
