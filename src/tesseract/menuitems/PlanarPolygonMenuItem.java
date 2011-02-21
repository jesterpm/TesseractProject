package tesseract.menuitems;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import tesseract.World;
import tesseract.objects.Ellipsoid;
import tesseract.objects.PlanarPolygon;

/**
 * Planar Polygon Menu Item.
 * 
 * @author Steve Bradshaw
 * @deprecated By Phillip Cardon
 */
public class PlanarPolygonMenuItem extends TesseractMenuItem {

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 1L;
	
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
		final JButton enterButton = getEnterButton();

		defaultButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent ev) {
				if (defaultButton.isSelected()) {
					myWorld.addObject(new PlanarPolygon(getDefaultPosition(), getDefaultRadius()));
					params.dispose();
				}
			}
		});
		
		enterButton.addActionListener(new ActionListener() {
			final int divs = 5;
			public void actionPerformed(final ActionEvent event) {
					String string = getPositionField().getText();
					Vector3f pos = parseVector(string);
					setPosition(pos);
				
					String string2 = getRadiusField().getText();
					float radius = Float.parseFloat(string2);
					setRadius(radius);

					String string3 = getMassField().getText();
					float mass = Float.parseFloat(string3);
					setMass(mass);
	
				if (event.getSource() == enterButton) {
					myWorld.addObject(new PlanarPolygon(getPosition(), getMass(), getRadius(), divs));
					params.dispose();
				}
			}
		});
	}
}
