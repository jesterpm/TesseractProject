package tesseract.objects;

import java.util.Enumeration;
import java.util.List;

import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BoundingLeaf;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupOnTransformChange;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * This class is the parent of all objects in the world.
 * 
 * Note: The constructor of a child class must add its shape
 *  to the transform group for it to be visible.
 * 
 * @author Jesse Morgan
 */
public abstract class PhysicalObject extends TransformGroup implements Physical {
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
	 * 
	 */
	private int skipTGUpdates;
	
	/**
	 * Constructor for a PhysicalObject.
	 * 
	 * @param position Initial position.
	 */
	
	public PhysicalObject(final Vector3f position) {
		skipTGUpdates = 0;
		myPosition = new Vector3f(position);
		
		myExistance = true;
		
		myTransformGroup = this; //new TransformGroup();
		myTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		myTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		myTransformGroup.setCapability(TransformGroup.ENABLE_PICK_REPORTING);

		
		myBranchGroup = new BranchGroup();
		myBranchGroup.setCapability(BranchGroup.ALLOW_DETACH);
		myBranchGroup.addChild(myTransformGroup);
		//myBranchGroup.addChild(new TGUpdateBehavior(null));

		updateTransformGroup();
	}

	public void setTransform(Transform3D t1) {
		super.setTransform(t1);
		
		Point3f pos = new Point3f(myPosition);
		t1.transform(pos);
		myPosition = new Vector3f(pos);
	}
	
	/**
	 * @return The object's position.
	 */
	public Vector3f getPosition() {
		return myPosition;
	}
	
	/**
	 * Update the object's position.
	 * 
	 * @param position The new position.
	 */
	public void setPosition(final Vector3f position) {
		myPosition = new Vector3f(position);
		updateTransformGroup();
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
	public BranchGroup getGroup() {
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
		
		skipTGUpdates++;
		super.setTransform(tmp);
	}

	/**
	 * Update the state of the object.
	 * @param duration How much time has passed.
	 * @return New objects to add to the world.
	 */
	public List<PhysicalObject> updateState(final float duration) {
		return null;
	}
	
	private class TGUpdateBehavior extends Behavior {
		public TGUpdateBehavior(final BoundingLeaf boundingLeaf) {
			//setSchedulingBoundingLeaf(boundingLeaf);
			setSchedulingBounds(new BoundingBox(new Point3d(-0.5, -0.5, -0.5), 
					new Point3d(0.5, 0.5, 0.5)));
		}

		public void initialize() {
			wakeupOn(new WakeupOnTransformChange(getTransformGroup()));
		}

		public void processStimulus(final Enumeration e) {
			if (skipTGUpdates == 0) {
				System.out.println(myPosition);
				
				Transform3D t3d = new Transform3D();
				getTransformGroup().getTransform(t3d);
				
				Point3f pos = new Point3f(myPosition);
				t3d.transform(pos);
				System.out.println(pos);
				myPosition = new Vector3f(pos);
			} else {
				skipTGUpdates--;
				System.out.println("Skip");
			}
			
			wakeupOn(new WakeupOnTransformChange(getTransformGroup()));
		}
	}
}
