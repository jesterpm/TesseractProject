package tesseract.objects.blimp;

/*
 * Class PlanarPolygon
 * TCSS 491 Computational Worlds
 * Steve Bradshaw
 */


import javax.media.j3d.Appearance;
import javax.media.j3d.Geometry;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TriangleFanArray;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.TexCoord2f;
import javax.vecmath.Vector3f;

import tesseract.objects.PhysicalObject;

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
public class BlimpFin extends PhysicalObject {
	
	/**
	 * Number of divisions in the sphere.
	 */
	public static final int DEFAULT_DIVISIONS = 4;
	
	/**
	 * The appearance of this fin
	 */
	private Appearance my_appearance;
	
	/**
	 * Create a new Ellipsoid.
	 * 
	 * @param position Initial position.
	 * @param mass Initial mass.
	 * @param radius the radius of the base sphere.
	 * @param divisions an in for the shape divisions.
	 */
	public BlimpFin( final float mass, final Vector3f position,
			final float radius, final Appearance appearance) {
		super(position, mass);
		
		my_appearance = appearance;
		
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
		Shape3D fin = new Shape3D(gInfo.getGeometryArray());

		PolygonAttributes polyAttr = new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE, 0);
		my_appearance.setPolygonAttributes(polyAttr);		
		geometry.setCapability(Geometry.ALLOW_INTERSECT);
		fin =  new Shape3D(geometry, my_appearance);
		Transform3D rotate = new Transform3D();
		rotate.rotY(Math.PI / 4);
		TransformGroup tg = new TransformGroup(rotate);
		tg.addChild(fin);
		return tg;
	}
}

