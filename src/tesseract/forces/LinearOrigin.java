package tesseract.forces;

import javax.vecmath.Vector3f;

import tesseract.objects.PhysicalObject;

@SuppressWarnings("restriction")
public class LinearOrigin extends Force {
	private float scale;
	
	public LinearOrigin(float scale) {
		this.scale = scale;
	}

	public Vector3f calculateForce(PhysicalObject obj) {
		Vector3f position = obj.getPosition();
		Vector3f force = new Vector3f(-position.x, -position.y, -position.z);
		force.scale(scale);
		return force;
	}
	
	public String toString() {
		return "Linear proportional force towards the origin";
	}
}
