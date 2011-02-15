package tesseract.forces;

import javax.vecmath.Vector3f;

import tesseract.objects.PhysicalObject;

@SuppressWarnings("restriction")
public class QuadradicOrigin extends Force {
	private float scale;
	
	public QuadradicOrigin(float scale) {
		this.scale = scale;
	}

	public Vector3f calculateForce(PhysicalObject obj) {
		Vector3f position = obj.getPosition();
		Vector3f force = new Vector3f(-position.x * Math.abs(position.x), -position.y * Math.abs(position.y), -position.z * Math.abs(position.z));
		force.scale(scale);
		return force;
	}
	
	public String toString() {
		return "Quadratic proportional force towards the origin";
	}
}
