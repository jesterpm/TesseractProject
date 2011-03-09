package tesseract.generators;

import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Primitive;

import tesseract.World;
import tesseract.objects.Box;
import tesseract.objects.PhysicalObject;
import tesseract.objects.Sphere;

/**
 * Generate a sphere field.
 * 
 * @author Phillip Cardon
 */
public class BoxField extends MenuItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int FIELD_SIZE = 4;
	
	private static final float BOX_SIZE = 0.025f;
	
	public BoxField(World theWorld) {
		super("Cube Field", theWorld);
	}

	/**
	 * Generate the field.
	 * 
	 * @param theWorld Where to put them
	 */
	public void generate(final World theWorld) {
		final float start = 1.01f * 0.5f * FIELD_SIZE * BOX_SIZE; 
		//int num = 0;
		//Box box = new Box(SPHERE_SIZE, SPHERE_SIZE, SPHERE_SIZE, new Vector3f(x, y, z));
		//PhysicalObject s = new Sphere(SPHERE_SIZE, new Vector3f(0f, 0f, 0f));
		
		for (float x = -start; x <= +start; x += BOX_SIZE * 1.01f) {
			for (float y = -start; y <= +start; y += BOX_SIZE * 1.01f) {
				for (float z = -start; z <= +start; z += BOX_SIZE * 1.01f) {
					//PhysicalObject s = new Sphere(SPHERE_SIZE, new Vector3f(x, y, z));
					//theWorld.addObject(s);
					//Sphere s = new Sphere(SPHERE_SIZE, new Vector3f(x, y, z));
					Box box = new Box(BOX_SIZE, BOX_SIZE, BOX_SIZE, new Vector3f(x, y, z));
					theWorld.addObject(box);
					//num++;
					//break;
				}
				//break;
			}
			//break;
		}//*/
		//System.out.println(num + " Spheres added");
	}
}
