package tesseract.menuitems;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import tesseract.World;
import tesseract.objects.PlanarPolygon;

/**
 * Planar Polygon Menu Item.
 * 
 * @author Steve Bradshaw
 */
public class PlanarPolygonMenuItem extends TesseractMenuItem {

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * A Default radius.
	 */
	private static final float DEFAULT_RADIUS = 0.1f;

	/**
	 * Constructor for the menu item.
	 * 
	 * @param theWorld The world into which we add.
	 */
	public PlanarPolygonMenuItem(final World theWorld) {
		super(theWorld, "Planar Polygon");
	}
	
	/**
	 * Action handler.
	 * 
	 * @param arg0 Unused event info.
	 */
	public void actionPerformed(final ActionEvent arg0) {
		createParameterMenu();
		
		//If the default button is checked, the frame will close.
		final JCheckBox defaultButton = getDefaultButton();
		final JFrame params = getParamFrame();

		defaultButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if (defaultButton.isSelected()) {
					myWorld.addObject(new PlanarPolygon(new Vector3f(0,0,0), DEFAULT_RADIUS));
					params.dispose();
				}
			}
		});
		
		
		/*if(arg0. == true) {
			myWorld.addObject(new PlanarPolygon(new Vector3f(0,0,0), DEFAULT_RADIUS));
			params.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}*/
		
		/*Vector3f pos = 
			parseVector(JOptionPane.showInputDialog("Enter the position"));
		float radius = 
			Float.parseFloat(JOptionPane.showInputDialog("Enter the radius"));
		
		myWorld.addObject(new PlanarPolygon(pos, radius));*/
	}
}
