package common;

import java.io.*;
import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.*;
import group2.*;

@SuppressWarnings("restriction")
public abstract class CollidableObject implements Serializable {
	private static final long serialVersionUID = 3667108226485766929L;

	protected float inverseMass;
	// The center of mass in the local coordinate system
	protected Vector3f centerOfMass;
	protected Vector3f position, previousPosition;
	protected Vector3f velocity, previousVelocity;
	protected Vector3f forceAccumulator;
	protected Quat4f orientation;
	protected Vector3f angularVelocity;
	protected Vector3f torqueAccumulator;
	protected Matrix3f inverseInertiaTensor;
	protected float coefficientOfRestitution;
	protected float penetrationCorrection;
	protected float dynamicFriction;
	transient protected BranchGroup BG;
	transient protected TransformGroup TG;
	transient protected Node node;
	transient private ArrayList<Vector3f> vertexCache;
	transient private ArrayList<CollisionDetector.Triangle> triangleCache;
	transient private Bounds boundsCache;
	// The inverse inertia tensor in world coordinates
	transient private Matrix3f inverseInertiaTensorCache;
	
	/**
	 * Copy Constructor
	 * @param The CollidableObject to copy.
	 */
	protected CollidableObject(CollidableObject o) {
		inverseMass = o.inverseMass;
		centerOfMass = o.centerOfMass;
		position = o.position;
		previousPosition = o.previousPosition;
		velocity = o.velocity;
		previousVelocity = o.previousVelocity;
		forceAccumulator = o.forceAccumulator;
		orientation = o.orientation;
		angularVelocity = o.angularVelocity;
		torqueAccumulator = o.torqueAccumulator;
		inverseInertiaTensor = o.inverseInertiaTensor;
		coefficientOfRestitution = o.coefficientOfRestitution;
		penetrationCorrection = o.penetrationCorrection;
		dynamicFriction = o.dynamicFriction;
		BG = o.BG;
		TG = o.TG;
		node = o.node;
		vertexCache = o.vertexCache;
		triangleCache = o.triangleCache;
		boundsCache = o.boundsCache;
		inverseInertiaTensorCache = o.inverseInertiaTensorCache;
	}
	
	public CollidableObject() {
		this(1);
	}

	public CollidableObject(float mass) {
		if (mass <= 0)
			throw new IllegalArgumentException();
		inverseMass = 1 / mass;
		centerOfMass = new Vector3f();
		position = new Vector3f();
		previousPosition = new Vector3f();
		velocity = new Vector3f();
		previousVelocity = new Vector3f();
		forceAccumulator = new Vector3f();
		orientation = new Quat4f(0, 0, 0, 1);
		angularVelocity = new Vector3f();
		torqueAccumulator = new Vector3f();
		inverseInertiaTensor = new Matrix3f();
		coefficientOfRestitution = 0.85f;
		penetrationCorrection = 1.05f;
		dynamicFriction = 0.04f;
		TG = new TransformGroup();
		TG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		TG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		TG.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		BG = new BranchGroup();
		BG.setCapability(BranchGroup.ALLOW_DETACH);
		BG.addChild(TG);
	}

	protected void setShape(Node node) {
		this.node = node;
		TG.addChild(node);
//		TG.addChild(CollisionDetector.createShape(CollisionDetector.triangularize(node)));
	}

	public Group getGroup() {
		return BG;
	}

	public void detach() {
		BG.detach();
	}
	
	//ADDED
	private void updatePositionAndOrientation() {
		Transform3D tmpTransform = new Transform3D();
		TG.getTransform(tmpTransform);
		tmpTransform.get(orientation, position);
	}
	//ADDED

	public void updateState(float duration) {
		updatePositionAndOrientation();		//ADDED
		previousPosition.set(position);
		previousVelocity.set(velocity);
		// The force vector now becomes the acceleration vector.
		forceAccumulator.scale(inverseMass);
		position.scaleAdd(duration, velocity, position);
		position.scaleAdd(duration * duration / 2, forceAccumulator, position);
		velocity.scaleAdd(duration, forceAccumulator, velocity);
		// The force vector is cleared.
		forceAccumulator.set(0, 0, 0);
		
		angularVelocity.scaleAdd(duration, torqueAccumulator, angularVelocity);
		torqueAccumulator.set(0, 0, 0);
		UnQuat4f tmp = new UnQuat4f(angularVelocity.x, angularVelocity.y, angularVelocity.z, 0);
		tmp.scale(duration / 2);
		tmp.mul(orientation);
		orientation.add(tmp);
		orientation.normalize();
	}

