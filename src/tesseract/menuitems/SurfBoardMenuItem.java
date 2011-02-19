package tesseract.menuitems;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Material;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Sphere;

import tesseract.World;
import tesseract.objects.Ellipsoid;
import tesseract.objects.PlanarPolygon;

/**
 * Ellipsoid Menu Item.
 * 
 * @author Steve Bradshaw
 */
public class SurfBoardMenuItem extends TesseractMenuItem {

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for the menu item.
	 * 
	 * @param theWorld The world into which we add.
	 */
	public SurfBoardMenuItem(final World theWorld) {
		super(theWorld, "SurfBoard");
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
		//final JButton enterButton = getEnterButton();
		
		defaultButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent ev) {
				if (defaultButton.isSelected()) {
					Appearance eApp = new Appearance();
					Material eggMat = new Material();
					eggMat.setDiffuseColor(0f, .5f, 1f);
					eApp.setMaterial(eggMat);
					eApp.setColoringAttributes(new ColoringAttributes(0f, 1f, 1f, ColoringAttributes.ALLOW_COLOR_WRITE));
					myWorld.addObject(new Ellipsoid(getPosition(), 1, 0.05f, new Sphere().getPrimitiveFlags(), 40, eApp, 0.2f, 4.0f));
					params.dispose();
				}
			}
		});
		
		/*enterButton.addActionListener(new ActionListener() {
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
		});*/

	}
}
