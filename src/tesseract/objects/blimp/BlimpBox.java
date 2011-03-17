package tesseract.objects.blimp;

import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.Geometry;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import tesseract.objects.PhysicalObject;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Primitive;

public class BlimpBox extends PhysicalObject {
	
	/**
	 * The appearance of this blimp box
	 */
	private Appearance my_appearance;
	
	/**
	 * The width.
	 */
	private float my_width;
	
	/**
	 * The height.
	 */
	private float my_height;
	
	/**
	 * The depth.
	 */
	private float my_depth;
	
	/**
	 * The box for the blimp
	 */
	private Primitive my_box;
	
	/**
	 * The tg for this object
	 */
	private TransformGroup my_tg;
	

	
	public BlimpBox(float mass, float width, float height, float depth,
			Vector3f position, final Appearance app) {
		super(position, mass);
		
		my_width = width;
		my_height = height;
		my_depth = depth;
		my_appearance = app;
		
		
		setShape(createShape());
		
		previousPosition.set(position);
		if (inverseMass != 0) {
			inverseInertiaTensor.m00 = 1f / 12 / inverseMass * (height * height + depth * depth);
			inverseInertiaTensor.m11 = 1f / 12 / inverseMass * (width * width + depth * depth);
			inverseInertiaTensor.m22 = 1f / 12 / inverseMass * (width * width + height * height);
			inverseInertiaTensor.invert();
		}
		updateTransformGroup();
	}

	public Node createShape() {
		Primitive box = new com.sun.j3d.utils.geometry.Box(my_width / 2, my_height / 2, my_depth / 2, my_appearance);
		my_box = box;
		return box;
	}
	
	/**
	 * Return the box shape to be used to get the front based on Phil's idea to get front
	 */
	public Primitive getBoxShape() {
		return my_box;
	}
	
	
	/**
	 * get the tg for this box
	 */
	public Node getTG() {
		return my_tg;
	}
}