	protected void updateTransformGroup() {
		Vector3f com = new Vector3f(-centerOfMass.x, -centerOfMass.y, -centerOfMass.z);
		Transform3D tmp = new Transform3D();
		tmp.setTranslation(com);
		Transform3D tmp2 = new Transform3D();
		tmp2.setRotation(orientation);
		com.negate();
		com.add(position);
		tmp2.setTranslation(com);
		tmp2.mul(tmp);
		TG.setTransform(tmp2);
		clearCaches();
	}

	public ArrayList<Vector3f> getVertices() {
		if (vertexCache == null)
			vertexCache = CollisionDetector.extractVertices(node);
		return vertexCache;
	}
	
	protected ArrayList<CollisionDetector.Triangle> getCollisionTriangles() {
		if (triangleCache == null)
			triangleCache = CollisionDetector.triangularize(node);
		return triangleCache;
	}

	protected Bounds getBounds() {
		if (boundsCache == null) {
			boundsCache = node.getBounds();
			Transform3D tmp = new Transform3D();
			node.getLocalToVworld(tmp);
			boundsCache.transform(tmp);
		}
		return boundsCache;
	}

	protected Matrix3f getInverseInertiaTensor() {
		if (inverseInertiaTensorCache == null) {
			inverseInertiaTensorCache = new Matrix3f();
			inverseInertiaTensorCache.set(orientation);
			Matrix3f tmp = new Matrix3f(inverseInertiaTensor);
			Matrix3f tmp2 = new Matrix3f(inverseInertiaTensorCache);
			tmp2.invert();
			tmp.mul(tmp2);
			inverseInertiaTensorCache.mul(tmp);
		}
		return inverseInertiaTensorCache;
	}

	protected void clearCaches() {
		vertexCache = null;
		triangleCache = null;
		boundsCache = null;
		inverseInertiaTensorCache = null;
	}
	
	public void resolveCollisions(CollidableObject other) {
		resolveCollisions(other, CollisionDetector.calculateCollisions(this, other));
	}

	public void resolveCollisions(CollidableObject other, ArrayList<CollisionInfo> collisions) {
		if (collisions.isEmpty())
			return;

		CollisionInfo finalCollision = null;
		float max = Float.NEGATIVE_INFINITY;
		int count = 0;
		for (CollisionInfo collision : collisions) {
			Vector3f thisRelativeContactPosition = new Vector3f();
			thisRelativeContactPosition.scaleAdd(-1, position, collision.contactPoint);
			thisRelativeContactPosition.scaleAdd(-1, centerOfMass, thisRelativeContactPosition);
			Vector3f thisContactVelocity = new Vector3f();
			thisContactVelocity.cross(angularVelocity, thisRelativeContactPosition);
			thisContactVelocity.add(previousVelocity);
			Vector3f otherRelativeContactPosition = new Vector3f();
			otherRelativeContactPosition.scaleAdd(-1, other.position, collision.contactPoint);
			otherRelativeContactPosition.scaleAdd(-1, other.centerOfMass, otherRelativeContactPosition);
			Vector3f otherContactVelocity = new Vector3f();
			otherContactVelocity.cross(other.angularVelocity, otherRelativeContactPosition);
			otherContactVelocity.add(other.previousVelocity);
			float speed = collision.contactNormal.dot(thisContactVelocity) - collision.contactNormal.dot(otherContactVelocity);
			if (speed > 0)
				if (speed > max + CollisionDetector.EPSILON) {
					finalCollision = collision;
					max = speed;
					count = 1;
				} else if (speed >= max - CollisionDetector.EPSILON) {
					finalCollision.contactPoint.add(collision.contactPoint);
					finalCollision.penetration += collision.penetration;
					count++;
				}
		}
		if (finalCollision != null) {
			finalCollision.contactPoint.scale(1f / count);
			finalCollision.penetration /= count;
			resolveCollision(other, finalCollision);
			updateTransformGroup();
			other.updateTransformGroup();
		}
	}

