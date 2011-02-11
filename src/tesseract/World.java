package tesseract;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.media.j3d.BoundingBox;
import javax.media.j3d.BoundingLeaf;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.IndexedLineArray;
import javax.media.j3d.Light;
import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import tesseract.forces.Force;
import tesseract.objects.Collidable;
import tesseract.objects.CollisionInfo;
import tesseract.objects.Forceable;
import tesseract.objects.PhysicalObject;

import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.picking.behaviors.PickTranslateBehavior;
import com.sun.j3d.utils.picking.behaviors.PickZoomBehavior;

/**
 * Model of the 3D world.
 * 
 * @author Jesse Morgan
 */
public class World {
	/**
	 * Root element of the world.
	 */
	private BranchGroup myScene;
	
	/**
	 * Pickable Objects.
	 */
	private BranchGroup myPickableObjects;
	
	/**
	 * Bounding box of the world.
	 */
	private BoundingBox myVirtualWorldBounds;
	
	/**
	 * A list of the objects in the world. 
	 */
	private List<PhysicalObject> myObjects;
	
	/**
	 * A list of the forces in the world.
	 */
	private List<Force> myForces;
	
	/**
	 * Objects that can be collided into, a subset of myObjects.
	 */
	private List<Collidable> myCollidables;
	
	//private List<ParticleEmitter> emitters;
	//private boolean enableEmitters;
	
	// A list of all the particles in the world
	//private List<Particle> particles;
	
	// A list of all the objects particles may collide with
	//private List<ParticleCollidableObject> collidables;
	
	// Available forces
	//private static final ParticleForceGenerator forces[] = {new Gravity(0.4f)};
	//private boolean activeForces[];
	
	/**
	 * Update rate for the world.
	 */
	private static final int UPDATE_RATE = 30;
	
	/**
	 * Create a new world.
	 * 
	 * @param bounds The bounding box of the world.
	 */
	public World(final BoundingBox bounds) {
		myVirtualWorldBounds = bounds;

		myForces = new LinkedList<Force>();
		myObjects = new LinkedList<PhysicalObject>();
		myCollidables = new LinkedList<Collidable>();
		
		// TODO: Should this go here?
		myScene = new BranchGroup();
		myScene.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		myScene.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		
		BoundingLeaf originLeaf = new BoundingLeaf(new BoundingSphere());
		myScene.addChild(originLeaf);
		
		myScene.addChild(createVirtualWorldBoundsShape());
		
		myPickableObjects = new BranchGroup();
		myPickableObjects.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		myPickableObjects.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		//myScene.addChild(myPickableObjects);
		
		addLights();
		
		myScene.compile();
	}

	/**
	 * Create the visual bounding box around the world.
	 * 
	 * @return A shape of the bounding box.
	 */
	private Node createVirtualWorldBoundsShape() {
		Point3d lower = new Point3d();
		Point3d upper = new Point3d();
		myVirtualWorldBounds.getLower(lower);
		myVirtualWorldBounds.getUpper(upper);
		
		double[] coordinates = { lower.x, lower.y, lower.z, upper.x, lower.y,
				lower.z, upper.x, lower.y, upper.z, lower.x, lower.y, upper.z,
				lower.x, upper.y, lower.z, upper.x, upper.y, lower.z, upper.x,
				upper.y, upper.z, lower.x, upper.y, upper.z };
		
		int[] coordinateIndices = { 0, 1, 1, 2, 2, 3, 3, 0, 4, 5, 5, 6, 6, 7,
				7, 4, 0, 4, 1, 5, 2, 6, 3, 7 };
		
		IndexedLineArray geometry = new IndexedLineArray(
				coordinates.length / 3, IndexedLineArray.COORDINATES,
				coordinateIndices.length);
		
		geometry.setCoordinates(0, coordinates);
		geometry.setCoordinateIndices(0, coordinateIndices);
		
		return new Shape3D(geometry);
	}

	/**
	 * Add some standard lights to the world.
	 */
	private void addLights() {
		Light light = new DirectionalLight(
				new Color3f(1f, 1f, 1f), new Vector3f(-1f, -1f, -1f));
		
		light.setInfluencingBounds(
				new BoundingSphere(new Point3d(0, 0, 0), 10));
		
		myScene.addChild(light);
		
		light = new DirectionalLight(
				new Color3f(0.3f, 0.1f, 0.1f), new Vector3f(1f, 0f, 0f));
		
		light.setInfluencingBounds(
				new BoundingSphere(new Point3d(0, 0, 0), 10));
		
		myScene.addChild(light);
	}
	
	/**
	 * Update the state of the world.
	 */
	public void tick() {
		// Iterate over objects in the world.
		Iterator<PhysicalObject> itr = myObjects.iterator();
	
		List<PhysicalObject> children = new LinkedList<PhysicalObject>();
		
		while (itr.hasNext()) {
			PhysicalObject obj = itr.next();
			
			// If the object is affected by forces...
			if (obj instanceof Forceable) {
				// Apply all forces.
				for (Force force : myForces) {
					force.applyForceTo((Forceable) obj);
				}
			}
			
			// Update the object's state.
			List<PhysicalObject> newChildren 
				= obj.updateState(1f / UPDATE_RATE);
			
			if (newChildren != null) {
				children.addAll(newChildren);
			}
			
			// Check for collisions
			for (Collidable collidable : myCollidables) {
				if (collidable.hasCollision(obj)) {
					// Resolve the collision
					CollisionInfo ci = collidable.calculateCollision(obj);
					collidable.resolveCollision(obj, ci);
				}
			}

			// If it leaves the bounds of the world, DESTROY IT
			if (!obj.isExisting()
					|| !myVirtualWorldBounds.intersect(
							new Point3d(obj.getPosition()))
			) {
				obj.detach();
				itr.remove();
			}
		}

		// Add new children to thr world.
		for (PhysicalObject obj : children) {
			myScene.addChild(obj.getGroup());
		}
		
		myObjects.addAll(children);
	}
	
	/**
	 * @return the root BG of the scene.
	 */
	public BranchGroup getScene() {
		return myScene;
	}
	
	/**
	 * Setup mouse behaviors in the world.
	 *
	 * @param canvas The canvas to tie the events to.
	 */
	public void setupMouseBehaviors(final Canvas3D canvas) {
		//BranchGroup pickables = new BranchGroup();
		myPickableObjects.addChild(new PickTranslateBehavior(myPickableObjects, canvas, myVirtualWorldBounds, PickTool.GEOMETRY));
		myPickableObjects.addChild(new PickZoomBehavior(myPickableObjects, canvas, myVirtualWorldBounds, PickTool.GEOMETRY));
		myScene.addChild(myPickableObjects);
		
		/*myPickableObjects.addChild(
				new PickTranslateBehavior(myPickableObjects, canvas,
						myVirtualWorldBounds, PickTool.GEOMETRY));
		
		myPickableObjects.addChild(
				new PickZoomBehavior(myPickableObjects, canvas,
						myVirtualWorldBounds, PickTool.GEOMETRY));
		*/
	}
	
	/**
	 * Add a new object to the world.
	 * 
	 * @param obj The object to add
	 */
	public void addObject(final PhysicalObject obj) {
		myPickableObjects.addChild(obj.getGroup());
		myObjects.add(obj);
		
		if (obj instanceof Collidable) {
			myCollidables.add((Collidable) obj);
		}
	}
	
	/**
	 * Add a new force to the world.
	 * 
	 * @param force the force to add.
	 */
	public void addForce(final Force force) {
		myForces.add(force);
	}
}	
