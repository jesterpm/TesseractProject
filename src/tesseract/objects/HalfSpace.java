package tesseract.objects;

import javax.vecmath.Vector3f;

public class HalfSpace extends PhysicalObject {
	public Vector3f normal;
	// Right-hand side of the plane equation: Ax + By + Cz = D
	public float intercept;

	public HalfSpace(Vector3f position, Vector3f normal) {
		super(position, Float.POSITIVE_INFINITY);

		this.normal = new Vector3f(normal);
		this.normal.normalize();
		this.intercept = this.normal.dot(position);
	}
}
