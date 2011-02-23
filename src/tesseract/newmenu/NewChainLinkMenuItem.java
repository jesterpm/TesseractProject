package tesseract.newmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.vecmath.Vector3f;

import tesseract.World;
import tesseract.objects.ChainLink2;

/**
 * NewChainLinkMenuItem
 * 
 * Defines a menu item to add an ChainLink to the world.
 * Code recycled from TesseractMenuItem by Steve Bradshaw and Jessie Morgan
 * 
 * @author Phillip Cardon
 */
public class NewChainLinkMenuItem extends MenuItem {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1936364496102891064L;
	//private static Map <String, Object> myParams;
	
	/**
	 * Constructor.
	 * @param theWorld to add objects to.
	 */
	public NewChainLinkMenuItem(final World theWorld) {
		super(theWorld, "Chain Link");
		buildParams();
	}
	
	/**
	 * Adds Parameters for user input.
	 * Sets default text box text.
	 */
	private void buildParams() {
		myParameters.put("Diameter", new Float(0f));
		myParameters.put("Length", new Float(0f));
		myParameters.put("Width", new Float(0f));
		this.makePanel();
		myReadData.get("Diameter").setText(((Float) 
				ChainLink2.DEFAULT_DIAMETER_RATIO).toString());
		myReadData.get("Length").setText(((Float) 
				ChainLink2.DEFAULT_LENGTH).toString());
		myReadData.get("Width").setText(((Float) 
				ChainLink2.DEFAULT_WIDTH_RATIO).toString());
		createParameterMenu();
		makeListeners();
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		//createParameterMenu();
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
					myWorld.addObject(new ChainLink2(MenuItem.DEFAULT_POSITION,
							MenuItem.DEFAULT_MASS));
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
					myWorld.addObject(
					new ChainLink2((Vector3f) myParameters.get("Position"),
							((Float) myParameters.get("Mass")).floatValue(),
							((Float) myParameters.get("Diameter")).floatValue(),
							((Float) myParameters.get("Length")).floatValue(),
							((Float) myParameters.get("Width")).floatValue(),
							myColor));
					params.dispose();
				}
			}
		});

	}
}
