package tesseract.newmenu;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import tesseract.World;
import tesseract.objects.Particle;

/**
 * NewIcosahedronMenutItem
 * 
 * Defines a menu item to add an Particle to the world.
 * Code recycled from TesseractMenuItem by Steve Bradshaw and Jessie Morgan
 * 
 * @author Phillip Cardon
 */
public class NewParticleMenuItem extends MenuItem {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1936364496102891064L;
	
	/**
	 * Default frequency.
	 */
	private static final float DEFAULT_FREQUENCY = 0.5f;
	
	/**
	 * Constructor.
	 * @param theWorld to add objects to.
	 */
	public NewParticleMenuItem(final World theWorld) {
		super(theWorld, "Particle");
		this.makePanel();
		createParameterMenu();
		makeListeners();
	}
	
	/**
	 * Adds Parameters for user input.
	 * Sets default text box text.
	 */
	/*private void buildParams() {
		myParameters.put("Frequency", new Float(0f));
		this.makePanel();
		myReadData.get("Frequency").setText(((Float) 
				Icosahedron.DEFAULT_SCALE).toString());
	}*/

	@Override
	public void actionPerformed(final ActionEvent e) {
		setParent();
		this.getParamFrame().pack();
		this.getParamFrame().setVisible(true);
	}
	
	/**
	 * makeListeners and attach to buttons.
	 */
	private void makeListeners() {
		final JButton defaultButton = getDefaultButton();
		final JFrame params = getParamFrame();
		final JButton enterButton = getEnterButton();
		
		
		
		defaultButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if (e.getSource() == defaultButton) {
					myWorld.addObject(new Particle(getPosition(),
							getMass(), new Color3f(1f, 0f, 0f)));
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
					params.dispose();
					//Color c = JColorChooser.showDialog(null, "Particle Color",
					//		Color.RED);
					myWorld.addObject(new Particle(getPosition(), getMass(),
						new Color3f(myColor)));
					
				}
			}
		});
	}
}
