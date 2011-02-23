package tesseract.newmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.vecmath.Vector3f;

import tesseract.World;
import tesseract.objects.Toroid;

/**
 * NewToroidMenuItem
 * 
 * Defines a menu item to add an Toroid to the world.
 * Code recycled from TesseractMenuItem by Steve Bradshaw and Jessie Morgan
 * 
 * @author Phillip Cardon
 */
public class NewToroidMenuItem extends MenuItem {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1936364496102891064L;
	//private static Map <String, Object> myParams;
	
	/**
	 * Constructor.
	 * @param theWorld to add objects to.
	 */
	public NewToroidMenuItem(final World theWorld) {
		super(theWorld, "Toroid");
		buildParams();
		
		
	}
	
	/**
	 * Adds Parameters for user input.
	 * Sets default text box text.
	 */
	private void buildParams() {
		myParameters.put("Tube Radius", new Float(0f));
		myParameters.put("Tube Resolution", new Integer(0));
		myParameters.put("Toroid Radius", new Float(0f));
		myParameters.put("Toroid Resolution", new Integer(0));
		this.makePanel();
		myReadData.get("Position").setText(
				MenuItem.DEFAULT_POSITION.toString());
		myReadData.get("Mass").setText(((Float)
				MenuItem.DEFAULT_MASS).toString());
		myReadData.get("Tube Radius").setText(".06");
		myReadData.get("Tube Resolution").setText("25");
		myReadData.get("Toroid Radius").setText(".08");
		myReadData.get("Toroid Resolution").setText("30");
		createParameterMenu();
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
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
					myWorld.addObject(new Toroid(MenuItem.DEFAULT_POSITION,
							MenuItem.DEFAULT_MASS, 0f, .06f, 25, .08f, 30));
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
					} else if (o.getClass().equals(new Integer(0).getClass())) {
						myParameters.put(s, new Integer(Integer.parseInt(p)));
					}
						
				}
				if (event.getSource() == enterButton) {
					myWorld.addObject(new Toroid(getPosition(), getMass(), 0f,
						((Float) myParameters.get("Tube Radius")).floatValue(),
						((Integer) myParameters.get("Tube Resolution")
								).intValue(),
						((Float) myParameters.get("Toroid Radius")
								).floatValue(),
						((Integer) myParameters.get("Toroid Resolution")
								).intValue(), myColor));
					params.dispose();
				}
			}
		});
	}
}