	public void resolveCollision(CollidableObject other, CollisionInfo ci) {
		if (ci.penetration <= 0)
			return;
		
		Vector3f thisRelativeContactPosition = new Vector3f();
		thisRelativeContactPosition.scaleAdd(-1, position, ci.contactPoint);
		thisRelativeContactPosition.scaleAdd(-1, centerOfMass, thisRelativeContactPosition);
		Vector3f thisContactVelocity = new Vector3f();
		thisContactVelocity.cross(angularVelocity, thisRelativeContactPosition);
		thisContactVelocity.add(previousVelocity);
		
		Vector3f otherRelativeContactPosition = new Vector3f();
		otherRelativeContactPosition.scaleAdd(-1, other.position, ci.contactPoint);
		otherRelativeContactPosition.scaleAdd(-1, other.centerOfMass, otherRelativeContactPosition);
		Vector3f otherContactVelocity = new Vector3f();
		otherContactVelocity.cross(other.angularVelocity, otherRelativeContactPosition);
		otherContactVelocity.add(other.previousVelocity);
		
		float initialClosingSpeed = ci.contactNormal.dot(thisContactVelocity) - ci.contactNormal.dot(otherContactVelocity);
		float finalClosingSpeed = -initialClosingSpeed * coefficientOfRestitution;
		float deltaClosingSpeed = finalClosingSpeed - initialClosingSpeed;
		float totalInverseMass = inverseMass + other.inverseMass;
		if (totalInverseMass == 0)
			return;
		
		Vector3f thisMovementUnit = new Vector3f();
		thisMovementUnit.cross(thisRelativeContactPosition, ci.contactNormal);
		getInverseInertiaTensor().transform(thisMovementUnit);
		Vector3f thisAngularVelocityUnit = new Vector3f();
		thisAngularVelocityUnit.cross(thisMovementUnit, thisRelativeContactPosition);
		totalInverseMass += thisAngularVelocityUnit.dot(ci.contactNormal);
		
		Vector3f otherMovementUnit = new Vector3f();
		otherMovementUnit.cross(otherRelativeContactPosition, ci.contactNormal);
		other.getInverseInertiaTensor().transform(otherMovementUnit);
		Vector3f otherAngularVelocityUnit = new Vector3f();
		otherAngularVelocityUnit.cross(otherMovementUnit, otherRelativeContactPosition);
		totalInverseMass += otherAngularVelocityUnit.dot(ci.contactNormal);

		Vector3f impulse = new Vector3f(ci.contactNormal);
		impulse.scale(deltaClosingSpeed / totalInverseMass);
		
		//Added
		Vector3f frictionalImpulse = new Vector3f();
		frictionalImpulse.scale(dynamicFriction); //not considering the time of impact
		//end-added
		
		velocity.scaleAdd(inverseMass, impulse, velocity);
		Vector3f tmp = new Vector3f();
		tmp.cross(thisRelativeContactPosition, impulse);
		getInverseInertiaTensor().transform(tmp);
		angularVelocity.add(tmp);
				
		position.scaleAdd(-ci.penetration * penetrationCorrection * inverseMass / totalInverseMass, ci.contactNormal, position);
		thisMovementUnit.scale(-ci.penetration * penetrationCorrection / totalInverseMass);
		UnQuat4f tmp2 = new UnQuat4f(thisMovementUnit.x, thisMovementUnit.y, thisMovementUnit.z, 0);
		tmp2.scale(0.5f);
		tmp2.mul(orientation);
		orientation.add(tmp2);
		orientation.normalize();
		
		// ADDED
		if(other instanceof Mars ) {
			Vector3f initialAngularVelocity = new Vector3f();

			Vector3f radius = new Vector3f();
			radius.scaleAdd(-1, other.centerOfMass, ci.contactPoint);
			
			float radiusNum = radius.length();
			initialAngularVelocity.cross(radius, other.velocity);
			other.angularVelocity.add(initialAngularVelocity);
			float torque = -dynamicFriction * radiusNum;
			other.angularVelocity.scale(torque);		
		}
		// END-ADDED

		impulse.negate();
		other.velocity.scaleAdd(other.inverseMass, impulse, other.velocity);
		//Added
		other.velocity.scaleAdd(other.inverseMass, frictionalImpulse, other.velocity);
		impulse.add(frictionalImpulse);
		//End-added
		tmp.cross(otherRelativeContactPosition, impulse);
		other.getInverseInertiaTensor().transform(tmp);
		other.angularVelocity.add(tmp);
		other.position.scaleAdd(ci.penetration * penetrationCorrection * other.inverseMass / totalInverseMass, ci.contactNormal, other.position);
		otherMovementUnit.scale(ci.penetration * penetrationCorrection / totalInverseMass);
		tmp2.set(otherMovementUnit.x, otherMovementUnit.y, otherMovementUnit.z, 0);
		tmp2.scale(0.5f);
		tmp2.mul(other.orientation);
		other.orientation.add(tmp2);
		other.orientation.normalize();
	}
	/*
	private float getMomentofInertia(Matrix3f inverseInertiaTensor2, Vector3f torque) {
		Vector3f tmp = new Vector3f(torque);
		tmp.scale(1/torque.length());
		inverseInertiaTensor2.transform(tmp);
		float inertia = tmp.dot(tmp);
		return inertia;
	}
	*/

