package tesseract.objects.tank;

import java.awt.Color;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import tesseract.objects.remote.RemoteObject;

public class Tank extends RemoteObject {
	
	private final TransformGroup tank;
	private final TransformGroup body;
	private final TransformGroup turret;
	private static final float DEFAULT_SCALE = 0.0625f;
	private static final Color DEFAULT_BODY_COLOR = Color.GREEN;
	private static final Color DEFAULT_TRACK_COLOR = Color.DARK_GRAY;
	private static final Color DEFAULT_TURRET_COLOR = Color.GREEN;
	
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
		body = Body.makeBody(DEFAULT_BODY_COLOR, DEFAULT_TRACK_COLOR, theScale);
		Transform3D turretMove = new Transform3D();
		turretMove.setTranslation(new Vector3f(0, Body.height * theScale, 0));
		turret = Body.makeTurret(turretColor, theScale, turretMove);
		tank = new TransformGroup();
		tank.addChild(body);
		tank.addChild(turret);
		setShape(tank);
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

}
