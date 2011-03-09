package common;

import javax.media.j3d.Transform3D;
import javax.vecmath.*;

@SuppressWarnings("restriction")
public abstract class Polygon extends CollidableObject {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3884429316406247640L;
	protected Vector3f normal;
	// Right-hand side of the plane equation: Ax + By + Cz = D
	protected float intercept;
	
	public Polygon(Vector3f position, Vector3f normal) {
		this(1, position, normal);
	}

	public Polygon(float mass, Vector3f position, Vector3f normal) {
		super(mass);
		this.position.set(position);
		this.normal = new Vector3f(normal);
		this.normal.normalize();
		intercept = this.normal.dot(position);
		Vector3f newX = new Vector3f(1, 0, 0);
		if (Math.abs(newX.dot(this.normal)) == 1)
			newX = new Vector3f(0, -1, 0);
		newX.scaleAdd(-newX.dot(this.normal), this.normal, newX);
		newX.normalize();
		Vector3f newZ = new Vector3f();
		newZ.cross(newX, this.normal);
		new Matrix4f(new Matrix3f(newX.x, this.normal.x, newZ.x, newX.y, this.normal.y, newZ.y, newX.z, this.normal.z, newZ.z), position, 1).get(orientation);
	}

	protected void updateTransformGroup() {
		super.updateTransformGroup();
		Transform3D tmp = new Transform3D();
		TG.getTransform(tmp);
		Matrix3f rot = new Matrix3f();
		tmp.get(rot);
		normal.x = rot.m01;
		normal.y = rot.m11;
		normal.z = rot.m21;
		intercept = normal.dot(position);
	}
}
