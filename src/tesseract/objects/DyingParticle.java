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
 * A dying particle object.
 * 
 * Particle.java used as code base, parts also taken from ModifyableParticle.
 * 
 * @author Phillip Cardon
 * @author Jesse Morgan
 */
public class DyingParticle extends PhysicalObject {
	/**
	 * Rendered radius of particle.
	 */
	private static final float RADIUS = .001f;
	
	/**
	 * Default mass.
	 */
	private static final float DEFAULT_MASS = .1f;
	
	/**
	 * Number of divisions in the sphere.
	 */
	private static final int DIVISIONS = 8;
	private static final int DEFAULT_LIFE = 6; 
	private TransformGroup myTop;
	private TransformGroup myBottom;

	private Shape3D myShape;

	private Color3f myColor;

	private int myLife;
	
	/**
	 * Create a new Particle.
	 * 
	 * @param position Initial position.
	 * @param color Initial color. Null for random.
	 */
	public DyingParticle(final Vector3f position, final float mass, final Color3f color, final TransformGroup top, 
			final TransformGroup bottom) {
		super(position, mass);
		myTop = top;
		myBottom = bottom;
		myBottom.addChild(createShape(color));
		myColor = color;
		setShape(myTop);
		myLife = DEFAULT_LIFE;
	}
	
	/**
	 * Create a new particle of the give color.
	 * 
	 * @param theColor The particle color or null for random.
	 * @return A sphere to visually represent the particle.
	 */
	private Shape3D createShape(final Color3f theColor) {
		
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
		myShape = shape;
		myShape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		return shape;
	}
	
	public boolean isDead() {
		return myLife <= 0;
	}
	
	public void updateState(float duration) {
		if (!isDead()) {
			myLife--;
			super.updateState(duration);
		} else {
			super.updateState(duration);
			this.detach();
		}
			
	}
	
	public void setAcceleration(Vector3f accelerator) {
		this.velocity = accelerator;		
	}
}
