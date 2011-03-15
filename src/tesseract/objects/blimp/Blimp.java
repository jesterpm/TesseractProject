package tesseract.objects.blimp;

import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;

import tesseract.objects.PhysicalObject;

import com.sun.j3d.utils.geometry.Sphere;

public class Blimp extends PhysicalObject {

	
	/**
	 * Default mass.
	 */
	//private static final float DEFAULT_MASS = Float.POSITIVE_INFINITY;
	private static final float DEFAULT_MASS = 10;
	/**
	 * Create a new Blimp.
	 * 
	 * @param position Initial position.
	 * @param radius a float for the size of the base sphere.
	 * @param theColor of object.
	 */
	public Blimp(final Vector3f position, final float radius) {
		super(position, DEFAULT_MASS);
		final float rSq = radius * radius;
		final float a = 1.0f;
		final float b = 1.0f;
		final float c = 2f;
		
		
		//setShape(createDefaultEllipsoid(radius, a, b, c));
		
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
	/*private TransformGroup createDefaultEllipsoid(final float radius, final float a,
			final float b, final float c) {
		
		BranchGroup blimp = new BranchGroup();
		
		//blimp node
		Appearance meshApp = new Appearance();
		Material surface = new Material();
		surface.setDiffuseColor(new Color3f(1f, 1f, 1f));
		meshApp.setMaterial(surface);
		meshApp.setColoringAttributes(new ColoringAttributes(new Color3f(1f, 1f, 1f),
				ColoringAttributes.NICEST));
		Sphere sphere = new Sphere(radius, new Sphere().getPrimitiveFlags() | Sphere.ENABLE_GEOMETRY_PICKING,
				30, meshApp);
		Transform3D tmp = new Transform3D();
		tmp.set(new Matrix3f(a, 0.0f, 0.0f, 0.0f, b, 0.0f, 0.0f, 0.0f, c));
		TransformGroup tgBlimp = new TransformGroup(tmp);
		tgBlimp.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tgBlimp.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		tgBlimp.addChild(sphere);
		
		//box node
		//Box box = new Box(2f, .1f, .2f, .3f, new Vector3f(0f,-.05f, 0f), new Color3f(1f, 1f, 1f));
		
		
		
		
		
		//return tg;
	}*/
}
