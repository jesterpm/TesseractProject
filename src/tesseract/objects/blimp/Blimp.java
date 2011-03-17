package tesseract.objects.blimp;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Group;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;

import tesseract.objects.ModifyableParticle;
import tesseract.objects.PhysicalObject;
import tesseract.objects.remote.RemoteObject;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;

public class Blimp extends RemoteObject {

	KeyEvent lastEvent;
	/**
	 * Default mass.
	 */
	//private static final float DEFAULT_MASS = Float.POSITIVE_INFINITY;
	private static final float DEFAULT_MASS = 10;
	private final int MAX_TURN = 32;
	private final float MAX_SPEED = .3f;
	private Vector3f[] vectors;
	private TransformGroup my_blimp;
	
	/**
	 * Use to scale all object together
	 */
	private float my_scale;
	
	/**
	 * ellipsoid of blimp radius
	 */
	private float my_radius;
	
	/**
	 * Create a new Blimp.
	 * 
	 * @param position Initial position.
	 * @param scale determine the size of the blimp
	 * @param theColor of object.
	 */
	public Blimp(final Vector3f position, final float scale) {
		super(position, DEFAULT_MASS);
		
		my_scale = scale;
		
		my_radius = .08f * my_scale;
		
		final float rSq = my_radius * my_radius;
		final float a = 1.0f;
		final float b = 1.0f;
		final float c = 2.8f;
		
		setShape(create(a, b, c));
		
		if (inverseMass != 0) {
			inverseInertiaTensor.m00 = 1f / 5 / inverseMass * (b * rSq + c * rSq);
			inverseInertiaTensor.m11 = 1f / 5 / inverseMass * (a * rSq + c * rSq);
			inverseInertiaTensor.m22 = 1f / 5 / inverseMass * (a * rSq + b * rSq);
			inverseInertiaTensor.invert();
		}
		updateTransformGroup();
	}
	
	/**
	 * creates the shapes in the blimp
	 * @param a float in the ellipsoid formula.
	 * @param b float in the ellipsoid formula.
	 * @param c float in the ellipsoid formula.
	 * @return TransformGroup with the shape.
	 */
	private TransformGroup create( final float a,
			final float b, final float c) {
		
		my_blimp = new TransformGroup();
		my_blimp.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		my_blimp.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		
		//blimp node
		Appearance b_appearance = new Appearance();
		Material surface = new Material();
		surface.setDiffuseColor(new Color3f(.5f, .6f, .6f));
		b_appearance.setMaterial(surface);
		
		Sphere sphere = new Sphere(my_radius,
				new Sphere().getPrimitiveFlags() | Sphere.ENABLE_GEOMETRY_PICKING,
				30, b_appearance );
		Transform3D tmp = new Transform3D();
		tmp.set(new Matrix3f(a, 0.0f, 0.0f, 0.0f, b, 0.0f, 0.0f, 0.0f, c));
		TransformGroup tgBlimp = new TransformGroup(tmp);
		tgBlimp.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tgBlimp.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		tgBlimp.addChild(sphere);
		
		//box node
		Appearance boxApp = new Appearance();
		Material box_surface = new Material();
		box_surface.setDiffuseColor(new Color3f(.7f, .6f, .4f));
		boxApp.setMaterial(box_surface);
		BlimpBox box = new BlimpBox(10, .05f * my_scale, .03f * my_scale, .1f * my_scale,
				new Vector3f(0f * my_scale, -.08f * my_scale, 0f * my_scale), boxApp);
		TransformGroup tgBox = new TransformGroup();
		tgBox.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tgBox.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		tgBox.addChild(box.getGroup());
		
		//This is done quite right, but it gets the front.
		Primitive boxOfBlimpBox = box.getBoxShape();
		Shape3D front = boxOfBlimpBox.getShape(Box.FRONT); //Gets the orientation for keys
		//
		Geometry g = front.getGeometry(0);
		GeometryInfo gi = new GeometryInfo((GeometryArray)g);
		vectors = gi.getNormals();
		
		//fin1
		TextureLoader tl = new TextureLoader("lava.jpg", null);
		ImageComponent2D image = tl.getImage();
		int width = image.getWidth();
		int height = image.getHeight();
		Texture2D texture = new Texture2D(Texture.MULTI_LEVEL_MIPMAP, Texture.RGB, width, height);
		
		int imageLevel = 0;
		texture.setImage(imageLevel, image);
		while (width > 1 || height > 1) {
			imageLevel++;
			if (width > 1) width /= 2;
			if (height > 1) height /= 2;
			texture.setImage(imageLevel, tl.getScaledImage(width, height));
		}
		texture.setMagFilter(Texture2D.NICEST);
		texture.setMinFilter(Texture2D.NICEST);
		Material mat = new Material();
		mat.setDiffuseColor(1, 0, 0);
		
		Appearance appearance = new Appearance();
		appearance.setTexture(texture);
		appearance.setMaterial(mat);
		
		
		BlimpFin fin = new BlimpFin(1, new Vector3f(0f * my_scale, 0f * my_scale,
				-.165f  * my_scale), .08f * my_scale, appearance);
		Transform3D rotate = new Transform3D();
		rotate.rotZ(Math.PI / 2);
		TransformGroup tgFin = new TransformGroup(rotate);
		tgFin.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tgFin.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		tgFin.addChild(fin.getGroup());
		
		//fin2
		BlimpFin fin2 = new BlimpFin(1, new Vector3f(0f * my_scale,
				0f * my_scale, -.165f * my_scale), .08f * my_scale, appearance);
		Transform3D rotate2 = new Transform3D();
		//rotate2.rotZ(Math.PI / 2);
		TransformGroup tgFin2 = new TransformGroup(rotate2);
		tgFin2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tgFin2.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		tgFin2.addChild(fin2.getGroup());
		
		//pole spike in front
		TransformGroup tgPole = createPole(new Color3f(.7f, .6f, .4f));
		Transform3D rotate3 = new Transform3D();
		rotate3.rotX(Math.PI / 2);
		tgPole.setTransform(rotate3);
		
		my_blimp.addChild(tgBlimp);
		my_blimp.addChild(tgBox);
		my_blimp.addChild(tgFin);
		my_blimp.addChild(tgFin2);
		my_blimp.addChild(tgPole);
		my_blimp.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		return my_blimp;
	}
	
