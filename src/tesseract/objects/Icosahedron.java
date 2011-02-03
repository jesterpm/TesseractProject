/*
 * Icosahedron.java
 * TCSS 491 Computational Worlds
 * Phillip Cardon
 */
package tesseract.objects;

import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

/**
 * Represents an Icosahedron, a 20 sided object who's
 * faces are all equal equilateral triangles.
 * @author Phillip Cardon
 * @verson 0.9a
 */
public class Icosahedron extends ForceableObject {
	//CONSTANTS
	//private static final Color DEFAULTCOLOR;
	//FIELDS
	private Shape3D myShape;
	
	private TransformGroup myTG;

	//CONSTRUCTORS
	/**
	 * Create new Icosahedron.
	 */
	public Icosahedron(final Vector3f position, final float mass, final Vector3f scale) {
		this(position, mass);
	}
	/**
	 * Create new Icosahedron.
	 * @param position Initial Position.
	 * @param mass object mass.
	 */
	public Icosahedron(final Vector3f position, final float mass) {
		super(position, mass);
		// TODO Auto-generated constructor stub
	}
	
}
