package tesseract.generators;

import javax.vecmath.Vector3f;

import tesseract.World;
import tesseract.objects.Particle;
import tesseract.objects.Sphere;

/**
 * Generate a field of random particles.
 * 
 * @author jesse
 */
public class ParticleField extends MenuItem {

	private static final int FIELD_SIZE = 100;
	
	public ParticleField(World theWorld) {
		super("Particle Field", theWorld);
	}

	/**
	 * Generate the field.
	 * 
	 * @param theWorld Where to put them
	 */
	public void generate(final World theWorld) {
		for (int i = 0; i < FIELD_SIZE; i++) {
			Vector3f position = new Vector3f((float) Math.random() - 0.5f,
					(float) Math.random() - 0.5f, (float) Math.random() - 0.5f);
			
			Particle p = new Particle(position, null);
			
			theWorld.addObject(p);
		}
	}
}
