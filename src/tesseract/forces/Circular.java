package tesseract.forces;

import javax.vecmath.Vector3f;

import tesseract.objects.PhysicalObject;

@SuppressWarnings("restriction")
public class Circular extends Force {
	private float strength;
	
	public Circular(float strength) {
		this.strength = strength;
	}

	public Vector3f calculateForce(final PhysicalObject obj) {
		Vector3f force = new Vector3f(-obj.getPosition().z, 0, obj.getPosition().x);
		if (force.length() > 0) {
			force.normalize();
			force.scale(strength);
		}
		return force;
	}
	
	public String toString() {
		return "Tangential force in the XZ plane";
	}
}
