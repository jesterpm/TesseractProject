package tesseract.generators;

import javax.swing.JMenu;

import tesseract.World;

/**
 * A generator menu.
 * 
 * @author jesse
 */
public class GeneratorsMenu extends JMenu {
	
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 8994598391451363374L;

	/**
	 * Constructor.
	 */
	public GeneratorsMenu(final World theWorld) {
		super("Generators");
		
		// Build the menu of generators.
		add(new SphereField(theWorld));
		add(new ParticleField(theWorld));
		add(new BoxField(theWorld));
	}
}
