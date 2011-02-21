package tesseract.menuitems;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.vecmath.Vector3f;

import tesseract.World;
import tesseract.objects.Ellipsoid;

/**
 * Ellipsoid Menu Item.
 * 
 * @author Steve Bradshaw
 * @deprecated By Phillip Cardon
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
		final JButton enterButton = getEnterButton();
		
		defaultButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent ev) {
				if (defaultButton.isSelected()) {
					myWorld.addObject(new Ellipsoid(getDefaultPosition(), getDefaultRadius()));
					params.dispose();
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
					myWorld.addObject(new Ellipsoid(getPosition(), getRadius()));
					params.dispose();
				}
			}
		});

	}
}
