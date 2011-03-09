package common;

import com.sun.j3d.utils.image.*;
import javax.media.j3d.*;
import javax.vecmath.*;

@SuppressWarnings("restriction")
public class Circle extends Polygon {
	/**
	 * 
	 */
	private static final long serialVersionUID = 936268321291207044L;

	public Circle(float radius, Vector3f position, Vector3f normal) {
		this(1, radius, position, normal);
	}

	public Circle(float mass, float radius, Vector3f position, Vector3f normal) {
		super(mass, position, normal);
		setShape(createShape(radius, 22));
		if (inverseMass != 0) {
			inverseInertiaTensor.m00 = 1f / 4 / inverseMass * radius * radius;
			inverseInertiaTensor.m11 = 2 * inverseInertiaTensor.m00;
			inverseInertiaTensor.m22 = inverseInertiaTensor.m00;
			inverseInertiaTensor.invert();
		}
		updateTransformGroup();
	}
	
	protected Node createShape(float radius, int divisions) {
		TriangleFanArray geometry = new TriangleFanArray(divisions, TriangleFanArray.COORDINATES | TriangleFanArray.TEXTURE_COORDINATE_2, new int[] {divisions});
		for (int i = 0; i < divisions; i++) {
			float baseX = (float)Math.cos(2 * Math.PI * i / divisions);
			float baseZ = -(float)Math.sin(2 * Math.PI * i / divisions);
			geometry.setCoordinate(i, new Point3f(radius * baseX, 0, radius * baseZ));
			geometry.setTextureCoordinate(0, i, new TexCoord2f((baseX + 1) / 2, (-baseZ + 1) / 2));
		}

		TextureLoader tl = new TextureLoader("wood.jpg", null);
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

		Appearance appearance = new Appearance();
		appearance.setTexture(texture);
		PolygonAttributes polyAttr = new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE, 0);
		appearance.setPolygonAttributes(polyAttr);		
		return new Shape3D(geometry, appearance);
	}
}
