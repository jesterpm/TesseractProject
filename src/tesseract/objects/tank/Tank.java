package tesseract.objects.tank;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import tesseract.objects.Particle;
import tesseract.objects.PhysicalObject;
import tesseract.objects.remote.RemoteObject;

public class Tank extends RemoteObject {
	
	private final TransformGroup whole;
	private final TransformGroup turret;
	//private final Vector3f orientation;
	KeyEvent lastEvent;
	private Vector3f aim;
	private final Point3f gunLocation;
	private static final float DEFAULT_SCALE = 0.0625f;
	private static final Color DEFAULT_BODY_COLOR = Color.GREEN;
	private static final Color DEFAULT_TRACK_COLOR = Color.DARK_GRAY;
	private static final Color DEFAULT_TURRET_COLOR = Color.GREEN;
	private final Body tank;
	private int barrelElevation = 0;
	private static final int maxBarrelElevation = 14;
	private static final int minBarrelElevation = -1;
	private final float myScale;
	private int barrelTurn = 0;
	private final int MAX_TURN = 32;
	
	public Tank(final Vector3f thePosition, final float mass) {
		this(thePosition, mass, DEFAULT_SCALE);
	}

	public Tank(final Vector3f thePosition, final float mass,
			final float theScale) {
		this(thePosition, mass, theScale, DEFAULT_BODY_COLOR,
				DEFAULT_TRACK_COLOR, DEFAULT_TURRET_COLOR);
	}

