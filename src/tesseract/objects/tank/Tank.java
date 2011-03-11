package tesseract.objects.tank;

import java.awt.Color;
import java.awt.event.KeyEvent;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import tesseract.objects.remote.RemoteObject;

public class Tank extends RemoteObject {
	
	private final TransformGroup whole;
	private final TransformGroup turret;
	private final Vector3f orientation;
	private final Vector3f aim;
	private final Point3f gunLocation;
	private static final float DEFAULT_SCALE = 0.0625f;
	private static final Color DEFAULT_BODY_COLOR = Color.GREEN;
	private static final Color DEFAULT_TRACK_COLOR = Color.DARK_GRAY;
	private static final Color DEFAULT_TURRET_COLOR = Color.GREEN;
	private final Body tank;
	private int barrelElevation = 0;
	private static final int maxBarrelElevation = 14;
	private static final int minBarrelElevation = -1;
	
	public Tank(Vector3f thePosition, float mass) {
		this(thePosition, mass, DEFAULT_SCALE);
	}

	public Tank(Vector3f thePosition, float mass, float theScale) {
		this(thePosition, mass, theScale, DEFAULT_BODY_COLOR,
				DEFAULT_TRACK_COLOR, DEFAULT_TURRET_COLOR);
	}

	public Tank(Vector3f thePosition, float mass, float theScale,
			Color bodyColor, Color trackColor, Color turretColor) {
		super (thePosition, mass);
		tank = new Body(trackColor, bodyColor, theScale, turretColor);
		orientation = new Vector3f();
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
		Transform3D check = new Transform3D();
		tank.getBody().getTransform(check);
		check.get(orientation);
		Transform3D current = new Transform3D();
		turret.getTransform(current);
		switch (event.getKeyCode()) {
			case KeyEvent.VK_W:
				
				break;
				
			case KeyEvent.VK_S:
				
				break;
				
			case KeyEvent.VK_A:
				angularVelocity.y += STEP;
				break;
				
			case KeyEvent.VK_D:
				angularVelocity.y -= STEP;
				break;
			case KeyEvent.VK_LEFT:
				
				Transform3D left = new Transform3D();
				left.rotY(Math.PI / 32);
				current.mul(left);
				turret.setTransform(current);
				break;
			case KeyEvent.VK_RIGHT:
				Transform3D right = new Transform3D();
				right.rotY(-Math.PI / 32);
				current.mul(right);
				turret.setTransform(current);
				break;
			case KeyEvent.VK_UP:
				Transform3D barrelUp = new Transform3D();
				tank.getBarrel().getTransform(barrelUp);
				Transform3D up = new Transform3D();
				up.rotZ(Math.PI /32);
				barrelUp.mul(up);
				if (barrelElevation < maxBarrelElevation) {
					tank.getBarrel().setTransform(barrelUp);
					barrelElevation++;
				}
				
				break;
			case KeyEvent.VK_DOWN:
				Transform3D barrelDown = new Transform3D();
				tank.getBarrel().getTransform(barrelDown);
				Transform3D down = new Transform3D();
				down.rotZ(-Math.PI /32);
				barrelDown.mul(down);
				if (barrelElevation > minBarrelElevation) {
					tank.getBarrel().setTransform(barrelDown);
					barrelElevation--;
				}
				break;
		}
	}
	
	

}
