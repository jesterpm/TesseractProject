package tesseract;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.media.j3d.BoundingBox;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.IndexedLineArray;
import javax.media.j3d.Light;
import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

public class World {
	/**
	 * Root element of the world.
	 */
	private BranchGroup myScene;
	
	/**
	 * Bounding box of the world.
	 */
	private BoundingBox myVirtualWorldBounds;
	
	/**
	 * A list of the objects in the world. 
	 */
	private List<Particle> myObjects;
	
	/**
	 * A list of the forces in the world.
	 */
	private List<Force> myForces;
	
	//private List<ParticleEmitter> emitters;
	//private boolean enableEmitters;
	
	// A list of all the particles in the world
	//private List<Particle> particles;
	
	// A list of all the objects particles may collide with
	//private List<ParticleCollidableObject> collidables;
	
	// Available forces
	//private static final ParticleForceGenerator forces[] = {new Gravity(0.4f)};
	//private boolean activeForces[];
	
	// Number of state updates per second
	//private final int UPDATE_RATE = 30;
	
	public World(final BoundingBox bounds) {
		myVirtualWorldBounds = bounds;

		myForces = new LinkedList<Force>();
		myObjects = new LinkedList<Particle>();
		
		// TODO: Should this go here?
		myScene = new BranchGroup();
		myScene.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		myScene.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		myScene.addChild(createVirtualWorldBoundsShape());
		addLights();
		addEmitters();
		addCollidableObjects();
		myScene.compile();
	}

	

	private Node createVirtualWorldBoundsShape() {
		Point3d lower = new Point3d();
		Point3d upper = new Point3d();
		virtualWorldBounds.getLower(lower);
		virtualWorldBounds.getUpper(upper);
		
		double coordinates[] = {lower.x, lower.y, lower.z, upper.x, lower.y, lower.z,
		                       upper.x, lower.y, upper.z, lower.x, lower.y, upper.z,
		                       lower.x, upper.y, lower.z, upper.x, upper.y, lower.z,
		                       upper.x, upper.y, upper.z, lower.x, upper.y, upper.z};
		int coordinateIndices[] = {0, 1, 1, 2, 2, 3, 3, 0,
		                           4, 5, 5, 6, 6, 7, 7, 4,
		                           0, 4, 1, 5, 2, 6, 3, 7}; 
		
		IndexedLineArray geometry = new IndexedLineArray(coordinates.length / 3, IndexedLineArray.COORDINATES, coordinateIndices.length);
		geometry.setCoordinates(0, coordinates);
		geometry.setCoordinateIndices(0, coordinateIndices);
		
		return new Shape3D(geometry);
	}

	private void addLights() {
		Light light = new DirectionalLight(new Color3f(1f, 1f, 1f), new Vector3f(-1f, -1f, -1f));
		light.setInfluencingBounds(new BoundingSphere(new Point3d(0, 0, 0), 10));
		scene.addChild(light);
		light = new DirectionalLight(new Color3f(0.3f, 0.1f, 0.1f), new Vector3f(1f, 0f, 0f));
		light.setInfluencingBounds(new BoundingSphere(new Point3d(0, 0, 0), 10));
		scene.addChild(light);
	}
	
	private void addEmitters() {
		emitters.add(new ColorShiftParticleEmitter(new Vector3f(-0.2f, 0.5f, 0)));
	}

	private void addCollidableObjects() {
		collidables.add(new Circle(0.25f, new Vector3f(-0.2f, 0.3f, 0), new Vector3f(0.6f, 1, 0)));
		collidables.add(new Circle(0.25f, new Vector3f(0.2f, 0.1f, 0), new Vector3f(-0.6f, 1, 0)));
		collidables.add(new Circle(0.25f, new Vector3f(-0.2f, -0.1f, 0), new Vector3f(0.6f, 1, 0)));
		collidables.add(new Circle(0.25f, new Vector3f(0.2f, -0.3f, 0), new Vector3f(-0.6f, 1, 0)));
		
//		collidables.add(new Circle(0.25f, new Vector3f(0.15f, 0, 0), new Vector3f(0.02f, 1, 0)));
//		collidables.add(new MathMesh(new Vector3f(0.13f, 0, 0), 151));
		for (ParticleCollidableObject object : collidables)
			scene.addChild(object.getGroup());
	}

	public void tick() {
		for (Iterator<Particle> itr = particles.iterator(); itr.hasNext();) {
			Particle particle = itr.next();
			for (int i = 0; i < forces.length; i++)
				if (activeForces[i])
					forces[i].applyForceTo(particle);
			particle.updateState(1f / UPDATE_RATE);
			for (ParticleCollidableObject object : collidables) {
				CollisionInfo ci = object.calculateCollision(particle);
				if (ci != null)
					object.resolveCollision(particle, ci);
			}
			if (!virtualWorldBounds.intersect(new Point3d(particle.getPosition()))) {
				particle.detach();
				itr.remove();
			}
		}
		if (!enableEmitters)
			return;
		for (ParticleEmitter emitter : emitters) {
			List<Particle> children = emitter.tick();
			for (Particle particle : children)
				scene.addChild(particle.getGroup());
			particles.addAll(children);
		}
	}
	
	/**
	 * @return the root BG of the scene.
	 */
	public BranchGroup getScene() {
		return myScene;
	}
}	
