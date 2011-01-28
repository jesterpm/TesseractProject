package tesseract.objects;

import java.util.List;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

/**
 * This class is the parent of all objects in the world.
 * 
 * Note: The constructor of a child class must add its shape
 *  to the transform group for it to be visible.
 * 
 * @author Jesse Morgan
 */
public abstract class PhysicalObject implements Physical {
	/**
	 * The object's current position.
	 */
	protected Vector3f myPosition;
	
	/**
	 * BranchGroup of the object.
	 */
	private BranchGroup myBranchGroup;
	
	/**
	 * TransformGroup for the object.
	 */
	private TransformGroup myTransformGroup;
	
	/**
	 * Does the object still exist in the world.
	 */
	protected boolean myExistance;

	/**
	 * Constructor for a PhysicalObject.
	 * 
	 * @param position Initial position.
	 */
	
	public PhysicalObject(final Vector3f position) {
		myPosition = new Vector3f(position);
		
		myExistance = true;
		
		myTransformGroup = new TransformGroup();
		myTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		myBranchGroup = new BranchGroup();
		myBranchGroup.setCapability(BranchGroup.ALLOW_DETACH);
		myBranchGroup.addChild(myTransformGroup);

		updateTransformGroup();
	}

	/**
	 * @return The object's position.
	 */
	public Vector3f getPosition() {
		return myPosition;
	}
	
	/**
	 * @return The transform group of the object.
	 */
	protected TransformGroup getTransformGroup() {
		return myTransformGroup;
	}
	
	
	/**
	 * @return Get the BranchGroup.
	 */
	public Group getGroup() {
		return myBranchGroup;
	}
	
	/**
	 * Remove the object from the world.
	 */
	public void detach() {
		myBranchGroup.detach();
		myExistance = false;
	}
	
	/**
	 * Does this object still exist.
	 * @return true if it exists.
	 */
	public boolean isExisting() {
		return myExistance;
	}

	/**
	 * Update the TransformGroup to the new position.
	 */
	protected void updateTransformGroup() {
		Transform3D tmp = new Transform3D();
		tmp.setTranslation(myPosition);
		myTransformGroup.setTransform(tmp);
	}

	/**
	 * Update the state of the object.
	 * @param duration How much time has passed.
	 * @return New objects to add to the world.
	 */
	public List<PhysicalObject> updateState(final float duration) {
		return null;
	}

}