	public Vector3f getFacing() {
		return vectors[0];
	}
	
	private TransformGroup createPole(Color3f color) {
		// Node managing the cone.
		TransformGroup cone = new TransformGroup();
		Appearance axisApp = new Appearance();
		Material axisMat = new Material();
		axisMat.setDiffuseColor(color);
		axisApp.setMaterial(axisMat);
		axisApp.setColoringAttributes(new ColoringAttributes(0f, 0f, 0f, ColoringAttributes.SHADE_GOURAUD));
		cone.addChild(new Cone(0.02f * my_scale, 0.1f * my_scale, new Cone().getPrimitiveFlags(), 80, 20, axisApp));
		Transform3D tmp = new Transform3D();
		tmp.setTranslation(new Vector3f(0f, .23f * my_scale, 0f));
		cone.setTransform(tmp);

		// Node managing the cylinder.
		TransformGroup cylinder = new TransformGroup();
		cylinder.addChild(new Cylinder(0.02f * my_scale, .3f * my_scale, new Cylinder().getPrimitiveFlags(), 80, 20, axisApp));
		tmp = new Transform3D();
		tmp.setTranslation(new Vector3f(0f, 0.075f * my_scale, 0f));
		cylinder.setTransform(tmp);

		// Node managing the entire axis.
		TransformGroup axis = new TransformGroup();
		axis.addChild(cone);
		axis.addChild(cylinder);
		
		return axis;
	}
	
	
	private static final long serialVersionUID = 4419863813052251438L;

	@Override
	public String getName() {
		return "Blimp";
	}
	
	/**
	 * This controls the blimp.  Most of it was written by Phillip Cardon for the Tank
	 * and modified to fit the blimp since it can move up or down
	 * 
	 * @author Phillip Cardon, Steve Bradshaw
	 */
	protected void keyEventReceived(final KeyEvent event) {
		lastEvent = event;
		Vector3f temp = new Vector3f();
		Transform3D currentOrientation = new Transform3D();
		my_blimp.getTransform(currentOrientation);
		Transform3D turnRight = new Transform3D();
		Transform3D turnLeft = new Transform3D();
		Vector3f facing = new Vector3f(vectors[0]);
		Transform3D faceTrans = new Transform3D();
		my_blimp.getTransform(faceTrans);
		faceTrans.transform(facing);
		switch (event.getKeyCode()) {
			case KeyEvent.VK_W:
				facing.scale(.01f);
				velocity.add(facing);
				break;
				
			case KeyEvent.VK_S:
				facing.scale(.01f);
				velocity.sub(facing);
				break;
				
			case KeyEvent.VK_A:
				
				turnLeft.rotY(Math.PI / 32);
				currentOrientation.mul(turnLeft);
				my_blimp.setTransform(currentOrientation);
				turnLeft.transform(velocity);
				//orientation.y += Math.PI / 32;
				
				//angularVelocity.y = 0;
				//orientation.normalize();
				//System.out.println(orientation.x+ ", " + orientation.y + " " + orientation.z);
				break;
				
			case KeyEvent.VK_D:
				
				turnRight.rotY(-Math.PI / 32);
				currentOrientation.mul(turnRight);
				my_blimp.setTransform(currentOrientation);
				turnRight.transform(velocity);
				//orientation.y -= Math.PI / 32;
				//angularVelocity.y = 0;
				//orientation.normalize();
				//System.out.println(orientation.x+ ", " + orientation.y + " " + orientation.z);
				break;
		}
	}

				
		//		break;

			//case KeyEvent.VK_SPACE:
				//spawnChildren(0f);
				//System.out.println("Tried to fire particle");
				//break;
		
/*		if (barrelTurn < -MAX_TURN) {
			barrelTurn = MAX_TURN - 1;
		} else if (barrelTurn > MAX_TURN) {
			barrelTurn = -MAX_TURN + 1;
		}
		
	}*/
	
	
	public void updateState(float duration) {
		float speed = velocity.length();
		//System.out.println(speed);
		//int i = 0;
		while(speed > MAX_SPEED) {
			velocity.scale(.99f);
			speed = velocity.length();
			//i++;
		}
		//System.out.println(i);
		
		super.updateState(duration);
	}
	
	
}
