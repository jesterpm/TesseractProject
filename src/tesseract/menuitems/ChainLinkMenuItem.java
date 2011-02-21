package tesseract.menuitems;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import tesseract.World;
import tesseract.objects.ChainLink;
import tesseract.objects.ChainLink2;
import tesseract.objects.Icosahedron;
import tesseract.objects.PlanarPolygon;

/**
 * Icosahedron Menu Item.
 * 
 * @author Steve Bradshaw
 * @deprecated By Phillip Cardon
 */
public class ChainLinkMenuItem extends TesseractMenuItem {

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for the menu item.
	 * 
	 * @param theWorld The world into which we add.
	 */
	public ChainLinkMenuItem(final World theWorld) {
		super(theWorld, "ChainLink");
	}
	
	/**
	 * Action handler.
	 * 
	 * @param arg0 Unused event info.
	 */
	public void actionPerformed(final ActionEvent arg0) {
		createParameterMenu();
		
		final float scale = 0.5f;
		//final float sliceRadius = .06f;  //inside whole
		//final int sliceDivisions = 50;
		//final float arcRadius = .08f; //outside whole
		//final int arcDivisions = 30;
		
		//If the default button is checked, the frame will close.
		final JCheckBox defaultButton = getDefaultButton();
		final JFrame params = getParamFrame();
		final JButton enterButton = getEnterButton();

		defaultButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if (defaultButton.isSelected()) {
					myWorld.addObject(new ChainLink2(getDefaultPosition(), 1, getDefaultRadius()));
					params.dispose();
					/*myWorld.addObject(new ChainLink(getDefaultPosition(), 1, scale,
							sliceRadius, sliceDivisions, arcRadius, arcDivisions));
					params.dispose();*/
				}
			}
		});
		
		enterButton.addActionListener(new ActionListener() {
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
					myWorld.addObject(new ChainLink2(getPosition(), getMass(), getRadius()));
					params.dispose();
				}
			}
		});
	}
}


