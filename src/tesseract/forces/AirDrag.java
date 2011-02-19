package tesseract.forces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.media.j3d.Transform3D;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import tesseract.objects.PhysicalObject;

public class AirDrag extends Force {

	private static final float COEFFICIENT = 20f;


	@Override
	protected Vector3f calculateForce(PhysicalObject obj) {
		if (obj.isNodeNull() || obj.getVelocity().length() == 0) {
			return new Vector3f();
		}
		
		Vector3f v = new Vector3f(obj.getVelocity());
		
		Vector3f p = new Vector3f(obj.getPosition());
		p.negate();
		
		Vector3f c = new Vector3f();
		c.sub(new Vector3f(0, 1, 0), v);
		
		Quat4f r = new Quat4f(c.x, c.y, c.z, 0);
		r.normalize();
		
		
		Vector3f com = new Vector3f(-obj.getCenterOfMass().x, -obj.getCenterOfMass().y, -obj.getCenterOfMass().z);
		com.negate();
		
		Transform3D tmp = new Transform3D();
		tmp.setTranslation(com);
		Transform3D tmp2 = new Transform3D();
		tmp2.setRotation(r);
		com.negate();
		com.add(p);
		tmp2.setTranslation(com);
		tmp2.mul(tmp);
		
		ArrayList<Vector3f> vertices = obj.getVertices();
		ArrayList<Vector2f> points = new ArrayList<Vector2f>(); 
		
		for (Vector3f point : vertices) {
			tmp2.transform(point);
			Vector2f newPoint = new Vector2f(point.x, point.z);
			
			// Place min y at front of arraylist if it's the minimum
			if (points.size() == 0) {
				points.add(newPoint);
				
			} else if (newPoint.y < points.get(0).y
					|| (newPoint.y == points.get(0).y 
							&& newPoint.x < points.get(0).x)) {
				Vector2f oldPoint = points.get(0);
				points.set(0, newPoint);
				points.add(oldPoint);
				
			} else {
				points.add(newPoint);
			}
		}
		
		List<Vector2f> hull = convexHull(points);
		
		float surfaceArea = areaOfHull(hull);
		
		float force = 0.5f * v.lengthSquared() * COEFFICIENT * surfaceArea; 
		
		System.out.println(v.lengthSquared());
		System.out.println(force);
		
		v.normalize();
		v.scale(-force);
		
		System.out.println(v);
		
		return new Vector3f();
		
	}
	
	public static void main(String[] args) {
		AirDrag ad = new AirDrag();
		
		ArrayList<Vector2f> points = new ArrayList<Vector2f>();
		
		/*points.add(new Vector2f(2, 1));
		points.add(new Vector2f(2, 3));
		points.add(new Vector2f(3, 4.5f));
		points.add(new Vector2f(4, 2));
		points.add(new Vector2f(4, 7));
		points.add(new Vector2f(1, 2));
		points.add(new Vector2f(1, 5));
		points.add(new Vector2f(1.5f, 7));*/
		
		points.add(new Vector2f(0, 0));
		points.add(new Vector2f(0, 3));
		points.add(new Vector2f(3, 3));
		points.add(new Vector2f(3, 0));
		points.add(new Vector2f(1, 2));
		points.add(new Vector2f(0, 1));
		
		List<Vector2f> newPoints = ad.convexHull(points);
		System.out.println(newPoints);
		
		System.out.println(ad.areaOfHull(newPoints));
	}
	
	private float areaOfHull(final List<Vector2f> hull) {
		float area = 0;
		Vector2f p = hull.get(0);
		
		for (int i = 2; i < hull.size(); i++) {
			// Area of triangle p0 - p(i-1) - p(i)
			Vector2f ab = new Vector2f();
			Vector2f ac = new Vector2f();
			
			ab.sub(hull.get(i-1), p);
			ac.sub(hull.get(i), p);
			
			area += 0.5f * (ab.x * ac.y - ac.x * ab.y);
		}
		
		return area;
	}
	
	private List<Vector2f> convexHull(final ArrayList<Vector2f> points) {
		Collections.sort(points, new Vector2fAngleCompare(points.get(0)));
		
		points.set(0, points.get(points.size() - 1));
		
		int m = 2;
		for (int i = 3; i < points.size(); i++) {
			try {
			while (i < points.size() - 1 && ccw(points.get(m - 1), points.get(m), points.get(i)) <= 0) {
				if (m == 2) {
					final Vector2f vec = points.get(m);
					points.set(m, points.get(i));
					points.set(i, vec);
					i++;
					
				} else {
					m--;
				}
			}
			} catch (Exception e) {
				System.out.println(e);
			}
			
			m++;
			
			final Vector2f vec = points.get(m);
			points.set(m, points.get(i));
			points.set(i, vec);
		}
		
		return points.subList(0, m+1);
	}
	
	private float ccw(final Vector2f v1, final Vector2f v2, final Vector2f v3) {
		return (v2.x - v1.x) * (v3.y - v1.y)- (v2.y - v1.y) * (v3.x - v1.x);  
	}
	

	private class Vector2fAngleCompare implements Comparator<Vector2f> {
		Vector2f base;
		
		public Vector2fAngleCompare(final Vector2f theBase) {
			base = theBase;
		}
		
		
		public int compare(Vector2f o1, Vector2f o2) {
			return (int) Math.signum(vecAngle(o1) - vecAngle(o2));
		}	
		
		private float vecAngle(final Vector2f vector) {
			final Vector2f v = new Vector2f();
			v.sub(vector, base);
			
			return v.y / (v.x * v.x + v.y * v.y);
		}
	}
}
