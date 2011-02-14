package tesseract.forces;

import javax.vecmath.Vector3f;

import tesseract.objects.PhysicalObject;

@SuppressWarnings("restriction")
public class CircularXY extends Force {
	private float strength;
	
	public CircularXY(float strength) {
		this.strength = strength;
	}

	public Vector3f calculateForce(final PhysicalObject obj) {
		Vector3f force = new Vector3f(-obj.getPosition().y, 0, obj.getPosition().x);
		if (force.length() > 0) {
			force.normalize();
			force.scale(strength);
		}
		return force;
	}
	
	public String toString() {
		return "Tangential force in the XY plane";
	}
}
