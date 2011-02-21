/*
 * Class PlanarPolygon
 * TCSS 491 Computational Worlds
 * Steve Bradshaw
 */

package tesseract.objects;

import javax.media.j3d.Appearance;
import javax.media.j3d.Geometry;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TriangleFanArray;
import javax.vecmath.Point3f;
import javax.vecmath.TexCoord2f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.image.TextureLoader;

/**
 * This class creates an planar polygon using Mathew Aldens design
 * of a circle, but this adds lava texture and lower divisions
 * 
 * @author Steve Bradshaw & Mathew Alden
 * @version 8 Feb 2011
 */
public class PlanarPolygon extends PhysicalObject {
	
	/**
	 * Default mass.
	 */
	private static final float DEFAULT_MASS = Float.POSITIVE_INFINITY;
	
	/**
	 * Number of divisions in the sphere.
	 */
	public static final int DEFAULT_DIVISIONS = 6;
	
	/**
	 * A Default radius.
	 */
	public static final float DEFAULT_RADIUS = 0.1f;
	
	/**
	 * Create a new Ellipsoid.
	 * 
	 * @param position Initial position.
	 * @param mass Initial mass.
	 * @param radius the radius of the base sphere.
	 * @param divisions an in for the shape divisions.
	 */
	public PlanarPolygon(final Vector3f position, final float mass,
			final float radius, final int divisions) {
		super(position, mass);
		
		setShape(createShape(radius, divisions));
		
		if (inverseMass != 0) {
			inverseInertiaTensor.m00 = 1f / 4 / inverseMass * radius * radius;
			inverseInertiaTensor.m11 = 2 * inverseInertiaTensor.m00;
			inverseInertiaTensor.m22 = inverseInertiaTensor.m00;
			inverseInertiaTensor.invert();
		}
		updateTransformGroup();
	}
	/**
	 * Create a new Ellipsoid.
	 * 
	 * @param position Initial position.
	 * @param radius a float for the size of the base sphere.
	 */
	public PlanarPolygon(final Vector3f position, final float radius) {
		super(position, DEFAULT_MASS);
		
		setShape(createShape(radius, DEFAULT_DIVISIONS));
		
		if (inverseMass != 0) {
			inverseInertiaTensor.m00 = 1f / 4 / inverseMass * radius * radius;
			inverseInertiaTensor.m11 = 2 * inverseInertiaTensor.m00;
			inverseInertiaTensor.m22 = inverseInertiaTensor.m00;
			inverseInertiaTensor.invert();
		}
		updateTransformGroup();

	}

	/**
	 * This method creates a planar polygon shape with lava texture.
	 * 
	 * @param radius a float for the size of the base polygon.
	 * @param divisions an int for the number of divisons.
	 * @param appearance an Appearance object.
	 * @return Node a polygon.
	 */
	private Node createShape(final float radius, final int divisions) {
		TriangleFanArray geometry = new TriangleFanArray(divisions,
				TriangleFanArray.COORDINATES | TriangleFanArray.TEXTURE_COORDINATE_2, new int[] {divisions});
		for (int i = 0; i < divisions; i++) {
			float baseX = (float)Math.cos(2 * Math.PI * i / divisions);
			float baseZ = -(float)Math.sin(2 * Math.PI * i / divisions);
			geometry.setCoordinate(i, new Point3f(radius * baseX, 0, radius * baseZ));
			geometry.setTextureCoordinate(0, i, new TexCoord2f((baseX + 1) / 2, (-baseZ + 1) / 2));
		}
		
		GeometryInfo gInfo = new GeometryInfo(geometry);
		new NormalGenerator().generateNormals(gInfo);
		gInfo.convertToIndexedTriangles();
		Shape3D polygon = new Shape3D(gInfo.getGeometryArray());
		
		TextureLoader tl = new TextureLoader("lava.jpg", null);
		ImageComponent2D image = tl.getImage();
		int width = image.getWidth();
		int height = image.getHeight();
		Texture2D texture = new Texture2D(Texture.MULTI_LEVEL_MIPMAP, Texture.RGB, width, height);
		
		int imageLevel = 0;
		texture.setImage(imageLevel, image);
		while (width > 1 || height > 1) {
			imageLevel++;
			if (width > 1) width /= 2;
			if (height > 1) height /= 2;
			texture.setImage(imageLevel, tl.getScaledImage(width, height));
		}
		texture.setMagFilter(Texture2D.NICEST);
		texture.setMinFilter(Texture2D.NICEST);
		Material mat = new Material();
		mat.setDiffuseColor(1, 0, 0);
		
		Appearance appearance = new Appearance();
		appearance.setTexture(texture);
		appearance.setMaterial(mat);
		PolygonAttributes polyAttr = new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE, 0);
		appearance.setPolygonAttributes(polyAttr);		
		geometry.setCapability(Geometry.ALLOW_INTERSECT);
		polygon =  new Shape3D(geometry, appearance);
		return polygon;
	}
}
