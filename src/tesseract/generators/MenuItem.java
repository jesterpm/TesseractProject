package tesseract.generators;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import tesseract.World;

/**
 * Parent class for generator menus.
 * 
 * @author jesse
 */
public abstract class MenuItem extends JMenuItem {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 5591914377175098868L;

	protected MenuItem(final String label, final World theWorld) {
		super(label);
		
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generate(theWorld);
			}
		});
	}
	
	/**
	 * Generate.
	 * 
	 * @param World the world to put it in.
	 */
	public abstract void generate(final World theWorld);
}
