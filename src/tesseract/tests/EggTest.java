/*
 * This is just a test for the Ellipsoid
 * Author:  Steve Bradshaw
 */
package tesseract.tests;

import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;

import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;

import tesseract.objects.Ellipsoid;

//import tesseract.objects.Ellipsoid;

@SuppressWarnings("restriction")
public class EggTest {
	private JFrame appFrame;
	private MouseEvent lastDragEvent;
	private Transform3D icc3D;
	private TransformGroup iccTG;
	
	private double[] m_VertexArray = { 1, 1, 0, //0
		      0, 3, 0, //1
		      1, 5, 0, //2
		      2, 4, 0, //3
		      4, 5, 0, //4
		      3, 3, 0, //5
		      4, 2, 0, //6
		      4, 0, 0, //7
		      3, 0, 0, //8
		      2, 1, 0, //9
		      // these are vertices for the hole
		      1, 3, 0, //10
		      2, 3, 0, //11
		      3, 2, 0, //12
		      3, 1, 0, //13
		      2, 2, 0 };//14
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				(new EggTest()).createAndShowGUI();
			}
		});
	}

	private void createAndShowGUI() {
		BranchGroup scene = new BranchGroup();
		Light light = new DirectionalLight(new Color3f(1f, 1f, 1f), new Vector3f(-1f, -1f, -1f));
		light.setInfluencingBounds(new BoundingSphere());
		scene.addChild(light);
		light = new DirectionalLight(new Color3f(0.3f, 0.1f, 0.1f), new Vector3f(1f, 0f, 0f));
		light.setInfluencingBounds(new BoundingSphere());
		scene.addChild(light);
		icc3D = new Transform3D();
		iccTG = new TransformGroup();
		iccTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		iccTG.addChild(createEllipsoid());
		scene.addChild(iccTG);
		scene.compile();
		
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas3D = new Canvas3D(config);
		SimpleUniverse simpleU = new SimpleUniverse(canvas3D);
		simpleU.getViewingPlatform().setNominalViewingTransform();
		simpleU.addBranchGraph(scene);

		appFrame = new JFrame("Java 3D Demo 6");
		appFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		appFrame.add(canvas3D);
		appFrame.pack();
		if (Toolkit.getDefaultToolkit().isFrameStateSupported(JFrame.MAXIMIZED_BOTH))
			appFrame.setExtendedState(appFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		
		canvas3D.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (lastDragEvent != null) {
					Transform3D newRotate = new Transform3D();
					newRotate.rotX(Math.toRadians(e.getY() - lastDragEvent.getY()) / 2);
					Transform3D tmp = new Transform3D();
					tmp.rotY(Math.toRadians(e.getX() - lastDragEvent.getX()) / 2);
					newRotate.mul(tmp);
					newRotate.mul(icc3D);
					icc3D = newRotate;
					iccTG.setTransform(icc3D);
				}
				lastDragEvent = e;
			}
			public void mouseMoved(MouseEvent e) {
				lastDragEvent = null;
			}});
		appFrame.setVisible(true);
	}
	
	private Group createEllipsoid() {
		
		TransformGroup ellipsoidTG = new TransformGroup();
		Appearance eApp = new Appearance();
		Material eggMat = new Material();
		eggMat.setDiffuseColor(0f, .8f, 1f);
		eApp.setMaterial(eggMat);
		eApp.setColoringAttributes(new ColoringAttributes(0f, 1f, 1f, ColoringAttributes.ALLOW_COLOR_WRITE));
		Vector3f position = new Vector3f(0.2f,0,0);
		
		//Test for first constructor.
		Ellipsoid egg = new Ellipsoid(position, 1, 0.1f, new Sphere().getPrimitiveFlags(), 100, eApp, 0.2f, 4.0f);
		//Test for second constructor.
		//Ellipsoid egg = new Ellipsoid(position, 0.3f);
		
		//unlike the basic sphere or cube etc., you must use a getter or will throw exception
		ellipsoidTG.addChild(egg.getGroup());
		
		BranchGroup bg = new BranchGroup();
		bg.addChild(ellipsoidTG);
		
		return bg;
	}
	
	private Group createPlanar() {
		
		TransformGroup objTrans = new TransformGroup();
		// triangulate the polygon
	    GeometryInfo gi = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);

	    gi.setCoordinates(m_VertexArray);

	    int[] stripCountArray = { 10, 5 };
	    int[] countourCountArray = { stripCountArray.length };

	    gi.setContourCounts(countourCountArray);
	    gi.setStripCounts(stripCountArray);

	    Triangulator triangulator = new Triangulator();
	    triangulator.triangulate(gi);

	    NormalGenerator normalGenerator = new NormalGenerator();
	    normalGenerator.generateNormals(gi);

	    // create an appearance
	    Appearance ap = new Appearance();

	    // render as a wireframe
	    PolygonAttributes polyAttrbutes = new PolygonAttributes();
	    polyAttrbutes.setPolygonMode(PolygonAttributes.POLYGON_LINE);
	    polyAttrbutes.setCullFace(PolygonAttributes.CULL_NONE);
	    ap.setPolygonAttributes(polyAttrbutes);

	    // add both a wireframe and a solid version
	    // of the triangulated surface
	    Shape3D shape1 = new Shape3D(gi.getGeometryArray(), ap);
	    Shape3D shape2 = new Shape3D(gi.getGeometryArray());

	    objTrans.addChild(shape1);
	    objTrans.addChild(shape2);
	   //objRoot.addChild(objTrans);

	    return objTrans;
	}
	
	
	

}
