package tesseract.menuitems;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import tesseract.World;
import tesseract.objects.PlanarPolygon;
import tesseract.objects.emitters.ParticleEmitter;

/**
 * Particle Emitter Menu Item.
 * 
 * @author Jesse Morgan
 */
public class ParticleEmitterMenuItem extends TesseractMenuItem {

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for the menu item.
	 * 
	 * @param theWorld The world into which we add.
	 */
	public ParticleEmitterMenuItem(final World theWorld) {
		super(theWorld, "Particle Emitter");
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
					myWorld.addObject(new ParticleEmitter(new Vector3f(0f,.49f, 0f),
							.5f, new Color3f(1f,0f,0f)));
					params.dispose();
				}
			}
		});
		
		/*Vector3f pos = 
			parseVector(JOptionPane.showInputDialog("Enter the position"));
		
		float freq = Float.parseFloat(
					JOptionPane.showInputDialog(
							"Emission Frequency (seconds)", "1"));
		
		myWorld.addObject(new ParticleEmitter(pos, freq, null));*/
	}

}
