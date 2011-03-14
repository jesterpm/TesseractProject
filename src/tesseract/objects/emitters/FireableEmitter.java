package tesseract.objects.emitters;

import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import tesseract.objects.Particle;
import tesseract.objects.PhysicalObject;

public class FireableEmitter extends PhysicalObject {
	Color3f myColor;
	
	public FireableEmitter(Vector3f position, Vector3f direction, Color3f color) {
		super(position, Float.POSITIVE_INFINITY);
		myColor = color;
	}
	
	public void moveMe (Vector3f newPosition) {
		position = newPosition;
	}
	
	/**
	 * Update State and maybe generate a new object.
	 * 
	 * @param duration The length of time that has passed.
	 * @return A list of new objects to add to the world.
	 */
	public List<PhysicalObject> spawnChildren() {
		List<PhysicalObject> children = super.spawnChildren(0f);
		
		if (children == null) {
			children = new LinkedList<PhysicalObject>();
		}
		
		children.add(new Particle(this.position, myColor));
		
		return children;
	}

}
