package tesseract.objects.tank;

import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

//import tesseract.objects.emitters.FireableEmitter;



import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;

public class Body {
	public static final float width = 1.35f;
	public static final float height = .45f;
	public static final float depth = .9f;
	public static float radius = .75f;
	public static float gunRad = .075f;
	public static float gunLength = 2f;
	private TransformGroup body;
	private TransformGroup turret;
	private TransformGroup barrel;
	private Vector3f[] vectors;
	private Vector3f aim;
	//private FireableEmitter shooter;
	
	
	public Body(Color trackColor, Color bodyColor, float theScale, Color turretColor) {
		//shooter = new FireableEmitter(new Vector3f(), new Vector3f(), new Color3f(1f, 0f, 0f));
		body = new TransformGroup();
		turret = new TransformGroup();
		barrel = new TransformGroup();
		makeBody(trackColor, bodyColor, theScale);
		Transform3D turretMove = new Transform3D();
		turretMove.setTranslation(new Vector3f(0, height * theScale, 0));
		makeTurret(turretColor, theScale, turretMove);
		barrel.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
	}
	
	private void makeBody(Color trackColor, Color bodyColor, float theScale) {
		Vector3f facing = new Vector3f();
		Appearance appearance = new Appearance();
		Material material = new Material();
		material.setDiffuseColor(new Color3f(bodyColor));
		appearance.setColoringAttributes(new ColoringAttributes(new Color3f(bodyColor), ColoringAttributes.NICEST));
		appearance.setMaterial(material);
		Primitive box = new Box(width * theScale, height * theScale,
				depth * theScale, appearance);
		Shape3D front = box.getShape(Box.RIGHT);
		//
		Geometry g = front.getGeometry(0);
		GeometryInfo gi = new GeometryInfo((GeometryArray)g);
		vectors = gi.getNormals();
		
		//
		Transform3D trackMove = new Transform3D();
		Transform3D downward = new Transform3D();
		downward.setTranslation(new Vector3f(0, (-height / 1.125f) * theScale,0));
		trackMove.setTranslation(new Vector3f(0, 0, depth * theScale));
		trackMove.mul(downward);
		Shape3D leftTrack = Track.makeTrack(theScale, trackColor, trackMove);
		trackMove = new Transform3D();
		trackMove.setTranslation(new Vector3f(0, 0, -depth * theScale));
		trackMove.mul(downward);
		Shape3D rightTrack = Track.makeTrack(theScale, trackColor, trackMove);
		//Shape3D body = new Shape3D();
		//body.removeAllGeometries();
		
		body.addChild(box);
		body.addChild(leftTrack);
		body.addChild(rightTrack);
		//makeTurret(appearance, theScale, turretMove);
		//TransformGroup turret = makeTurret(appearance, theScale, turretMove);
		//TransformGroup[] tankNturret = {tank, turret};
	}
	
	public void makeTurret(Color turretColor, float theScale,
			Transform3D toMove) {
		Appearance appearance = new Appearance();
		Material material = new Material();
		material.setDiffuseColor(new Color3f(turretColor));
		appearance.setColoringAttributes(new ColoringAttributes(new Color3f(turretColor), ColoringAttributes.NICEST));
		appearance.setMaterial(material);
		
		
		Primitive sphere = new Sphere(radius * theScale, appearance);
		Primitive gun = new Cylinder(gunRad * theScale, gunLength * theScale, appearance);
		
		
		TransformGroup tg = new TransformGroup();
		TransformGroup gunTG = new TransformGroup();
		gunTG.addChild(gun);
		Transform3D mg = new Transform3D();
		mg.rotZ(Math.PI / 2);
		mg.setTranslation(new Vector3f(1.4f * theScale, .25f * theScale, 0));
		gunTG.setTransform(mg);
		
		
		tg.addChild(sphere);
		tg.addChild(barrel);
		barrel.addChild(gunTG);
		tg.setTransform(toMove);
		turret.addChild(tg);
	}
	
	public TransformGroup getBody() {
		return body;
	}
	
	public TransformGroup getTurret() {
		return turret;
	}
	
	public TransformGroup getBarrel() {
		return barrel;
	}
	
	public Vector3f getFacing() {
		return vectors[0];
	}
	
	public Vector3f getAim() {
		return aim;
	}
	
	//public FireableEmitter getShooter() {
	//	return shooter;
	//}
}
