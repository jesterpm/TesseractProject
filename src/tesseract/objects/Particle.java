package tesseract.objects;

import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Node;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Sphere;

/**
 * A particle object.
 * 
 * @author Jesse Morgan
 */
public class Particle extends ForceableObject {
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
		
		getTransformGroup().addChild(createShape(color));
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
	private Node createShape(final Color3f theColor) {
		Color3f color = theColor;
		
		ColoringAttributes cAttr;
		
		if (color == null) {
			Color randomColor = Color.getHSBColor((float) Math.random(), 1, 1);
			color = new Color3f(randomColor);
		}
		
		cAttr = new ColoringAttributes(color, ColoringAttributes.FASTEST);
		Appearance appearance = new Appearance();
		appearance.setColoringAttributes(cAttr);
		return new Sphere(RADIUS, Sphere.ENABLE_GEOMETRY_PICKING,
				DIVISIONS, appearance);
	}
}
