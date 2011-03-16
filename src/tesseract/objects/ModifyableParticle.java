package tesseract.objects;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Sphere;
import common.CollidableObject;
import common.CollisionInfo;

/**
 * A particle object.
 * @author Phillip Cardon
 * @author Jesse Morgan
 */
public class ModifyableParticle extends PhysicalObject {
	/**
	 * Rendered radius of particle.
	 */
	private static final float RADIUS = .004f;
	
	/**
	 * Default mass.
	 */
	private static final float DEFAULT_MASS = 1;
	
	/**
	 * Number of divisions in the sphere.
	 */
	private static final int DIVISIONS = 8;
	
	private TransformGroup myTop;
	private TransformGroup myBottom;
	private TransformGroup myDetach;

	private boolean spawnKids;

	private float myMass;

	private Color3f myColor;

	private boolean kidsSpawned;
	private float myScale;
	
	/**
	 * Create a new Particle.
	 * 
	 * @param position Initial position.
	 * @param color Initial color. Null for random.
	 */
	public ModifyableParticle(final Vector3f position, final float mass, final Color3f color, final TransformGroup top, 
			final TransformGroup bottom, final float theScale) {
		super(position, mass);
		myScale = theScale;
		myTop = (TransformGroup) top.cloneTree();
		myBottom = getBottom(myTop);
		myMass = mass;
		myColor = color;
		myDetach = top;
		bottom.addChild(createShape(color));
		BranchGroup bg = new BranchGroup();
		bg.addChild(top);
		setShape(bg);
		spawnKids = false;
		kidsSpawned = false;
	}
	
	/**
	 * Create a new particle of the give color.
	 * 
	 * @param theColor The particle color or null for random.
	 * @return A sphere to visually represent the particle.
	 */
	private Shape3D createShape(final Color3f theColor) {
		
		Color3f color = theColor;
		
		ColoringAttributes cAttr;
		
		if (color == null) {
			Color randomColor = Color.getHSBColor((float) Math.random(), 1, 1);
			color = new Color3f(randomColor);
		}
		/*
		cAttr = new ColoringAttributes(color, ColoringAttributes.FASTEST);
		Appearance appearance = new Appearance();
		Material mat = new Material();
		mat.setAmbientColor(color);
		mat.setDiffuseColor(color);
		appearance.setMaterial(mat);
		appearance.setColoringAttributes(cAttr);
		
		Sphere sphere = new Sphere(RADIUS, Sphere.ENABLE_GEOMETRY_PICKING,
				DIVISIONS, appearance);
		*/
		
		Sphere sphere = new Sphere(RADIUS * myScale * 8, Sphere.ENABLE_GEOMETRY_PICKING,
				DIVISIONS);
		Shape3D shape = sphere.getShape();
		sphere.removeAllChildren();
		Appearance meshApp = new Appearance();
		Material surface = new Material();
		surface.setDiffuseColor(color);
		meshApp.setMaterial(surface);
		meshApp.setColoringAttributes(new ColoringAttributes(color,
						ColoringAttributes.FASTEST));
		shape.setAppearance(meshApp);
		return shape;
	}

	public void setAcceleration(Vector3f accelerator) {
		//accelerator.y -= 0.0118 * 2;
		accelerator.scale(1f / myScale);
		this.velocity = accelerator;		
	}
	
	public void updateState(float duration) {
		if (velocity.x < 0f && previousVelocity.x > 0f) {
			explode();
		} else if (velocity.x > 0f && previousVelocity.x < 0f) {
			explode();
		}
		
		if (velocity.y < 0f && previousVelocity.y > 0f) {
			explode();
		} else if (velocity.y > 0f && previousVelocity.y < 0f) {
			explode();
		}
		
		if (velocity.z < 0f && previousVelocity.z > 0f) {
			explode();
		} else if (velocity.z > 0f && previousVelocity.z < 0f) {
			explode();
		}
		if (!kidsSpawned) {
			super.updateState(duration);
		} else {
			detach();
		}
	}
	
	public void explode() {
		if (!kidsSpawned){
			spawnKids = true;
		}
		//detach();
	}
	
	public List<PhysicalObject> spawnChildren(float duration) {
		List<PhysicalObject> children = super.spawnChildren(duration);
		
		if (children == null) {
			children = new LinkedList<PhysicalObject>();
		}
		if (spawnKids) {
			//
			for (int i = 0; i < 20; i++) {
				Vector3f childVelocity = new Vector3f();
				childVelocity.x = (float)(Math.random() - 0.5);
				childVelocity.y = (float)(Math.random() - 0.5);
				childVelocity.z = (float)(Math.random() - 0.5);
				childVelocity.normalize();
				childVelocity.scale(0.2f);
				childVelocity.add(getVelocity());
				TransformGroup cloned = (TransformGroup) myTop.cloneTree();
				TransformGroup clonedBottom = getBottom(cloned);
				DyingParticle shrapnel = new DyingParticle(position, myMass, myColor, cloned, clonedBottom);
				shrapnel.setAcceleration(childVelocity);
				children.add(shrapnel);
			}
			kidsSpawned = true;
			spawnKids = false;
			detach();
		}
		
		return children;
	}
	
	/**
	 * Gets a bottom TransformGroup in a tree consisting of TransformGroups.
	 * @param cloned
	 * @return bottom node
	 */
	private TransformGroup getBottom(TransformGroup cloned) {
		if (cloned.numChildren() != 0) {
			for (int i = 0; i < cloned.numChildren(); i++) {
				if (cloned.getChild(i) instanceof TransformGroup) {
					return getBottom((TransformGroup) cloned.getChild(i));
				}
			}
		}
			return cloned;
	}
}
