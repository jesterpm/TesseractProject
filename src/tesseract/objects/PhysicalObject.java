package tesseract.objects;

import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.vecmath.Vector3f;

import alden.CollidableObject;
import alden.CollisionInfo;

import com.sun.j3d.utils.geometry.Primitive;

/**
 * This class is the parent of all objects in the world.
 * 
 * Note: The constructor of a child class must add its shape
 *  to the transform group for it to be visible.
 * 
 * @author Jesse Morgan
 */
public class PhysicalObject extends CollidableObject {
	protected boolean collidable;
	
	public PhysicalObject(final Vector3f thePosition, final float mass) {
		super(mass);
		this.position.set(thePosition);
		collidable = true;
	}

	public List<PhysicalObject> spawnChildren(final float duration) {
		return null;
	}
	
	public void addForce(final Vector3f force) {
		this.forceAccumulator.add(force);
	}
	
	public void updateState(final float duration) {
		super.updateState(duration);
		this.updateTransformGroup();
	}
	
	public void setShape(final Node node) {
		node.setCapability(javax.media.j3d.Node.ALLOW_BOUNDS_READ);
		node.setCapability(javax.media.j3d.Node.ALLOW_LOCAL_TO_VWORLD_READ);

		if (node instanceof Primitive) {
			Primitive prim = (Primitive) node;
			int index = 0;
			Shape3D shape;
			while ((shape = prim.getShape(index++)) != null) {
				shape.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
				shape.setCapability(Node.ALLOW_LOCAL_TO_VWORLD_READ);
				shape.setCapability(Node.ALLOW_LOCAL_TO_VWORLD_READ);

				for (int i = 0; i < shape.numGeometries(); i++) {
					shape.getGeometry(i).setCapability(
							GeometryArray.ALLOW_COUNT_READ);
					shape.getGeometry(i).setCapability(
							GeometryArray.ALLOW_COORDINATE_READ);
				}

			}
		}

		super.setShape(node);
	}

	public void resolveCollisions(final PhysicalObject other) {
		if (collidable && other.collidable) {
			super.resolveCollisions(other);
		}
	}
}