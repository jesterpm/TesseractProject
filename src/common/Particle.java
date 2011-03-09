package common;

import com.sun.j3d.utils.geometry.Sphere;
import java.awt.*;
import javax.media.j3d.*;
import javax.vecmath.*;

@SuppressWarnings("restriction")
public class Particle extends CollidableObject {
	protected static final float RADIUS = 0.04f;
	
	public Particle(Color3f color, Vector3f position, Vector3f velocity) {
		this(1, color, position, velocity);
	}
	
	public Particle(float mass, Color3f color, Vector3f position, Vector3f velocity) {
		super(mass);
		setShape(createShape(color));
		this.position.set(position);
		this.velocity = new Vector3f(velocity);
		updateTransformGroup();
	}
	
	private Node createShape(Color3f color) {
		if (color == null)
			color = new Color3f(Color.getHSBColor((float)Math.random(), 1, 1));
		Appearance appearance = new Appearance();
		appearance.setColoringAttributes(new ColoringAttributes(color, ColoringAttributes.FASTEST));
		return new Sphere(RADIUS, 0, 8, appearance);
	}
}
