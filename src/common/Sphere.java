package common;

import javax.media.j3d.*;
import javax.vecmath.*;

@SuppressWarnings("restriction")
public class Sphere extends CollidableObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7821415888599737442L;
	protected float radius;
	
	public Sphere(float radius, Vector3f position) {
		this(1, radius, position);
	}
	
	public Sphere(float mass, float radius, Vector3f position) {
		super(mass);
		setShape(createShape(radius, 22));
		this.radius = radius;
		this.position.set(position);
		if (inverseMass != 0) {
			inverseInertiaTensor.m00 = 2f / 5 / inverseMass * radius * radius;
			inverseInertiaTensor.m11 = inverseInertiaTensor.m00;
			inverseInertiaTensor.m22 = inverseInertiaTensor.m00;
			inverseInertiaTensor.invert();
		}
		updateTransformGroup();
	}

	protected Node createShape(float radius, int divisions) {
		Appearance appearance = new Appearance();
		Material material = new Material();
		material.setDiffuseColor(0.7f, 0.7f, 1);
		appearance.setMaterial(material);
		return new com.sun.j3d.utils.geometry.Sphere(radius, com.sun.j3d.utils.geometry.Sphere.GENERATE_NORMALS, divisions, appearance);
	}
}
