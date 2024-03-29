package tesseract.objects;

import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Sphere;

/**
 * A particle object.
 * 
 * @author Jesse Morgan
 */
public class Particle extends PhysicalObject {
	/**
	 * Rendered radius of particle.
	 */
	private static final float RADIUS = .004f;
	
	/**
	 * Default mass.
	 */
	private static final float DEFAULT_MASS = 1;
	
	/**
	 * Number of divisions in the sphere.
	 */
	private static final int DIVISIONS = 8;
	
	/**
	 * Create a new Particle.
	 * 
	 * @param position Initial position.
	 * @param mass Initial mass.
	 * @param color Initial color. Null for random.
	 */
	public Particle(final Vector3f position, final float mass,
			final Color3f color) {
		super(position, mass);
		
		setShape(createShape(color));
	}
	
	/**
	 * Create a new Particle.
	 * 
	 * @param position Initial position.
	 * @param color Initial color. Null for random.
	 */
	public Particle(final Vector3f position, final Color3f color) {
		this(position, DEFAULT_MASS, color);
	}
	
	/**
	 * Create a new particle of the give color.
	 * 
	 * @param theColor The particle color or null for random.
	 * @return A sphere to visually represent the particle.
	 */
	private TransformGroup createShape(final Color3f theColor) {
		
		Color3f color = theColor;
		
		ColoringAttributes cAttr;
		
		if (color == null) {
			Color randomColor = Color.getHSBColor((float) Math.random(), 1, 1);
			color = new Color3f(randomColor);
		}
		/*
		cAttr = new ColoringAttributes(color, ColoringAttributes.FASTEST);
		Appearance appearance = new Appearance();
		Material mat = new Material();
		mat.setAmbientColor(color);
		mat.setDiffuseColor(color);
		appearance.setMaterial(mat);
		appearance.setColoringAttributes(cAttr);
		
		Sphere sphere = new Sphere(RADIUS, Sphere.ENABLE_GEOMETRY_PICKING,
				DIVISIONS, appearance);
		*/
		
		Sphere sphere = new Sphere(RADIUS, Sphere.ENABLE_GEOMETRY_PICKING,
				DIVISIONS);
		Shape3D shape = sphere.getShape();
		sphere.removeAllChildren();
		Appearance meshApp = new Appearance();
		Material surface = new Material();
		surface.setDiffuseColor(color);
		meshApp.setMaterial(surface);
		meshApp.setColoringAttributes(new ColoringAttributes(color,
						ColoringAttributes.FASTEST));
		shape.setAppearance(meshApp);
		
		TransformGroup tg = new TransformGroup();
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tg.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		tg.addChild(shape);
		return tg;
	}
}
