package tesseract.newmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.vecmath.Vector3f;

import tesseract.World;
import tesseract.objects.PlanarPolygon;

/**
 * NewIcosahedronMenutItem
 * 
 * Defines a menu item to add a Planar Polygon to the world.
 * Code recycled from TesseractMenuItem by Steve Bradshaw and Jessie Morgan
 * 
 * @author Phillip Cardon
 */
public class NewPlanarPolygonMenuItem extends MenuItem {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1936364496102891064L;
	//private static Map <String, Object> myParams;
	
	/**
	 * Constructor.
	 * @param theWorld to add objects to.
	 */
	public NewPlanarPolygonMenuItem(final World theWorld) {
		super(theWorld, "Planar Polygon", false);
		buildParams();
		
		
	}
	
	/**
	 * Adds Parameters for user input.
	 * Sets default text box text.
	 */
	private void buildParams() {
		myParameters.put("Radius", new Float(PlanarPolygon.DEFAULT_RADIUS));
		myParameters.put("Divisions", new Integer(0));
		this.makePanel();
		myReadData.get("Radius").setText(
				((Float) PlanarPolygon.DEFAULT_RADIUS).toString());
		myReadData.get("Divisions").setText(
				((Integer) PlanarPolygon.DEFAULT_DIVISIONS).toString());
		createParameterMenu();
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		this.getParamFrame().pack();
		this.getParamFrame().setVisible(true);
		final JButton defaultButton = getDefaultButton();
		final JFrame params = getParamFrame();
		final JButton enterButton = getEnterButton();
		
		defaultButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if (e.getSource() == defaultButton) {
					myWorld.addObject(new PlanarPolygon(getPosition(),
							PlanarPolygon.DEFAULT_RADIUS));
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
					myWorld.addObject(new PlanarPolygon(getPosition(),
						getMass(),
						((Float) myParameters.get("Radius")).floatValue(),
						((Integer) myParameters.get("Divisions")).intValue()));
					params.dispose();
				}
			}
		});

	}
	

}
