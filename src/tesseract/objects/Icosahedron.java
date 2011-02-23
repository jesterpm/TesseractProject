/*
 * Icosahedron.java
 * TCSS 491 Computational Worlds
 * Phillip Cardon
 */
package tesseract.objects;

import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TriangleArray;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;

/**
 * Represents an Icosahedron, a 20 sided object who's
 * faces are all equal equilateral triangles.
 * @author Phillip Cardon
 * @version 1.1a
 */
public class Icosahedron extends PhysicalObject {
	//CONSTANTS
	/**
	 * Angle to stop checking normals.
	 */
	private static final int MAX_ANGLE = 120;
	
	/**
	 * Default Icosohedran Scale.
	 */
	public static final float DEFAULT_SCALE = 0.1f;
	
	/**
	 * Vertex count in Icosohedran.
	 */
	private static final int NUM_VERTEX = 12;
	
	/**
	 * Golden ratio for calculating points.
	 */
	private static final float GOLDEN_RATIO = (float) ((1.0 + Math.sqrt(5.0))
			/ 2.0);
	/**
	 * Default Object color.
	 */
	private static final Color3f DEFAULT_COLOR = new Color3f(.9f, .05f, .05f);
	
	/**
	 * User definable color.
	 */
	private final Color3f myColor;

	//CONSTRUCTORS
	/**
	 * Create new Icosahedron.
	 * @param position start position.
	 * @param mass start mass.
	 * @param scale of object.
	 * @param theColor of object.
	 */
	public Icosahedron(final Vector3f position, final float mass,
			final float scale, final Color theColor) {
		super(position, mass);
		myColor = new Color3f(theColor);
		setShape(buildIcosahedron(scale));
		
		if (inverseMass != 0) {
			final float radius = (float) (scale * Math.sqrt(GOLDEN_RATIO * GOLDEN_RATIO + 1));
			inverseInertiaTensor.m00 = 2f / 5 / inverseMass * radius * radius;
			inverseInertiaTensor.m11 = inverseInertiaTensor.m00;
			inverseInertiaTensor.m22 = inverseInertiaTensor.m00;
			inverseInertiaTensor.invert();
		}
	}
	
	/**
	 * Create new Icosahedron.
	 * @param position start position.
	 * @param mass start mass.
	 * @param scale of object.
	 */
	public Icosahedron(final Vector3f position, final float mass,
			final float scale) {
		this(position, mass, scale, DEFAULT_COLOR.get());
	}
	
	/**
	 * Create new Icosahedron.
	 * @param position Initial Position.
	 * @param mass object mass.
	 */
	public Icosahedron(final Vector3f position, final float mass) {
		this(position, mass, DEFAULT_SCALE);
		
	}
	
