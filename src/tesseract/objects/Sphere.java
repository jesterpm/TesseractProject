package tesseract.objects;
import javax.media.j3d.*;
import javax.vecmath.*;

import alden.CollidableObject;

public class Sphere extends PhysicalObject {
	public float radius;
	
	public Sphere(float radius, Vector3f position) {
		this(1, radius, position);
	}
	
	public Sphere(float mass, float radius, Vector3f position) {
		super(position, mass);
		setShape(createShape(radius, 22));
		this.radius = radius;
		
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
