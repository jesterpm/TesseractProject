package tesseract.objects.tank;

import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Group;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;

public class Body {
	public static final float width = 1.35f;
	public static final float height = .45f;
	public static final float depth = .9f;
	public static float radius = .75f;
	public static float gunRad = .075f;
	public static float gunLength = 2f;
	
	public static TransformGroup makeBody(Color trackColor, Color bodyColor, float theScale) {
		TransformGroup tank = new TransformGroup();
		Appearance appearance = new Appearance();
		Material material = new Material();
		material.setDiffuseColor(new Color3f(bodyColor));
		appearance.setColoringAttributes(new ColoringAttributes(new Color3f(bodyColor), ColoringAttributes.NICEST));
		appearance.setMaterial(material);
		Primitive box = new Box(width * theScale, height * theScale,
				depth * theScale, appearance);
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
		Transform3D turretMove = new Transform3D();
		turretMove.setTranslation(new Vector3f(0, height * theScale, 0));
		tank.addChild(box);
		tank.addChild(leftTrack);
		tank.addChild(rightTrack);
		//makeTurret(appearance, theScale, turretMove);
		//TransformGroup turret = makeTurret(appearance, theScale, turretMove);
		//TransformGroup[] tankNturret = {tank, turret};
		return tank;
	}
	public static TransformGroup makeTurret(Color turretColor,
			float theScale,	Transform3D toMove) {
		Appearance appearance = new Appearance();
		Material material = new Material();
		material.setDiffuseColor(new Color3f(turretColor));
		appearance.setColoringAttributes(new ColoringAttributes(new Color3f(turretColor), ColoringAttributes.NICEST));
		appearance.setMaterial(material);
		TransformGroup tg = new TransformGroup();
		TransformGroup gunTG = new TransformGroup();
		Primitive sphere = new Sphere(radius * theScale, appearance);
		Primitive gun = new Cylinder(gunRad * theScale, gunLength * theScale, appearance);
		gunTG.addChild(gun);
		Transform3D mg = new Transform3D();
		mg.rotZ(Math.PI / 2);
		mg.setTranslation(new Vector3f(1.4f * theScale, .25f * theScale, 0));
		gunTG.setTransform(mg);
		Transform3D rotateGun = new Transform3D();
		rotateGun.rotY(Math.PI / 2);
		//toMove.mul(rotateGun);
		tg.addChild(sphere);
		tg.addChild(gunTG);
		tg.setTransform(toMove);
		TransformGroup turret = new TransformGroup();
		turret.addChild(tg);
		//turret.setTransform(rotateGun);
		//tg.setTransform();
		return turret;
	}
}
