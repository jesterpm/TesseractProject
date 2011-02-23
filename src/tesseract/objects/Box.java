package tesseract.objects;
import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.Geometry;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Primitive;

public class Box extends PhysicalObject {
	/**
	 * default color.
	 */
	public static final Color3f DEFAULT_COLOR = new Color3f(0.7f, 1, 0.7f);
	
	/**
	 * Object color.
	 */
	 private final Color3f myColor;
	
	/**
	 * 
	 * @param width
	 * @param height
	 * @param depth
	 * @param position
	 */
	public Box(float width, float height, float depth, Vector3f position) {
		this(1, width, height, depth, position, DEFAULT_COLOR.get());
	}
	
	public Box(float mass, float width, float height, float depth, Vector3f position, Color theColor) {
		super(position, mass);
		myColor = new Color3f(theColor);
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
		material.setDiffuseColor(myColor);
		appearance.setMaterial(material);
		return new com.sun.j3d.utils.geometry.Box(width / 2, height / 2, depth / 2, appearance);
	}
}
