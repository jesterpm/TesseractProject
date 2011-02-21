package tesseract.newmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.vecmath.Vector3f;

import tesseract.World;
import tesseract.objects.Icosahedron;

/**
 * NewIcosahedronMenutItem
 * 
 * Defines a menu item to add an Icosahedron to the world.
 * Code recycled from TesseractMenuItem by Steve Bradshaw and Jessie Morgan
 * 
 * @author Phillip Cardon
 */
public class NewIcosahedronMenuItem extends MenuItem {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1936364496102891064L;
	//private static Map <String, Object> myParams;
	
	/**
	 * Constructor.
	 * @param theWorld to add objects to.
	 */
	public NewIcosahedronMenuItem(final World theWorld) {
		super(theWorld, "Icosahedron");
		buildParams();
		
		
	}
	
	/**
	 * Adds Parameters for user input.
	 * Sets default text box text.
	 */
	private void buildParams() {
		myParameters.put("Scale", new Float(0f));
		this.makePanel();
		myReadData.get("Scale").setText(((Float) 
				Icosahedron.DEFAULT_SCALE).toString());
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
					myWorld.addObject(new Icosahedron(MenuItem.DEFAULT_POSITION,
							DEFAULT_MASS, Icosahedron.DEFAULT_SCALE));
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
					myWorld.addObject(new Icosahedron(
							getPosition(), getMass(), getScale()));
					params.dispose();
				}
			}
		});

	}
	
	/**
	 * Gets the Scale for Icosahedron.
	 * @return the scale.
	 */
	private float getScale() {
		return ((Float) myParameters.get("Scale")).floatValue();
	}

}