	/**
	 * Builds Icosahedron.
	 * @param scale of Icosahedron
	 * @return shape object
	 */
	public Shape3D buildIcosahedron(final float scale) {
		Point3f[] coordinates = new Point3f[NUM_VERTEX];
		
		float phi = GOLDEN_RATIO;
		int i = 0;
		// Y / Z Plane coordinates
		coordinates[i++] = new Point3f(0f, 1.0f, phi);          //0
		coordinates[i++] = new Point3f(0f, 1.0f, -1 * phi);    
		coordinates[i++] = new Point3f(0f, -1.0f, -1 * phi);
		coordinates[i++] = new Point3f(0f, -1.0f, phi);
		// X / Y Plane coordinates
		coordinates[i++] = new Point3f(1f, phi, 0);             //4
		coordinates[i++] = new Point3f(-1f, phi, 0);
		coordinates[i++] = new Point3f(1f, -1 * phi, 0);
		coordinates[i++] = new Point3f(-1f, -1 * phi, 0);
		// X / Z Plane coordinates
		coordinates[i++] = new Point3f(phi, 0, 1f);            //8
		coordinates[i++] = new Point3f(phi, 0, -1f);
		coordinates[i++] = new Point3f(-1 * phi, 0, 1f);
		coordinates[i++] = new Point3f(-1 * phi, 0, -1f);
		
		// Scaling
		for (int it = 0; it < coordinates.length; it++) {
			coordinates[it].scale(Math.min(DEFAULT_SCALE, scale));
		}
		
		GeometryArray die = new TriangleArray(((NUM_VERTEX / 2) - 1)
				* coordinates.length, GeometryArray.COORDINATES);
		int index = 0;
		
		
		//Builds triangles
		die.setCoordinate(index++, coordinates[0]);
		die.setCoordinate(index++, coordinates[8]);
		die.setCoordinate(index++, coordinates[4]);
		
		die.setCoordinate(index++, coordinates[0]);
		die.setCoordinate(index++, coordinates[4]);
		die.setCoordinate(index++, coordinates[5]);
		
		die.setCoordinate(index++, coordinates[0]);
		die.setCoordinate(index++, coordinates[5]);
		die.setCoordinate(index++, coordinates[10]);
		
		die.setCoordinate(index++, coordinates[0]);
		die.setCoordinate(index++, coordinates[10]);
		die.setCoordinate(index++, coordinates[3]);
		
		die.setCoordinate(index++, coordinates[0]);
		die.setCoordinate(index++, coordinates[3]);
		die.setCoordinate(index++, coordinates[8]);
		
		die.setCoordinate(index++, coordinates[8]);
		die.setCoordinate(index++, coordinates[9]);
		die.setCoordinate(index++, coordinates[4]);
		
		die.setCoordinate(index++, coordinates[4]);
		die.setCoordinate(index++, coordinates[9]);
		die.setCoordinate(index++, coordinates[1]);
		
		die.setCoordinate(index++, coordinates[4]);
		die.setCoordinate(index++, coordinates[1]);
		die.setCoordinate(index++, coordinates[5]);
		
		die.setCoordinate(index++, coordinates[5]);
		die.setCoordinate(index++, coordinates[1]);
		die.setCoordinate(index++, coordinates[11]);
		
		die.setCoordinate(index++, coordinates[5]);
		die.setCoordinate(index++, coordinates[11]);
		die.setCoordinate(index++, coordinates[10]);
		
		die.setCoordinate(index++, coordinates[10]);
		die.setCoordinate(index++, coordinates[11]);
		die.setCoordinate(index++, coordinates[7]);
		
		die.setCoordinate(index++, coordinates[10]);
		die.setCoordinate(index++, coordinates[7]);
		die.setCoordinate(index++, coordinates[3]);
		
		die.setCoordinate(index++, coordinates[3]);
		die.setCoordinate(index++, coordinates[7]);
		die.setCoordinate(index++, coordinates[6]);
		
		die.setCoordinate(index++, coordinates[3]);
		die.setCoordinate(index++, coordinates[6]);
		die.setCoordinate(index++, coordinates[8]);
		
		die.setCoordinate(index++, coordinates[8]);
		die.setCoordinate(index++, coordinates[6]);
		die.setCoordinate(index++, coordinates[9]);
		
		die.setCoordinate(index++, coordinates[9]);
		die.setCoordinate(index++, coordinates[2]);
		die.setCoordinate(index++, coordinates[1]);
		
		die.setCoordinate(index++, coordinates[1]);
		die.setCoordinate(index++, coordinates[2]);
		die.setCoordinate(index++, coordinates[11]);
		
		die.setCoordinate(index++, coordinates[11]);
		die.setCoordinate(index++, coordinates[2]);
		die.setCoordinate(index++, coordinates[7]);
		
		die.setCoordinate(index++, coordinates[7]);
		die.setCoordinate(index++, coordinates[2]);
		die.setCoordinate(index++, coordinates[6]);
		
		die.setCoordinate(index++, coordinates[6]);
		die.setCoordinate(index++, coordinates[2]);
		die.setCoordinate(index++, coordinates[9]);
		
		TransformGroup trans = new TransformGroup();
		NormalGenerator norms = new NormalGenerator(MAX_ANGLE);
		GeometryInfo geo = new GeometryInfo(die);
		norms.generateNormals(geo);
		
		Shape3D shape = new Shape3D(geo.getGeometryArray());
		Appearance meshApp = new Appearance();
		Material surface = new Material();
		surface.setDiffuseColor(myColor);
		meshApp.setMaterial(surface);
		meshApp.setColoringAttributes(new ColoringAttributes(myColor,
				ColoringAttributes.NICEST));
		shape.setAppearance(meshApp);
		
		return shape;
	}
}
