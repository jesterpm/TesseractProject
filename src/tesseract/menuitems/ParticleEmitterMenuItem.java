package tesseract.menuitems;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.vecmath.Vector3f;

import tesseract.World;
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
		Vector3f pos = 
			parseVector(JOptionPane.showInputDialog("Enter the position"));
		
		float freq = Float.parseFloat(
					JOptionPane.showInputDialog(
							"Emission Frequency (seconds)", "1"));
		
		myWorld.addObject(new ParticleEmitter(pos, freq, null));
	}

}
