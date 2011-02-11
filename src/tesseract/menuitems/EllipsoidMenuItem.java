package tesseract.menuitems;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import tesseract.World;
import tesseract.objects.Ellipsoid;
import tesseract.objects.PlanarPolygon;

/**
 * Ellipsoid Menu Item.
 * 
 * @author Steve Bradshaw
 */
public class EllipsoidMenuItem extends TesseractMenuItem {

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for the menu item.
	 * 
	 * @param theWorld The world into which we add.
	 */
	public EllipsoidMenuItem(final World theWorld) {
		super(theWorld, "Ellipsoid");
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
					myWorld.addObject(new Ellipsoid(getDefaultPosition(), getDefaultRadius()));
					params.dispose();
				}
			}
		});
		
		
		
		
		/*Vector3f pos = 
			parseVector(JOptionPane.showInputDialog("Enter the position"));
		float radius = 
			Float.parseFloat(JOptionPane.showInputDialog("Enter the radius"));
		
		myWorld.addObject(new Ellipsoid(pos, radius));*/
	}
}