	private static final int NODE_TYPE_BRANCH = 1;
	private static final int NODE_TYPE_TRANSFORM = 2;
	private static final int NODE_TYPE_PRIMITIVE = 3;
	private static final int NODE_TYPE_SHAPE = 4;

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		writeObject(out, node);
	}

	private static void writeObject(ObjectOutputStream out, Node node) throws IOException {
		if (node instanceof BranchGroup) {
			out.writeInt(NODE_TYPE_BRANCH);
			BranchGroup bg = (BranchGroup)node;
			out.writeInt(bg.numChildren());
			for (int i = 0; i < bg.numChildren(); i++) {
				writeObject(out, bg.getChild(i));
			}		
		} else if (node instanceof TransformGroup) {
			out.writeInt(NODE_TYPE_TRANSFORM);
			TransformGroup tg = (TransformGroup)node;
			Transform3D transform = new Transform3D();
			tg.getTransform(transform);
			Matrix4f matrix4f = new Matrix4f();
			transform.get(matrix4f);
			
			out.writeObject(matrix4f);
			out.writeInt(tg.numChildren());
			for (int i = 0; i < tg.numChildren(); i++) {
				writeObject(out, tg.getChild(i));
			}		
		} else if (node instanceof Primitive) {
			out.writeInt(NODE_TYPE_PRIMITIVE);
			Primitive prim = (Primitive)node;
			int index = 0;
			Shape3D shape;
			out.writeInt(prim.numChildren());
			while ((shape = prim.getShape(index++)) != null) {
				Appearance appearance = shape.getAppearance();
				if (appearance != null) {
					out.writeBoolean(true);
					writeObject(out, appearance);
				} else
					out.writeBoolean(false);
				out.writeInt(shape.numGeometries());
				for (int i = 0; i < shape.numGeometries(); i++)
					writeObject(out, shape.getGeometry(i));
			}
		} else if (node instanceof Shape3D) {
			out.writeInt(NODE_TYPE_SHAPE);
			// Shape3D extends a Leaf class and it has no children.
			// It contains a list of one or more Geometry component 
			// objects and a single Appearance component object. 
			Shape3D shape3D = (Shape3D)node;
			Appearance appearance = shape3D.getAppearance();
			if (appearance != null) {
				out.writeBoolean(true);
				writeObject(out, appearance);
			} else
				out.writeBoolean(false);
			out.writeInt(shape3D.numGeometries());
			for (int i = 0; i < shape3D.numGeometries(); i++)
				writeObject(out, shape3D.getGeometry(i));
		} else 
			throw new IllegalArgumentException("Illegal node type for serialization");
	}

	private static void writeObject(ObjectOutputStream out, Geometry geometry) throws IOException {
		GeometryInfo gi = new GeometryInfo((GeometryArray)geometry);
		gi.convertToIndexedTriangles();
		geometry = gi.getIndexedGeometryArray();
		IndexedTriangleArray trueGeometry = (IndexedTriangleArray)geometry;
		
		int format = trueGeometry.getVertexFormat() & (IndexedTriangleArray.COORDINATES | IndexedTriangleArray.NORMALS);
		out.writeInt(format);
		Point3f vertices[] = new Point3f[trueGeometry.getValidVertexCount()];
		for (int i = 0; i < vertices.length; i++) {
			vertices[i] = new Point3f();
			trueGeometry.getCoordinate(i, vertices[i]);
		}
		out.writeObject(vertices);
		int indices[] = new int[trueGeometry.getValidIndexCount()];
		trueGeometry.getCoordinateIndices(0, indices);
		out.writeObject(indices);
		
		if ((format & IndexedTriangleArray.NORMALS) != 0) {
			Vector3f normals[] = new Vector3f[trueGeometry.getValidVertexCount()];
			for (int i = 0; i < normals.length; i++) {
				normals[i] = new Vector3f();
				trueGeometry.getNormal(i, normals[i]);
			}
			out.writeObject(normals);
			indices = new int[trueGeometry.getValidIndexCount()];
			trueGeometry.getNormalIndices(0, indices);
			out.writeObject(indices);
		}
	}

	private static void writeObject(ObjectOutputStream out, Appearance appearance) throws IOException {
		// Write Coloring Attributes
		ColoringAttributes cAttrs = appearance.getColoringAttributes();
		if (cAttrs != null) {
			out.writeBoolean(true);
			Color3f color = new Color3f();
			cAttrs.getColor(color);
			out.writeObject(color);
			out.writeInt(cAttrs.getShadeModel());
		} else
			out.writeBoolean(false);

		// Write Polygon Attributes
		PolygonAttributes pAttrs = appearance.getPolygonAttributes();
		if (pAttrs != null) {
			out.writeBoolean(true);
			out.writeInt(pAttrs.getPolygonMode());
			out.writeInt(pAttrs.getCullFace());
			out.writeBoolean(pAttrs.getBackFaceNormalFlip());
			out.writeFloat(pAttrs.getPolygonOffset());
			out.writeFloat(pAttrs.getPolygonOffsetFactor());
		} else
			out.writeBoolean(false);
		
		// Write Transparency Attributes
		TransparencyAttributes tAttrs = appearance.getTransparencyAttributes();
		if (tAttrs != null) {
			out.writeBoolean(true);
			out.writeInt(tAttrs.getTransparencyMode());
			out.writeFloat(tAttrs.getTransparency());
			out.writeInt(tAttrs.getSrcBlendFunction());
			out.writeInt(tAttrs.getDstBlendFunction());
		} else
			out.writeBoolean(false);
		
		// Write Material Attributes
		Material material = appearance.getMaterial();
		if (material != null) {
			out.writeBoolean(true);
			Color3f color = new Color3f();
			material.getAmbientColor(color);
			out.writeObject(color);
			color = new Color3f();
			material.getDiffuseColor(color);
			out.writeObject(color);
			color = new Color3f();
			material.getSpecularColor(color);
			out.writeObject(color);
			color = new Color3f();
			material.getEmissiveColor(color);
			out.writeObject(color);
			out.writeFloat(material.getShininess());
			out.writeInt(material.getColorTarget());
		} else
			out.writeBoolean(false);
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		TG = new TransformGroup();
		TG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		TG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		TG.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		BG = new BranchGroup();
		BG.setCapability(BranchGroup.ALLOW_DETACH);
		BG.addChild(TG);
		setShape(readNode(in));
	}

	private static Node readNode(ObjectInputStream in) throws IOException, ClassNotFoundException {
		int type = in.readInt();
		switch (type) {
		case NODE_TYPE_BRANCH:
			BranchGroup branch = new BranchGroup();

			int numChildrenBranch = in.readInt();
			for (int i = 0; i < numChildrenBranch; i++) {
				branch.addChild(readNode(in));
			}
			return branch;
		case NODE_TYPE_TRANSFORM:
			TransformGroup tg = new TransformGroup();
			Transform3D transform = new Transform3D();
			transform.set((Matrix4f)in.readObject());
			tg.setTransform(transform);
			
			int numChildren = in.readInt();
			for (int i = 0; i < numChildren; i++) {
				tg.addChild(readNode(in));
			}
			return tg;
		case NODE_TYPE_PRIMITIVE:
			BranchGroup bg = new BranchGroup();
			int shapes = in.readInt();
			for (int i = 0; i < shapes; i++) {
				Shape3D shape = new Shape3D();
				shape.removeAllGeometries();
				if (in.readBoolean())
					shape.setAppearance(readAppearance(in));
				int geometries = in.readInt();
				for (int j = 0; j < geometries; j++)
					shape.addGeometry(readGeometry(in));
				bg.addChild(shape);			
			}
			return bg;
		case NODE_TYPE_SHAPE:
			Shape3D shape = new Shape3D();
			shape.removeAllGeometries();
			if (in.readBoolean())
				shape.setAppearance(readAppearance(in));
			int geometries = in.readInt();
			for (int j = 0; j < geometries; j++)
				shape.addGeometry(readGeometry(in));			
			return shape;
		default:
			throw new IllegalArgumentException("Illegal node type for serialization");
		}
	}

	private static GeometryArray readGeometry(ObjectInputStream in) throws IOException, ClassNotFoundException {
		int format = in.readInt();
		Point3f vertices[] = (Point3f[])in.readObject();
		int indices[] = (int[])in.readObject();
		IndexedTriangleArray geometry = new IndexedTriangleArray(vertices.length, format, indices.length);
		geometry.setCoordinates(0, vertices);
		geometry.setCoordinateIndices(0, indices);

		if ((format & IndexedTriangleArray.NORMALS) != 0) {
			Vector3f normals[] = (Vector3f[])in.readObject();
			indices = (int[])in.readObject();
			geometry.setNormals(0, normals);
			geometry.setNormalIndices(0, indices);
		}
		
		return geometry;
	}

	private static Appearance readAppearance(ObjectInputStream in) throws IOException, ClassNotFoundException {
		Appearance appearance = new Appearance();
		// Read Coloring Attributes
		if (in.readBoolean()) {
			ColoringAttributes cAttrs = new ColoringAttributes();
			cAttrs.setColor((Color3f)in.readObject());
			cAttrs.setShadeModel(in.readInt());
			appearance.setColoringAttributes(cAttrs);
		}	
		// Read Polygon Attributes
		if (in.readBoolean()) {
			PolygonAttributes pAttrs = new PolygonAttributes();
			pAttrs.setPolygonMode(in.readInt());
			pAttrs.setCullFace(in.readInt());
			pAttrs.setBackFaceNormalFlip(in.readBoolean());
			pAttrs.setPolygonOffset(in.readFloat());
			pAttrs.setPolygonOffsetFactor(in.readFloat());
			appearance.setPolygonAttributes(pAttrs);
		}
		// Read Transparency Attributes 
		if (in.readBoolean()) {
			TransparencyAttributes tAttrs = new TransparencyAttributes();
			tAttrs.setTransparencyMode(in.readInt());
			tAttrs.setTransparency(in.readFloat());
			tAttrs.setSrcBlendFunction(in.readInt());
			tAttrs.setDstBlendFunction(in.readInt());
			appearance.setTransparencyAttributes(tAttrs);
		}
		// Read Material
		if (in.readBoolean()) {
			Material material = new Material();
			material.setAmbientColor((Color3f)in.readObject());
			material.setDiffuseColor((Color3f)in.readObject());
			material.setSpecularColor((Color3f)in.readObject());
			material.setEmissiveColor((Color3f)in.readObject());
			material.setShininess(in.readFloat());
			material.setColorTarget(in.readInt());
			appearance.setMaterial(material);
		}
		return appearance;
	}
}
