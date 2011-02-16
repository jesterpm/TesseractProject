package tesseract.objects;
import javax.media.j3d.Appearance;
import javax.media.j3d.Geometry;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Primitive;

public class Box extends PhysicalObject {
	public Box(float width, float height, float depth, Vector3f position) {
		this(1, width, height, depth, position);
	}
	
	public Box(float mass, float width, float height, float depth, Vector3f position) {
		super(position, mass);
		setShape(createShape(width, height, depth));
		
		previousPosition.set(position);
		if (inverseMass != 0) {
			inverseInertiaTensor.m00 = 1f / 12 / inverseMass * (height * height + depth * depth);
			inverseInertiaTensor.m11 = 1f / 12 / inverseMass * (width * width + depth * depth);
			inverseInertiaTensor.m22 = 1f / 12 / inverseMass * (width * width + height * height);
			inverseInertiaTensor.invert();
		}
		updateTransformGroup();
	}

	protected Node createShape(float width, float height, float depth) {
		Appearance appearance = new Appearance();
		Material material = new Material();
		material.setDiffuseColor(0.7f, 1, 0.7f);
		appearance.setMaterial(material);
		return new com.sun.j3d.utils.geometry.Box(width / 2, height / 2, depth / 2, appearance);
	}
}
