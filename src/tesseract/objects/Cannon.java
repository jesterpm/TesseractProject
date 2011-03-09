package tesseract.objects;

import javax.media.j3d.IndexedQuadArray;
import javax.media.j3d.PointArray;
import javax.media.j3d.Transform3D;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

public class Cannon extends PhysicalObject{

	public Cannon(Vector3f thePosition, float mass) {
		super(thePosition, mass);
		// TODO Auto-generated constructor stub
		buildShape(14, 7f, 2f, 2f);
	}
	
	private void buildShape(final int theDivisions, final float length,
			final float breechRadius, final float gunThickness) {
		Point3f[] coordinates = new Point3f[theDivisions * 4];
		final float barrelRadius = breechRadius + gunThickness;
		final float arcAngle = (float) (Math.PI * 2.0);
		final float gunDivAngle = 2 * (float) Math.PI / theDivisions;
		Transform3D center = new Transform3D();
		
		for (int i = 0; i < theDivisions; i++) {
			coordinates[i] = new Point3f(breechRadius
					* (float) Math.cos(i * gunDivAngle), 0f, breechRadius 
					* (float) Math.sin(i * gunDivAngle));
		}
		
		for (int i = theDivisions; i < theDivisions * 2; i++) {
			coordinates[i] = new Point3f(breechRadius
					* (float) Math.cos(i * gunDivAngle), length, breechRadius 
					* (float) Math.sin(i * gunDivAngle));
		}
		
		for (int i = theDivisions * 2; i < theDivisions * 3; i++) {
			coordinates[i] = new Point3f(barrelRadius
					* (float) Math.cos(i * gunDivAngle), 0f, barrelRadius 
					* (float) Math.sin(i * gunDivAngle));
		}
		
		for (int i = theDivisions * 3; i < theDivisions * 4; i++) {
			coordinates[i] = new Point3f(barrelRadius
					* (float) Math.cos(i * gunDivAngle), length, barrelRadius 
					* (float) Math.sin(i * gunDivAngle));
		}
		
		IndexedQuadArray geometry = new IndexedQuadArray(theDivisions * 4, PointArray.COORDINATES, 
				theDivisions * 4);
		int topInsideStop = 0;
		int index = 0;
		for (int i = 0; i < theDivisions; i++) {
			geometry.setCoordinate(index++, coordinates[i]);
			geometry.setCoordinate(index++, coordinates[(theDivisions + i) % (2 * theDivisions)]);
			geometry.setCoordinate(index++, coordinates[(theDivisions + i + 1) % (2 * theDivisions)]);
			geometry.setCoordinate(index++, coordinates[(i + 1) % theDivisions]);
		}
		
		for (int i = theDivisions * 2; i < theDivisions * 3; i++) {
			geometry.setCoordinate(index++, coordinates[i]);
			geometry.setCoordinate(index++, coordinates[(theDivisions + i) % (4 * theDivisions)]);
			geometry.setCoordinate(index++, coordinates[(theDivisions + i + 1) % (4 * theDivisions)]);
			geometry.setCoordinate(index++, coordinates[((i + 1) % theDivisions) + theDivisions * 2]);
		}
		
		for (int i = 0; i < theDivisions; i++) {
			geometry.setCoordinate(index++, coordinates[i]);
			geometry.setCoordinate(index++, coordinates[i + 3 * theDivisions]);
			geometry.setCoordinate(index++, coordinates[(1 + i + 3 * theDivisions) % theDivisions]);
			geometry.setCoordinate(index++, coordinates[(i + 1) % theDivisions]);
		}
	}
}
