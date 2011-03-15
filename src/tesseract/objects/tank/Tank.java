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

import tesseract.objects.ModifyableParticle;
import tesseract.objects.Particle;
import tesseract.objects.PhysicalObject;
import tesseract.objects.remote.RemoteObject;

public class Tank extends RemoteObject {
	
	private final TransformGroup whole;
	private final TransformGroup turret;
	//private final Vector3f orientation;
	KeyEvent lastEvent;
	//private Vector3f aim;
	//private final Point3f gunLocation;
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
		//aim = new Vector3f();
		//gunLocation = new Point3f();
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
		//inverseInertiaTensor.m00 = 1f / 12 / inverseMass * (Body.height * Body.height * theScale + Body.depth * Body.depth * theScale);
		//inverseInertiaTensor.m11 = 1f / 12 / inverseMass * (Body.width * Body.width * theScale + Body.depth * Body.depth * theScale);
		//inverseInertiaTensor.m22 = 1f / 12 / inverseMass * (Body.width * Body.width * theScale + Body.height * Body.height * theScale);
		//inverseInertiaTensor.invert();
		
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
		Vector3f facing = new Vector3f(tank.getFacing());
		Transform3D faceTrans = new Transform3D();
		whole.getTransform(faceTrans);
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
				whole.setTransform(currentOrientation);
				turnLeft.transform(velocity);
				//orientation.y += Math.PI / 32;
				
				//angularVelocity.y = 0;
				//orientation.normalize();
				//System.out.println(orientation.x+ ", " + orientation.y + " " + orientation.z);
				break;
				
			case KeyEvent.VK_D:
				
				turnRight.rotY(-Math.PI / 32);
				currentOrientation.mul(turnRight);
				whole.setTransform(currentOrientation);
				turnRight.transform(velocity);
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
			
			TransformGroup particleBody = new TransformGroup();
			TransformGroup particleTurret = new TransformGroup();
			TransformGroup particleTG = new TransformGroup();
			TransformGroup particleBarrel = new TransformGroup();
			TransformGroup particleGunTG = new TransformGroup();
			Transform3D collector = new Transform3D();
			Transform3D current = new Transform3D();
			Transform3D pTurret = new Transform3D();
			Transform3D pTG = new Transform3D();
			Transform3D pBarrel = new Transform3D();
			Transform3D pGun = new Transform3D();
			Transform3D temp = new Transform3D();
			temp.setTranslation(new Vector3f(0, .3f * myScale, 0));
			whole.getTransform(current);
			collector.set(current);
			current.mul(temp);
			particleBody.setTransform(current);
			turret.getTransform(pTurret);
			pTurret.setScale(1.7);
			particleTurret.setTransform(pTurret);
			((TransformGroup) turret.getChild(0)).getTransform(pTG);
			particleTG.setTransform(pTG);
			tank.getBarrel().getTransform(pBarrel);
			particleBarrel.setTransform(pBarrel);
			((TransformGroup) tank.getBarrel().getChild(0)).getTransform(pGun);
			particleGunTG.setTransform(pGun);
			particleBody.addChild(particleTurret);
			particleTurret.addChild(particleTG);
			particleTG.addChild(particleBarrel);
			particleBarrel.addChild(particleGunTG);
			collector.mul(pTurret);
			collector.mul(pTG);
			collector.mul(pBarrel);
			collector.mul(pGun);
			Vector3f accelerator = new Vector3f();
			collector.get(accelerator);
			//System.out.println(accelerator);
			ModifyableParticle toAdd = new ModifyableParticle(position, 1f, new Color3f(Color.RED),
					particleBody, particleGunTG);
			toAdd.setAcceleration(accelerator);
			/*
			float xyTheta = ((float) Math.PI / 32) * barrelElevation;
			float xzTheta = ((float) Math.PI / 32) * barrelTurn;
			float zyTheta = ((float) Math.PI / 32) * barrelElevation;
			//float c = theta * Body.gunLength * myScale;
			//toSet.y = toSet.y + c;
			//VERTICAL CALCULATION
			float l = Body.gunLength * myScale + .45f * myScale;
			float q = (l * (float) Math.sin((double) xyTheta))/ (float) Math.sin((Math.PI - xyTheta) / 2);
			float w = (l * (float) Math.sin((double) xzTheta))/ (float) Math.sin((Math.PI - xzTheta) / 2);
			float e = (l * (float) Math.sin((double) zyTheta))/ (float) Math.sin((Math.PI - zyTheta) / 2);
			float newX = l - ((q * q) / (2 * l));
			float newY = (float) ((q / (2 * l)) * Math.sqrt(4 * l * l - q * q));
			toSet.x = toSet.x + newX;
			toSet.y = toSet.y + newY;
			
			//HORIZONTAL CALCULATION
			float newnewX = l - ((w * w) / (2 * l));
			float newZ = (float) ((w / (2 * l)) * Math.sqrt(4 * l * l - w * w));
			float newZy = (float) ((e / (2 * l)) * Math.sqrt(4 * l * l - e * e));
			//toSet.x = toSet.x - newnewX;
			if (barrelTurn != 0) {
				//toSet.z = toSet.z + newZ;
				toSet.x = toSet.x - newX;
				float temp = Math.max(newnewX, newX) - Math.min(newnewX, newX);
				//toSet.x = toSet.x + Math.max(newnewX, newX) - temp;
				if (Math.abs(newnewX) > Math.abs(newX)) {
					toSet.x = toSet.x + newnewX - temp;
				} else {
					toSet.x = toSet.x + newX - temp;
				}
				
				
					float zTemp = Math.abs(newZ - newZy);
					if (Math.abs(newZ) < Math.abs(newZy)) {
						toSet.z = toSet.z + newZ - zTemp;
					} else {
						toSet.z = toSet.z + newZy - zTemp;
					}
			}
			/*
			System.out.println("theta " + theta);
			System.out.println("q " + q);
			System.out.println("l " + q);
			System.out.println(newX);
			System.out.println(newY);*/
			//toSet.y += Body.height * myScale + .275 * myScale;
			//System.out.println(toSet);
			//Particle toAdd = new Particle(toSet, new Color3f(DEFAULT_BODY_COLOR));
			//children.add(toAdd);
			children.add(toAdd);
			//System.out.println(toAdd.getPosition());
			//System.out.println(this.position);
			lastEvent = null;
		}
		return children;
	}
	
	

}
