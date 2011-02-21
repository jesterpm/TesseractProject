package tesseract.menuitems;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import tesseract.World;
import tesseract.objects.Particle;

/**
 * Particle Menu Item.
 * 
 * @author Jesse Morgan
 * @deprecated By Phillip Cardon
 */
public class ParticleMenuItem extends TesseractMenuItem {

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for the menu item.
	 * 
	 * @param theWorld The world into which we add.
	 */
	public ParticleMenuItem(final World theWorld) {
		super(theWorld, "Particle");
	}
	
	/**
	 * Action handler.
	 * 
	 * @param arg0 Unused event info.
	 */
	public void actionPerformed(final ActionEvent arg0) {
		Color c = JColorChooser.showDialog(null, "Particle Color", Color.RED);
		
		Vector3f pos = 
			parseVector(JOptionPane.showInputDialog("Enter the position"));
		
		myWorld.addObject(new Particle(pos, new Color3f(c)));
	}

}
