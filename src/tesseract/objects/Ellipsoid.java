/*
 * Class Ellipsoid
 * TCSS 491 Computational Worlds
 * Steve Bradshaw
 */

package tesseract.objects;

import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Sphere;

/**
 * This class creates an ellipsoid using the formula 
 * (x/a)^2  +  (y/b)^2  +  (z/c)^2 = 1 using a matrix3f transformation
 * on a basic Sphere.  This class sets 'a' to a constant 1.0 and allows
 * 'b' and 'c'  to alter the ellipsoid's shape along with the radius field 
 * Sphere.  Since this is a sphere, the normals are already calculated.
 * 
 * @author Steve Bradshaw
 * @version 1 Feb 2011
 */
public class Ellipsoid extends PhysicalObject {
	
	/**
	 * Default mass.
	 */
	//private static final float DEFAULT_MASS = Float.POSITIVE_INFINITY;
	private static final float DEFAULT_MASS = 10;
	
	/**
	 * Default Object color.
	 */
	private static final Color3f DEFAULT_COLOR = new Color3f(.9f, .05f, .05f);
	
	/**
	 * User definable color.
	 */
	private final Color3f myColor; 
	
	/**
	 * Number of divisions in the sphere.
	 */
	private static final int DEFAULT_DIVISIONS = 50;
	
	
	/**
	 * Create a new Ellipsoid.
	 * 
	 * @param position Initial position.
	 * @param mass Initial mass.
	 * @param radius the radius of the base sphere.
	 * @param primflags an int for the base spere primflags.
	 * @param divisions an in for the shape divisions.
	 * @param appearance an Appearance object.
	 * @param b a float for the b portion of the ellipsoid formula.
	 * @param c a float for the c portion of the ellipsoid formula.
	 * @param theColor of the object.
	 */
	public Ellipsoid(final Vector3f position, final float mass,
			final float radius,	final int primflags, final int divisions,
			final Appearance appearance, final float b, final float c) {
		super(position, mass);
		myColor = new Color3f();
		appearance.getMaterial().getDiffuseColor(myColor);
		setShape(createShape(radius, primflags, appearance, divisions, b, c));
		
		final float rSq = radius * radius;
		final float a = 1.0f;
		
		if (inverseMass != 0) {
			inverseInertiaTensor.m00 = 1f / 5 / inverseMass * (b * rSq + c * rSq);
			inverseInertiaTensor.m11 = 1f / 5 / inverseMass * (a * rSq + c * rSq);
			inverseInertiaTensor.m22 = 1f / 5 / inverseMass * (a * rSq + b * rSq);
			inverseInertiaTensor.invert();
		}
		updateTransformGroup();
	}
	
	/**
	 * Create a new Ellipsoid.
	 * @author Phillip Cardon
	 * @param position Initial position.
	 * @param mass mass of ellipsoid
	 * @param radius a float for the size of the base sphere.
	 * @param theColor of the object.
	 */
	public Ellipsoid(final Vector3f position, final float mass,
			final float radius) {
		this(position, mass, radius, DEFAULT_COLOR.get());
	}
	
	/**
	 * Create a new Ellipsoid.
	 * @author Phillip Cardon
	 * @param position Initial position.
	 * @param mass mass of ellipsoid
	 * @param radius a float for the size of the base sphere.
	 * @param theColor of the object.
	 */
	public Ellipsoid(final Vector3f position, final float mass,
			final float radius, Color theColor) {
		super(position, mass);
		myColor = new Color3f(theColor);
		final float rSq = radius * radius;
		final float a = 1.0f;
		final float b = 1.0f;
		final float c = 1.5f;
		
		
		setShape(createDefaultEllipsoid(radius, a, b, c));
		
		if (inverseMass != 0) {
			inverseInertiaTensor.m00 = 1f / 5 / inverseMass * (b * rSq + c * rSq);
			inverseInertiaTensor.m11 = 1f / 5 / inverseMass * (a * rSq + c * rSq);
			inverseInertiaTensor.m22 = 1f / 5 / inverseMass * (a * rSq + b * rSq);
			inverseInertiaTensor.invert();
		}
		updateTransformGroup();
	}
	
	/**
	 * Create a new Ellipsoid.
	 * 
	 * @param position Initial position.
	 * @param radius a float for the size of the base sphere.
	 */
	public Ellipsoid(final Vector3f position, final float radius) {
		this(position, radius, DEFAULT_COLOR.get());
	}
	
	/**
	 * Create a new Ellipsoid.
	 * 
	 * @param position Initial position.
	 * @param radius a float for the size of the base sphere.
	 * @param theColor of object.
	 */
	public Ellipsoid(final Vector3f position, final float radius,
			final Color theColor) {
		super(position, DEFAULT_MASS);
		myColor = new Color3f(theColor);
		final float rSq = radius * radius;
		final float a = 1.0f;
		final float b = 1.0f;
		final float c = 1.5f;
		
		
		setShape(createDefaultEllipsoid(radius, a, b, c));
		
		if (inverseMass != 0) {
			inverseInertiaTensor.m00 = 1f / 5 / inverseMass * (b * rSq + c * rSq);
			inverseInertiaTensor.m11 = 1f / 5 / inverseMass * (a * rSq + c * rSq);
			inverseInertiaTensor.m22 = 1f / 5 / inverseMass * (a * rSq + b * rSq);
			inverseInertiaTensor.invert();
		}
		updateTransformGroup();
	}
	
	/**
	 * This creates a default Ellipsoid for the 2 argument constructor.
	 * @param radius the size of the ellipsoid
	 * @param a float in the ellipsoid formula.
	 * @param b float in the ellipsoid formula.
	 * @param c float in the ellipsoid formula.
	 * @return TransformGroup with the shape.
	 */
	private TransformGroup createDefaultEllipsoid(final float radius, final float a,
			final float b, final float c) {
		Appearance meshApp = new Appearance();
		Material surface = new Material();
		surface.setDiffuseColor(myColor);
		meshApp.setMaterial(surface);
		meshApp.setColoringAttributes(new ColoringAttributes(myColor,
				ColoringAttributes.NICEST));
		Sphere sphere = new Sphere(radius, new Sphere().getPrimitiveFlags() | Sphere.ENABLE_GEOMETRY_PICKING,
				DEFAULT_DIVISIONS, meshApp);
		Transform3D tmp = new Transform3D();
		tmp.set(new Matrix3f(a, 0.0f, 0.0f, 0.0f, b, 0.0f, 0.0f, 0.0f, c));
		TransformGroup tg = new TransformGroup(tmp);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tg.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		tg.addChild(sphere);
		return tg;
	}
	
	/**
	 * This method creates multiple ellipsoidial shapes.
	 * 
	 * @param radius a float for the size of the base sphere
	 * @param primflags an int for the base sphere
	 * @param appearance an Appearance object
	 * @param divisions an int for the number of divisons
	 * @param b a float for the y axis transform
	 * @param c a float for the z axis transfrom
	 * @return TransformGroup with the shape.
	 */
	private TransformGroup createShape(final float radius, final int primflags,
			final Appearance appearance, final int divisions, final float b,
			final float c) {
		
		Sphere sphere = new Sphere(radius, primflags, divisions, appearance);
		Transform3D tmp = new Transform3D();
		tmp.set(new Matrix3f(1.0f, 0.0f, 0.0f, 0.0f, b, 0.0f, 0.0f, 0.0f, c));
		TransformGroup tg = new TransformGroup(tmp);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tg.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		tg.addChild(sphere);
		
		return tg;
	}
}
