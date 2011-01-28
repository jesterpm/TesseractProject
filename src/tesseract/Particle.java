package tesseract;

import com.sun.j3d.utils.geometry.*;
import java.awt.*;
import javax.media.j3d.*;
import javax.vecmath.*;

public class Particle implements Forceable {
	private float inverseMass;
	private Vector3f position, prevPosition;
	private Vector3f velocity;
	private Vector3f force;
	private BranchGroup BG;
	private TransformGroup TG;

	private static final float RADIUS = 0.004f;
	
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
		return position;
	}

	public Vector3f getPreviousPosition() {
		return prevPosition;
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
