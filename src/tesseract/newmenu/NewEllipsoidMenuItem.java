package tesseract.newmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.vecmath.Vector3f;

import tesseract.World;
import tesseract.objects.Ellipsoid;

/**
 * NewIcosahedronMenutItem
 * 
 * Defines a menu item to add an Ellipsoid to the world.
 * Code recycled from TesseractMenuItem by Steve Bradshaw and Jessie Morgan
 * 
 * @author Phillip Cardon
 */
public class NewEllipsoidMenuItem extends MenuItem {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1936364496102891064L;
	
	/**
	 * A Default radius.
	 */
	private static final float DEFAULT_RADIUS = 0.1f;
	
	/**
	 * Constructor.
	 * @param theWorld to add objects to.
	 */
	public NewEllipsoidMenuItem(final World theWorld) {
		super(theWorld, "Ellipsoid(NEW)");
		buildParams();
	}
	
	/**
	 * Adds Parameters for user input.
	 * Sets default text box text.
	 */
	private void buildParams() {
		myParameters.put("Radius", new Float(0f));
		this.makePanel();
		myReadData.get("Radius").setText(((Float) 
				DEFAULT_RADIUS).toString());
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		createParameterMenu();
		final JButton defaultButton = getDefaultButton();
		final JFrame params = getParamFrame();
		final JButton enterButton = getEnterButton();
		
		defaultButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if (e.getSource() == defaultButton) {
					myWorld.addObject(new Ellipsoid(getPosition(),
							DEFAULT_RADIUS));
					params.dispose();
				}
			}
		});
		enterButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				Set<String> itr = myParameters.keySet();	
				for (String s : itr) {
					Object o = myParameters.get(s);
					String p = myReadData.get(s).getText();
					if (o.getClass().equals(new Float(0f).getClass())) {
						myParameters.put(s, new Float(Float.parseFloat(p)));
					} else if (o.getClass().equals(new Vector3f().getClass())) {
						myParameters.put(s, parseVector(p));
					}
						
				}
				if (event.getSource() == enterButton) {
					myWorld.addObject(new Ellipsoid(getPosition(), getMass(),
							((Float) myParameters.get("Radius")).floatValue()));
					params.dispose();
				}
			}
		});
	}
}
