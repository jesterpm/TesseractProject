/*
 * TCSS 491 Computational Worlds
 * Author Steve Bradshaw
 */
package tesseract.objects;

import javax.media.j3d.Appearance;
import javax.media.j3d.Group;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Matrix3f;

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
public class Ellipsoid extends Sphere {
	
	/**
	 *  The b in the formula (x/a)^2 + (y/b)^2 + (z/c)^2 = 1.
	 */
	private float my_b;
	
	/**
	 *  The c in the formula (x/a)^2 + (y/b)^2 + (z/c)^2 = 1.
	 */
	private float my_c;
	
	/**
	 *  The Group containing the new ellipsoid.
	 */
	private TransformGroup my_ellipsoidTG;
	
	/**
	 * Constructor similar to sphere but with the additions of b and c to change
	 * the shape.
	 * This constructor does not have the Appearance as an argument.
	 * 
	 * @param radius the radius of the ellipsoid if in sphere form
	 * @param primflags an int
	 * @param divisions an int 
	 * @param b to change the shape of the ellipsoid in the y direction
	 * @param c to change the shape of the ellipsoid in the z direction
	 */
	public Ellipsoid(final float radius, final int primflags,
			final int divisions, final float b, final float c) {
		super(radius, primflags, divisions);
		
		my_b = b;
		my_c = c;
		my_ellipsoidTG = new TransformGroup();
		createGeometry();
	}
	
	/**
	 * Constructor similar to sphere but with the additions of b and c to change
	 * the shape. This constructor adds Appearance as an argument.
	 * 
	 * @param radius the radius of the ellipsoid if in sphere form
	 * @param primflags an int
	 * @param divisions an int
	 * @param appearance brings an Appearance object for material, color etc.
	 * @param b to change the shape of the ellipsoid in the y direction
	 * @param c to change the shape of the ellipsoid in the z direction
	 */
	public Ellipsoid(final float radius, final int primflags,
			final int divisions, final Appearance appearance,
			final float b, final float c) {
		super(radius, primflags, divisions, appearance);
		
		my_b = b;
		my_c = c;
		my_ellipsoidTG = new TransformGroup();
		createGeometry();
	}
	
	/*
	 * This private method transforms a sphere using a 3D Matrix
	 */
	private void createGeometry() {
		Transform3D tmp = new Transform3D();
		tmp.set(new Matrix3f(1.0f, 0.0f, 0.0f, 0.0f, my_b, 0.0f, 0.0f, 0.0f, my_c));
		my_ellipsoidTG.setTransform(tmp);
		my_ellipsoidTG.addChild(this);
	}
	
	/**
	 * This method is the getter to get custom ellipsoid
	 * 
	 * @return Group containing the transformation of the sphere
	 */
	public Group getEllipsoid() {
		return my_ellipsoidTG;
	}
}
