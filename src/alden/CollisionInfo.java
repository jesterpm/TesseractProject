package alden;
import javax.vecmath.*;

@SuppressWarnings("restriction")
public class CollisionInfo implements Cloneable {
	public Vector3f contactPoint;
	public Vector3f contactNormal;
	public float penetration;
	
	public CollisionInfo(Vector3f contactPoint, Vector3f contactNormal, float penetration) {
		this.contactPoint = contactPoint;
		this.contactNormal = contactNormal;
		this.penetration = penetration;
	}
	
	public CollisionInfo clone() {
		try {
			CollisionInfo copy = (CollisionInfo)super.clone();
			copy.contactPoint = new Vector3f(contactPoint);
			copy.contactNormal = new Vector3f(contactNormal);
			return copy;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}