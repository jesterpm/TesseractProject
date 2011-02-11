package tesseract.objects;

import javax.vecmath.Vector3f;

/**
 * This interface is applied to any object that has a position in the world.
 * 
 * @author Jesse Morgan
 */
public interface Physical {

	/**
	 * @return The position of the object in the world.
	 */
	Vector3f getPosition(); 
	
	/**
	 * Set the object's position.
	 * 
	 * @param position The new position.
	 */
	void setPosition(Vector3f position);
}
