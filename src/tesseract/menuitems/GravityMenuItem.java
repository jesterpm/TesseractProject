package tesseract.menuitems;

import java.awt.event.ActionEvent;

import tesseract.World;
import tesseract.forces.Gravity;

/**
 * Gravity Menu Item.
 * 
 * @author Steve Bradshaw
 */
public class GravityMenuItem extends TesseractMenuItem {

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for the menu item.
	 * 
	 * @param theWorld The world into which we add.
	 */
	public GravityMenuItem(final World theWorld) {
		super(theWorld, "Gravity");
	}
	
	/**
	 * Action handler.
	 * 
	 * @param arg0 Unused event info.
	 */
	public void actionPerformed(final ActionEvent arg0) {
		myWorld.addForce(new Gravity());
	}
}
