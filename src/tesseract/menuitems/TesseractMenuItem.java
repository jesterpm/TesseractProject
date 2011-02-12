package tesseract.menuitems;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.vecmath.Vector3f;

import tesseract.World;

/**
 * Abstract class for menu items.
 * 
 * @author Jesse Morgan, Steve Bradshaw
 */
public abstract class TesseractMenuItem 
	extends JMenuItem implements ActionListener {

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 1839955501629795920L;
	
	/**
	 * A Default radius.
	 */
	private static final float DEFAULT_RADIUS = 0.1f;
	
	/**
	 * A Default position.
	 */
	private static final Vector3f DEFAULT_POSITION = new Vector3f(0,0,0);
	
	/**
	 * The reference to the world.
	 */
	protected World myWorld;
	
	/**
	 * The default button
	 */
	private JCheckBox my_default_button;
	
	/**
	 * A Parameter setting Jframe
	 */
	private JFrame my_param_frame;
	
	/**
	 * Constructor.
	 * 
	 * @param theWorld The world in which to add.
	 * @param theLabel The label for the menu item.
	 */
	public TesseractMenuItem(final World theWorld, final String theLabel) {
		super(theLabel);
		
		myWorld = theWorld;
		
		addActionListener(this);
	}
	
	/**
	 * Utility method to parse a string formatted as x,y,z into a vector3f.
	 * 
	 * @param input A string to parse.
	 * @return A vector3f.
	 */
	protected Vector3f parseVector(final String input)  {
		String[] split = input.split(",");
		
		float x = Float.parseFloat(split[0]);
		float y = Float.parseFloat(split[1]);
		float z = Float.parseFloat(split[2]);

		return new Vector3f(x, y, z);
	}
	
	protected void createParameterMenu() {
		my_param_frame= new JFrame("Parameters");
		Toolkit tk = Toolkit.getDefaultToolkit();
	    Dimension screenSize = tk.getScreenSize();
	    int screenHeight = screenSize.height;
	    int screenWidth = screenSize.width;
	    my_param_frame.setSize(screenWidth / 2, screenHeight / 2);
	    my_param_frame.setLocation(screenWidth / 4, screenHeight / 4);
	    my_default_button = new JCheckBox("Default Shape");
	    my_param_frame.add(my_default_button);
	    my_param_frame.setAlwaysOnTop(true);
	    my_param_frame.pack();
	    my_param_frame.setVisible(isVisible());
	}
	
	protected JCheckBox getDefaultButton() {
		return my_default_button;
	}
	
	protected JFrame getParamFrame() {
		return my_param_frame;
	}

	/**
	 * @return the defaultRadius
	 */
	public static float getDefaultRadius() {
		return DEFAULT_RADIUS;
	}

	/**
	 * @return the defaultPosition
	 */
	public static Vector3f getDefaultPosition() {
		return DEFAULT_POSITION;
	}
}
