package tesseract;

import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Group;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Sphere;

/**
 * This class is the parent of all objects in the world.
 * 
 * @author Jesse Morgan
 */
public abstract class PhysicalObject {
	/**
	 * The inverse of the object's mass.
	 */
	protected float inverseMass;
	
	/**
	 * The object's current position.
	 */
	private Vector3f myPosition;
	
	/**
	 * The object's previous position.
	 */
	private Vector3f myPrevPosition;
	
	private Vector3f myVelocity;
	
	private BranchGroup BG;
	private TransformGroup TG;

	public PhysicalObject(final Vector3f position, final float mass) {
		inverseMass = 1 / mass;
		myPosition = new Vector3f(position);
		myPrevPosition = new Vector3f(position);
	}
	
	public Particle(Color3f color, Vector3f position, Vector3f velocity) {
		inverseMass = 1;
		this.position = new Vector3f(position);
		prevPosition = new Vector3f(position);
		this.velocity = new Vector3f(velocity);
		force = new Vector3f();
		TG = new TransformGroup();
		TG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		TG.addChild(createShape(color));
		BG = new BranchGroup();
		BG.setCapability(BranchGroup.ALLOW_DETACH);
		BG.addChild(TG);
		updateTransformGroup();
	}
	
	public float getInverseMass() {
		return inverseMass;
	}

	public Vector3f getPosition() {
		return myPosition;
	}

	public Vector3f getPreviousPosition() {
		return myPrevPosition;
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public Group getGroup() {
		return BG;
	}
	
	public void detach() {
		BG.detach();
	}

	public void addForce(Vector3f force) {
		this.force.add(force);
	}

	public void updateState(float duration) {
		// The force vector now becomes the acceleration vector.
		force.scale(inverseMass);
		prevPosition.set(position);
		position.scaleAdd(duration, velocity, position);
		position.scaleAdd(duration * duration / 2, force, position);
		velocity.scaleAdd(duration, force, velocity);
		// The force vector is cleared.
		force.x = force.y = force.z = 0;
		updateTransformGroup();
	}

	public void updateTransformGroup() {
		Transform3D tmp = new Transform3D();
		tmp.setTranslation(position);
		TG.setTransform(tmp);
	}
	
	private Node createShape(Color3f color) {
		ColoringAttributes cAttr;
		if (color == null) {
			Color randomColor = Color.getHSBColor((float)Math.random(), 1, 1);
			color = new Color3f(randomColor);
		}
		cAttr = new ColoringAttributes(color, ColoringAttributes.FASTEST);
		Appearance appearance = new Appearance();
		appearance.setColoringAttributes(cAttr);
		return new Sphere(RADIUS, 0, 8, appearance);
	}
}
