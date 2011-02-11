package alden;
import javax.vecmath.*;

@SuppressWarnings("restriction")
public class CollisionInfo {
	public Vector3f contactPoint;
	public Vector3f contactNormal;
	public float penetration;
	
	public CollisionInfo(Vector3f contactPoint, Vector3f contactNormal, float penetration) {
		this.contactPoint = contactPoint;
		this.contactNormal = contactNormal;
		this.penetration = penetration;
	}
}