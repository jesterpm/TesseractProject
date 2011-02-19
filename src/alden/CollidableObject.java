package alden;
import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

@SuppressWarnings("restriction")
public abstract class CollidableObject {
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
	protected BranchGroup BG;
	protected TransformGroup TG;
	protected Node node;
	private ArrayList<Vector3f> vertexCache;
	private ArrayList<CollisionDetector.Triangle> triangleCache;
	private Bounds boundsCache;
	// The inverse inertia tensor in world coordinates
	private Matrix3f inverseInertiaTensorCache;
	
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
		coefficientOfRestitution = 0.75f;
		penetrationCorrection = 1.05f;
		dynamicFriction = 0.02f;
		TG = new TransformGroup();
		TG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
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

	public void updateState(float duration) {
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
		ArrayList<CollisionInfo> collisions = CollisionDetector.calculateCollisions(this, other);
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
		
		/* Dynamic Friction */
		if (dynamicFriction > 0) {
			Vector3f perpVelocity = new Vector3f();
			perpVelocity.scaleAdd(initialClosingSpeed, ci.contactNormal, previousVelocity);
			if (perpVelocity.length() > 0) {
				perpVelocity.normalize();
				Vector3f acceleration = new Vector3f();
				acceleration.scaleAdd(-1, previousVelocity, velocity);
				velocity.scaleAdd(dynamicFriction * acceleration.dot(ci.contactNormal), perpVelocity, velocity);
			}
			
			perpVelocity.scaleAdd(initialClosingSpeed, ci.contactNormal, other.previousVelocity);
			if (perpVelocity.length() > 0) {
				perpVelocity.normalize();
				Vector3f acceleration = new Vector3f();
				acceleration.scaleAdd(-1, other.previousVelocity, other.velocity);
				other.velocity.scaleAdd(dynamicFriction * acceleration.dot(ci.contactNormal), perpVelocity, other.velocity);
			}
		}
		
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
		
		impulse.negate();
		other.velocity.scaleAdd(other.inverseMass, impulse, other.velocity);
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
}
