package tesseract.objects;

import javax.media.j3d.Appearance;
import javax.media.j3d.IndexedQuadArray;
import javax.media.j3d.Material;
import javax.media.j3d.PointArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;

/**
 * Chain link object.
 * 
 * @author Jesse Morgan
 */
public class ChainLink2 extends PhysicalObject {
	/**
	 * Default link length.
	 */
	public static final float DEFAULT_LENGTH = 0.15f;
	
	/**
	 * Default ratio of length to width.
	 */
	public static final float DEFAULT_WIDTH_RATIO = 0.75f;
	
	/**
	 * Default ratio of length to link diameter.
	 */
	public static final float DEFAULT_DIAMETER_RATIO = 0.25f;
	
	/**
	 * Number of slices to render.
	 */
	protected static final int SLICE_COUNT = 40;
	
	/**
	 * Number of points in the circumference of the circle.
	 */
	protected static final int SLICE_DIVISIONS = 16;
	
	/**
	 * Construct a chain link.
	 * 
	 * @param thePosition Position.
	 * @param mass Mass.
	 */
	public ChainLink2(final Vector3f thePosition, final float mass) {
		this(thePosition, mass, DEFAULT_LENGTH * DEFAULT_DIAMETER_RATIO,
				DEFAULT_LENGTH, DEFAULT_LENGTH * DEFAULT_WIDTH_RATIO);
	}
	
	/**
	 * Construct a chain link.
	 * 
	 * @param thePosition Position.
	 * @param mass Mass.
	 * @param length Link length.
	 * 		Width and diameter are created from the default ratios.
	 */
	public ChainLink2(final Vector3f thePosition, final float mass,
			final float length) {
		this(thePosition, mass, length * DEFAULT_DIAMETER_RATIO,
				length, length * DEFAULT_WIDTH_RATIO);
	}
	
	/**
	 * Construct a Chain Link.
	 * 
	 * @param thePosition Position.
	 * @param mass Mass.
	 * @param diameter Diameter of link.
	 * @param length Length of link.
	 * @param width Width of link.
	 */
	public ChainLink2(final Vector3f thePosition, final float mass,
			final float diameter, final float length, final float width) {
		super(thePosition, mass);
		
		setShape(createShape(SLICE_COUNT, SLICE_DIVISIONS,
				diameter, length, width));
		
		if (inverseMass != 0) {
			final float radius2 = diameter / 2.0f * diameter / 2.0f;
			inverseInertiaTensor.m00 = 1 / (8.0f * inverseMass) * (4 * radius2 + 5 * (length * length + width * width));
			inverseInertiaTensor.m11 = inverseInertiaTensor.m00;
			inverseInertiaTensor.m22 = 1 / (inverseMass) * (radius2 + 0.75f * (length * length + width * width));
			inverseInertiaTensor.invert();
		}
	}

	/**
	 * Create the shape.
	 * 
	 * @param sliceCount Number of slices.
	 * @param sliceDivisions Number of divisions.
	 * @param diameter Diameter.
	 * @param length Length.
	 * @param width Width.
	 * 
	 * @return Chainlink shape.
	 */
	public Shape3D createShape(final int sliceCount, final int sliceDivisions,
			final float diameter, final float length, final float width) {
		
		Point3f[][] coords = new Point3f[sliceCount][sliceDivisions];
		
		// Design the first circle
		double theta = 2 * Math.PI / sliceDivisions;
		float radius = diameter / 2.0f;
		
		for (int i = 0; i < sliceDivisions; i++) {
			coords[0][i] = new Point3f(
					0f,
					(float) (radius * Math.cos(i * theta)),
					(float) (radius * Math.sin(i * theta)));
		}
		
		// Create the arc
		radius = (width - radius) / 2.0f;
		
		Transform3D t3d = new Transform3D();
		t3d.setIdentity();
		t3d.setTranslation(new Vector3f(0, -radius, 0));
		
		Transform3D tmp = new Transform3D();
		tmp.rotZ(Math.PI / (sliceCount / 2.0 - 1));
		t3d.mul(tmp);
		tmp.setIdentity();
		tmp.setTranslation(new Vector3f(0, radius, 0));
		t3d.mul(tmp);
		
		for (int j = 1; j < sliceCount / 2; j++) {
			for (int i = 0; i < sliceDivisions; i++) {
				coords[j][i] = new Point3f(coords[j - 1][i]);
				t3d.transform(coords[j][i]);
			}
		}
		
		// Next build second half...
		t3d.setIdentity();
		t3d.setTranslation(new Vector3f(
				-(length  / 2.0f - radius / 2.0f),
				(width - diameter / 2.0f) / 2.0f,
				0));
		
		Transform3D rot = new Transform3D();
		rot.rotY(Math.PI);
		tmp.setIdentity();
		tmp.rotX(Math.PI);
		rot.mul(tmp);
		for (int j = 0; j < sliceCount / 2; j++) {
			for (int i = 0; i < sliceDivisions; i++) {
				t3d.transform(coords[j][i]);
				
				coords[j + sliceCount / 2][i] = new Point3f(coords[j][i]);
				rot.transform(coords[j + sliceCount / 2][i]);
			}
		}
		
		
		// Build Geoemetry
		IndexedQuadArray geometry = new IndexedQuadArray(
				sliceCount * sliceDivisions,
				PointArray.COORDINATES,
				4 * sliceDivisions * sliceCount);
		
		for (int i = 0; i < sliceCount; i++) {
			geometry.setCoordinates(i * sliceDivisions, coords[i]);
		}
		
		int index = 0;
		for (int j = 0; j < (sliceCount - 1); j++) {
			for (int i = 0; i < sliceDivisions; i++) {
				geometry.setCoordinateIndex(index++, j * sliceDivisions + i);
				geometry.setCoordinateIndex(index++,
						(j + 1) * sliceDivisions + i);
				geometry.setCoordinateIndex(index++,
						(j + 1) * sliceDivisions + (i + 1) % sliceDivisions);
				geometry.setCoordinateIndex(index++,
						j * sliceDivisions + (i + 1) % sliceDivisions);
			}
		}
		
		for (int i = 0; i < sliceDivisions; i++) {
			geometry.setCoordinateIndex(index++,
					(sliceCount - 1) * sliceDivisions + i);
			geometry.setCoordinateIndex(index++,
					i);
			geometry.setCoordinateIndex(index++,
					(i + 1) % sliceDivisions);
			geometry.setCoordinateIndex(index++,
					(sliceCount - 1) * sliceDivisions 
						+ (i + 1) % sliceDivisions);
		}
		
		GeometryInfo gInfo = new GeometryInfo(geometry);
		gInfo.convertToIndexedTriangles();
		new NormalGenerator().generateNormals(gInfo);
		
		Shape3D shape = new Shape3D(gInfo.getGeometryArray());
		Appearance app = new Appearance();
		Material mat = new Material();
		mat.setDiffuseColor(1, 0, 0);
		app.setMaterial(mat);
		shape.setAppearance(app);
		
		return shape;		
	}

}
