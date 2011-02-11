package alden;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.picking.*;
import com.sun.j3d.utils.picking.behaviors.*;
import com.sun.j3d.utils.universe.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.media.j3d.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.vecmath.*;

@SuppressWarnings("restriction")
public class CollisionDetectorDemo {
	private JFrame appFrame;
	private Canvas3D canvas3D;
	private BranchGroup scene;
	private BoundingLeaf originLeaf;
	// Bounding box defining the periphery of the virtual world.
	private BoundingBox virtualWorldBounds;
	// Data needed for adjusting the camera position.
	private TransformGroup cameraTG;
	private double cameraXRotation, cameraYRotation, cameraDistance;
	private MouseEvent lastDragEvent;
	// A list of all the objects in the world
	private List<CollidableObject> objects;
	// A list of all the visual collision points
	private List<BranchGroup> collisionPoints;
	// Number of state updates per second
	private final int UPDATE_RATE = 30;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CollisionDetectorDemo().createAndShowGUI();
			}
		});
	}

	private CollisionDetectorDemo() {
		final double UNIT = 1f;
		virtualWorldBounds = new BoundingBox(new Point3d(-UNIT/2, -UNIT/2, -UNIT/2), new Point3d(UNIT/2, UNIT/2, UNIT/2));
		cameraDistance = 3 * UNIT;
		objects = new ArrayList<CollidableObject>();
		collisionPoints = new ArrayList<BranchGroup>();
	}

	private void createAndShowGUI() {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		canvas3D = new Canvas3D(config);
		SimpleUniverse universe = new SimpleUniverse(canvas3D);
		cameraTG = universe.getViewingPlatform().getViewPlatformTransform();
		updateCamera();
		universe.getViewer().getView().setSceneAntialiasingEnable(true);
		
		scene = new BranchGroup();
		scene.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		scene.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		originLeaf = new BoundingLeaf(new BoundingSphere());
		scene.addChild(originLeaf);
		scene.addChild(createVirtualWorldBoundsShape());
		addObjects();
		scene.compile();
		universe.addBranchGraph(scene);

		appFrame = new JFrame("Collision Detector Demo");
		appFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		appFrame.add(canvas3D);
		appFrame.pack();
		if (Toolkit.getDefaultToolkit().isFrameStateSupported(JFrame.MAXIMIZED_BOTH))
			appFrame.setExtendedState(appFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		
		canvas3D.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == 0)
					return;
				if (lastDragEvent != null) {
					cameraXRotation += Math.toRadians(e.getY() - lastDragEvent.getY()) / 3;
					if (cameraXRotation > Math.PI / 2)
						cameraXRotation = Math.PI / 2;
					else if (cameraXRotation < -Math.PI / 2)
						cameraXRotation = -Math.PI / 2;
					cameraYRotation += Math.toRadians(e.getX() - lastDragEvent.getX()) / 3;
					updateCamera();
				}
				lastDragEvent = e;
			}
			public void mouseMoved(MouseEvent e) {
				lastDragEvent = null;
			}});
		canvas3D.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation() > 0)
					cameraDistance *= 1.05;
				else if (e.getWheelRotation() < 0)
					cameraDistance *= 0.95;
				updateCamera();
			}
		});
 		new Timer(1000 / UPDATE_RATE, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canvas3D.stopRenderer();
				tick();
				canvas3D.startRenderer();
			}
		}).start();
		
		appFrame.setVisible(true);
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

	private void addObjects() {
		BranchGroup pickables = new BranchGroup();
		pickables.addChild(new PickTranslateBehavior(pickables, canvas3D, virtualWorldBounds, PickTool.GEOMETRY));
		pickables.addChild(new PickZoomBehavior(pickables, canvas3D, virtualWorldBounds, PickTool.GEOMETRY));
		scene.addChild(pickables);
		
		Appearance appearance = new Appearance();
		appearance.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.NICEST, 0.2f));
		appearance.setColoringAttributes(new ColoringAttributes(1, 0.7f, 0.7f, ColoringAttributes.FASTEST));
		Primitive prim = new com.sun.j3d.utils.geometry.Sphere(0.2f, 0, 15, appearance); 
		objects.add(new GenericCollidableObject(prim, new Vector3f(0, 0, 0.3f), originLeaf));
		
		appearance = new Appearance();
		appearance.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.NICEST, 0.2f));
		appearance.setColoringAttributes(new ColoringAttributes(0.7f, 1, 0.7f, ColoringAttributes.FASTEST));
		prim = new Box(0.2f, 0.15f, 0.1f, 0, appearance);
		objects.add(new GenericCollidableObject(prim, new Vector3f(0.3f, 0, 0), originLeaf));
		
		appearance = new Appearance();
		appearance.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.NICEST, 0.2f));
		appearance.setColoringAttributes(new ColoringAttributes(0.7f, 0.7f, 1, ColoringAttributes.FASTEST));
		prim = new Cylinder(0.2f, 0.4f, 0, 15, 1, appearance);
		objects.add(new GenericCollidableObject(prim, new Vector3f(0, 0, -0.3f), originLeaf));
		
		appearance = new Appearance();
		appearance.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.NICEST, 0.2f));
		appearance.setColoringAttributes(new ColoringAttributes(1, 1, 0.7f, ColoringAttributes.FASTEST));
		prim = new com.sun.j3d.utils.geometry.Cone(0.15f, 0.3f, 0, 15, 1, appearance);
		objects.add(new GenericCollidableObject(prim, new Vector3f(-0.3f, 0, 0), originLeaf));
		
		for (CollidableObject object : objects)
			pickables.addChild(object.getGroup());
	}

	private void tick() {
		for (BranchGroup BG : collisionPoints)
			BG.detach();
		collisionPoints.clear();
		
		for (int i = 0; i < objects.size() - 1; i++)
			for (int j = i + 1; j < objects.size(); j++) {
				ArrayList<CollisionInfo> collisions = CollisionDetector.calculateCollisions(objects.get(i), objects.get(j));
				Appearance appearance = new Appearance();
				ColoringAttributes cAttr = new ColoringAttributes(new Color3f(1, 0, 0), ColoringAttributes.FASTEST);
				appearance.setColoringAttributes(cAttr);
				for (CollisionInfo ci : collisions) {
					Transform3D tmp = new Transform3D();
					tmp.setTranslation(ci.contactPoint);
					TransformGroup TG = new TransformGroup(tmp);
					TG.addChild(new com.sun.j3d.utils.geometry.Sphere(0.002f, 0, 8, appearance));
					BranchGroup BG = new BranchGroup();
					BG.setCapability(BranchGroup.ALLOW_DETACH);
					BG.addChild(TG);
					collisionPoints.add(BG);
					scene.addChild(BG);
				}
			}
	}
	
	private void updateCamera() {
		Transform3D camera3D = new Transform3D();
		camera3D.setTranslation(new Vector3f(0f, 0f, -(float)cameraDistance));
		Transform3D tmp = new Transform3D();
		tmp.rotX(cameraXRotation);
		camera3D.mul(tmp);
		tmp.rotY(cameraYRotation);
		camera3D.mul(tmp);
		camera3D.invert();
		cameraTG.setTransform(camera3D);
	}

	private static class CollisionUpdateBehavior extends Behavior {
		private GenericCollidableObject gco;
		private TransformGroup TG;
		
		public CollisionUpdateBehavior(GenericCollidableObject gco, TransformGroup TG, BoundingLeaf boundingLeaf) {
			this.gco = gco;
			this.TG = TG;
			setSchedulingBoundingLeaf(boundingLeaf);
		}

		public void initialize() {
			wakeupOn(new WakeupOnTransformChange(TG));
		}

		public void processStimulus(Enumeration e) {
			gco.clearCaches();
			wakeupOn(new WakeupOnTransformChange(TG));
		}		
	}

	private static class GenericCollidableObject extends CollidableObject {
		public GenericCollidableObject(Node shapeNode, Vector3f position, BoundingLeaf boundingLeaf) {
			setShape(shapeNode);
			this.position.set(position);
			TG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
			TG.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
			TG.addChild(CollisionDetector.createShape(CollisionDetector.triangularize(shapeNode)));
			BG.addChild(new CollisionUpdateBehavior(this, TG, boundingLeaf));
			updateTransformGroup();
		}
	}
}	
