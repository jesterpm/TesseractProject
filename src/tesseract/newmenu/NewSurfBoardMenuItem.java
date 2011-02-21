package tesseract.newmenu;

import java.awt.event.ActionEvent;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Material;

import tesseract.World;
import tesseract.objects.Ellipsoid;

import com.sun.j3d.utils.geometry.Sphere;

/**
 * SurfBoard Menu Item.
 * 
 * @author Steve Bradshaw
 * @author Phillip cardon
 */
public class NewSurfBoardMenuItem extends MenuItem {

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for the menu item.
	 * 
	 * @param theWorld The world into which we add.
	 */
	public NewSurfBoardMenuItem(final World theWorld) {
		super(theWorld, "Surf Board");
	}
	
	/**
	 * Action handler.
	 * 
	 * @param arg0 Unused event info.
	 */
	public void actionPerformed(final ActionEvent arg0) {
		Appearance eApp = new Appearance();
		Material eggMat = new Material();
		eggMat.setDiffuseColor(0f, .5f, 1f);
		eApp.setMaterial(eggMat);
		eApp.setColoringAttributes(new ColoringAttributes(
				0f, 1f, 1f, ColoringAttributes.ALLOW_COLOR_WRITE));
		myWorld.addObject(new Ellipsoid(getPosition(), 1, 0.05f,
				new Sphere().getPrimitiveFlags(), 40, eApp, 0.2f, 4.0f));
	}
}
