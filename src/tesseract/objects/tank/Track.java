package tesseract.objects.tank;

import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Material;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;

public class Track {
	
	private Track(){}
	
	public static Shape3D makeTrack(final float theScale, final Color theColor,
			final Transform3D toMove) {
		Point3f[] points = new Point3f[8];
		points[0] = new Point3f(-2f, .75f, .25f);
		points[1] = new Point3f(-2f, .75f, -.25f);
		points[2] = new Point3f(2f, .75f, -.25f);
		points[3] = new Point3f(2f, .75f, .25f);
		points[4] = new Point3f(-1.25f, -.25f, .25f);
		points[5] = new Point3f(-1.25f, -.25f, -.25f);
		points[6] = new Point3f(1.25f, -.25f, -.25f);
		points[7] = new Point3f(1.25f, -.25f, .25f);
		for (int it = 0; it < points.length; it++) {
			points[it].scale(theScale);
		}
		for (int it = 0; it < points.length; it++) {
			toMove.transform(points[it]);
		}
		QuadArray track = new QuadArray(24, GeometryArray.COORDINATES);
		int index = 0;
		track.setCoordinate(index++, points[3]);
		track.setCoordinate(index++, points[2]);
		track.setCoordinate(index++, points[1]);
		track.setCoordinate(index++, points[0]);
		
		track.setCoordinate(index++, points[4]);
		track.setCoordinate(index++, points[5]);
		track.setCoordinate(index++, points[6]);
		track.setCoordinate(index++, points[7]);
		
		track.setCoordinate(index++, points[4]);
		track.setCoordinate(index++, points[7]);
		track.setCoordinate(index++, points[3]);
		track.setCoordinate(index++, points[0]);
		
		track.setCoordinate(index++, points[1]);
		track.setCoordinate(index++, points[2]);
		track.setCoordinate(index++, points[6]);
		track.setCoordinate(index++, points[5]);
		
		track.setCoordinate(index++, points[0]);
		track.setCoordinate(index++, points[1]);
		track.setCoordinate(index++, points[5]);
		track.setCoordinate(index++, points[4]);
		
		track.setCoordinate(index++, points[2]);
		track.setCoordinate(index++, points[3]);
		track.setCoordinate(index++, points[7]);
		track.setCoordinate(index++, points[6]);

		//TransformGroup trans = new TransformGroup();
		NormalGenerator norms = new NormalGenerator(120);
		GeometryInfo geo = new GeometryInfo(track);
		norms.generateNormals(geo);
		
		Shape3D mesh = new Shape3D(geo.getGeometryArray());
		Appearance meshApp = new Appearance();
		Material surface = new Material();
		surface.setDiffuseColor(new Color3f (theColor));
		meshApp.setMaterial(surface);
		meshApp.setColoringAttributes(new ColoringAttributes(new Color3f (theColor),
				ColoringAttributes.NICEST));
		mesh.setAppearance(meshApp);
		//trans.addChild(mesh);
		//trans.addChild(new Shape3D(die2));
		//trans.addChild(new Shape3D(points));
		return mesh;
	}
}
