package common;

import com.sun.j3d.utils.image.*;
import javax.media.j3d.*;
import javax.vecmath.*;

@SuppressWarnings("restriction")
public class Earth extends Sphere {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6696594698875803920L;
	protected float radius;
	
	public Earth(float radius, Vector3f position) {
		this(1, radius, position);
	}
	
	public Earth(float mass, float radius, Vector3f position) {
		super(mass, radius, position);
	}

	protected Node createShape(float radius, int divisions) {
		TextureLoader tl = new TextureLoader("earth.png", null);
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
		appearance.setMaterial(new Material());
		return new com.sun.j3d.utils.geometry.Sphere(radius, com.sun.j3d.utils.geometry.Sphere.GENERATE_NORMALS | com.sun.j3d.utils.geometry.Sphere.GENERATE_TEXTURE_COORDS, divisions, appearance);
	}
}
