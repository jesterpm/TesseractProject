package tesseract.menuitems;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.vecmath.Vector3f;

import tesseract.World;
import tesseract.objects.Toroid;

/**
 * Icosahedron Menu Item.
 * 
 * @author Steve Bradshaw
 * @deprecated By Phillip Cardon
 */
public class DonutMenuItem extends TesseractMenuItem {

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 1L;
	
	private float scale = 1f;
	private float sliceRadius = .06f;  //inside whole
	private int sliceDivisions = 25;
	private float arcRadius = .08f; //outside whole
	private int arcDivisions = 30;

	/**
	 * Constructor for the menu item.
	 * 
	 * @param theWorld The world into which we add.
	 */
	public DonutMenuItem(final World theWorld) {
		super(theWorld, "Donut");
	}
	
	/**
	 * Action handler.
	 * 
	 * @param arg0 Unused event info.
	 */
	public void actionPerformed(final ActionEvent arg0) {
		
		createParameterMenu();
		
		//If the default button is checked, the frame will close.
		final JCheckBox defaultButton = getDefaultButton();
		final JFrame params = getParamFrame();
	/*	final JButton enterButton = getEnterButton();
		
		final JTextField scale_input = new JTextField(10);
		scale_input.setText("1");
	    final JTextField sliceRadiusInput = new JTextField(10);
	    sliceRadiusInput.setText(".06");
	    final JTextField sliceDivisionsInput = new JTextField(10);
	    sliceDivisionsInput.setText("50");
	    final JTextField arcRadiusInput = new JTextField(10);
	    arcRadiusInput.setText(".08");
	    final JTextField arcDivisionsInput = new JTextField(10);
	    arcDivisionsInput.setText("30");
	    
	    JLabel scale_label = new JLabel("scale:  ");
	    JLabel slice_radius_label = new JLabel("sliceRadius:  ");
	    JLabel slice_divs_label = new JLabel("sliceDivs:  ");
	    JLabel arc_radius_label = new JLabel("arcRadius:  ");
	    JLabel arc_divs_label = new JLabel("arcDivs:  ");
	    
	    params.add(scale_label);
	    params.add(scale_input);
	    params.add(slice_radius_label);
	    params.add(sliceRadiusInput);
	    params.add(slice_divs_label);
	    params.add(sliceDivisionsInput);
	    params.add(arc_radius_label);
	    params.add(arcRadiusInput);
	    params.add(arc_divs_label);
	    params.add(arcDivisionsInput);
	    
	    
	    params.add(enterButton);*/
	    
		defaultButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if (defaultButton.isSelected()) {
					myWorld.addObject(new Toroid(getDefaultPosition(), 1, scale,
							sliceRadius, sliceDivisions, arcRadius, arcDivisions));
					params.dispose();
				}
			}
		});
		
		/*enterButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
					
					String string = getPositionField().getText();
					Vector3f pos = parseVector(string);
					setPosition(pos);
				
					String string3 = getMassField().getText();
					float mass = Float.parseFloat(string3);
					setMass(mass);
					
					String string4 = getMassField().getText();
					scale = Float.parseFloat(string4);
					
					String string5 = getMassField().getText();
					sliceRadius = Float.parseFloat(string5);
					
					String string6 = getMassField().getText();
					sliceDivisions = Integer.parseInt(string6);
					
					String string7 = getMassField().getText();
					arcRadius = Float.parseFloat(string7);
					
					String string8 = getMassField().getText();
					arcDivisions = Integer.parseInt(string8);
	
				if (event.getSource() == enterButton) {
					myWorld.addObject(new Toroid(pos, 1, scale,
							sliceRadius, sliceDivisions, arcRadius, arcDivisions));
					params.dispose();
				}
			}
		});*/
	}
}