	public Tank(final Vector3f thePosition, final float mass,
			final float theScale, final Color bodyColor, final Color trackColor,
			final Color turretColor) {
		super (thePosition, mass);
		myScale = theScale;
		tank = new Body(trackColor, bodyColor, theScale, turretColor);
		//orientation = new Vector3f();
		aim = new Vector3f();
		gunLocation = new Point3f();
		Transform3D turretMove = new Transform3D();
		turretMove.setTranslation(new Vector3f(0, Body.height * theScale, 0));
		turret = new TransformGroup();
		turret.addChild(tank.getTurret());
		turret.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		whole = new TransformGroup();
		whole.addChild(tank.getBody());
		whole.addChild(turret);
		whole.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		setShape(whole);
		inverseInertiaTensor.m00 = 1f / 12 / inverseMass * (Body.height * Body.height * theScale + Body.depth * Body.depth * theScale);
		inverseInertiaTensor.m11 = 1f / 12 / inverseMass * (Body.width * Body.width * theScale + Body.depth * Body.depth * theScale);
		inverseInertiaTensor.m22 = 1f / 12 / inverseMass * (Body.width * Body.width * theScale + Body.height * Body.height * theScale);
		inverseInertiaTensor.invert();
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4419863813052251438L;

	@Override
	public String getName() {
		return "Tank";
	}
	
	protected void keyEventReceived(final KeyEvent event) {
		lastEvent = event;
		//Transform3D check = new Transform3D();
		//tank.getBody().getTransform(check);
		//check.get(orientation);
		Vector3f temp = new Vector3f();
		Transform3D current = new Transform3D();
		turret.getTransform(current);
		Transform3D currentOrientation = new Transform3D();
		whole.getTransform(currentOrientation);
		Transform3D turnRight = new Transform3D();
		Transform3D barrelDown = new Transform3D();
		Transform3D barrelUp = new Transform3D();
		Transform3D right = new Transform3D();
		Transform3D left = new Transform3D();
		Transform3D turnLeft = new Transform3D();
		switch (event.getKeyCode()) {
			case KeyEvent.VK_W:
				
				break;
				
			case KeyEvent.VK_S:
				
				break;
				
			case KeyEvent.VK_A:
				
				turnLeft.rotY(Math.PI / 32);
				currentOrientation.mul(turnLeft);
				whole.setTransform(currentOrientation);
				//orientation.y += Math.PI / 32;
				
				//angularVelocity.y = 0;
				//orientation.normalize();
				//System.out.println(orientation.x+ ", " + orientation.y + " " + orientation.z);
				break;
				
			case KeyEvent.VK_D:
				
				turnRight.rotY(-Math.PI / 32);
				currentOrientation.mul(turnRight);
				whole.setTransform(currentOrientation);
				//orientation.y -= Math.PI / 32;
				//angularVelocity.y = 0;
				//orientation.normalize();
				//System.out.println(orientation.x+ ", " + orientation.y + " " + orientation.z);
				break;
			case KeyEvent.VK_LEFT:
				
				left.rotY(Math.PI / 32);
				current.mul(left);
				turret.setTransform(current);
				barrelTurn = barrelTurn - 1;
				break;
			case KeyEvent.VK_RIGHT:
				
				right.rotY(-Math.PI / 32);
				current.mul(right);
				turret.setTransform(current);
				barrelTurn = barrelTurn + 1;
				break;
			case KeyEvent.VK_UP:
				
				tank.getBarrel().getTransform(barrelUp);
				Transform3D up = new Transform3D();
				up.rotZ(Math.PI /32);
				barrelUp.mul(up);
				if (barrelElevation < maxBarrelElevation) {
					tank.getBarrel().setTransform(barrelUp);
					barrelElevation++;
				}
				//barrelUp.get(aim);
				
				break;
			case KeyEvent.VK_DOWN:
				
				tank.getBarrel().getTransform(barrelDown);
				Transform3D down = new Transform3D();
				down.rotZ(-Math.PI /32);
				barrelDown.mul(down);
				if (barrelElevation > minBarrelElevation) {
					tank.getBarrel().setTransform(barrelDown);
					barrelElevation--;
				}
				
				break;
			//case KeyEvent.VK_SPACE:
				//spawnChildren(0f);
				//System.out.println("Tried to fire particle");
				//break;
		}
		if (barrelTurn < -MAX_TURN) {
			barrelTurn = MAX_TURN - 1;
		} else if (barrelTurn > MAX_TURN) {
			barrelTurn = -MAX_TURN + 1;
		}
		
	}
	
	/**
	 * Update State and maybe generate a new object.
	 * 
	 * @return A list of new objects to add to the world.
	 */
	public List<PhysicalObject> spawnChildren(float duration) {
		List<PhysicalObject> children = super.spawnChildren(duration);
		
		if (children == null) {
			children = new LinkedList<PhysicalObject>();
		}
		
		if (lastEvent != null && lastEvent.getKeyCode() == KeyEvent.VK_SPACE) {
			
			//System.out.println(barrelTurn);
			Vector3f toSet = new Vector3f(position.getX(), position.getY(), position.getZ());
			//toSet.x += 1f * myScale;
			//toSet.x = toSet.x - .5f * myScale;
			//toSet.y += Body.height * myScale + .275 * myScale;
			
			float xyTheta = ((float) Math.PI / 32) * barrelElevation;
			float xzTheta = ((float) Math.PI / 32) * barrelTurn;
			//float c = theta * Body.gunLength * myScale;
			//toSet.y = toSet.y + c;
			//VERTICAL CALCULATION
			float l = Body.gunLength * myScale + .45f * myScale;
			float q = (l * (float) Math.sin((double) xyTheta))/ (float) Math.sin((Math.PI - xyTheta) / 2);
			float w = (l * (float) Math.sin((double) xzTheta))/ (float) Math.sin((Math.PI - xzTheta) / 2);
			float newX = l - ((q * q) / (2 * l));
			float newY = (float) ((q / (2 * l)) * Math.sqrt(4 * l * l - q * q));
			toSet.x = toSet.x + newX;
			toSet.y = toSet.y + newY;
			
			//HORIZONTAL CALCULATION
			float newnewX = l - ((w * w) / (2 * l));
			float newZ = (float) ((w / (2 * l)) * Math.sqrt(4 * l * l - w * w));
			//toSet.x = toSet.x - newnewX;
			
				toSet.z = toSet.z + newZ;
				toSet.x = toSet.x - newX;
				float temp = Math.max(newnewX, newX) - Math.min(newnewX, newX);
				toSet.x = toSet.x + Math.max(newnewX, newX) - temp;
			
			/*
			System.out.println("theta " + theta);
			System.out.println("q " + q);
			System.out.println("l " + q);
			System.out.println(newX);
			System.out.println(newY);*/
			toSet.y += Body.height * myScale + .275 * myScale;
			Particle toAdd = new Particle(toSet, new Color3f(DEFAULT_BODY_COLOR));
			children.add(toAdd);
			
			//System.out.println(toAdd.getPosition());
			//System.out.println(this.position);
			lastEvent = null;
		}
		
		return children;
	}
	
	

}
