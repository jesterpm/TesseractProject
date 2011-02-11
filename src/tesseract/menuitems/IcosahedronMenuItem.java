package tesseract.menuitems;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import tesseract.World;
import tesseract.objects.Icosahedron;
import tesseract.objects.PlanarPolygon;

/**
 * Planar Polygon Menu Item.
 * 
 * @author Steve Bradshaw
 */
public class IcosahedronMenuItem extends TesseractMenuItem {

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for the menu item.
	 * 
	 * @param theWorld The world into which we add.
	 */
	public IcosahedronMenuItem(final World theWorld) {
		super(theWorld, "Icosahedron");
	}
	
	/**
	 * Action handler.
	 * 
	 * @param arg0 Unused event info.
	 */
	public void actionPerformed(final ActionEvent arg0) {
		//Color c = JColorChooser.showDialog(null, "Planar Polygon Color", Color.RED);
		Vector3f pos = 
			parseVector(JOptionPane.showInputDialog("Enter the position"));
		float radius = 
			Float.parseFloat(JOptionPane.showInputDialog("Enter the radius"));
		
		myWorld.addObject(new Icosahedron(pos, radius));
	}
}

