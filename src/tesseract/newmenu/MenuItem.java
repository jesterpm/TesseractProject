package tesseract.newmenu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.vecmath.Vector3f;

import tesseract.World;

abstract public class MenuItem extends JMenuItem implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4191575308469669044L;
	protected static final Vector3f DEFAULT_POSITION = new Vector3f(0, 0, 0);
	private JFrame myFrame;
	private JPanel myPanel;
	private JTextField[] myFields;
	private Map <String, Object> myParameters;
	protected Map <String, JTextField> myReadData;
	protected World myWorld;
	/**
	 * A Parameter setting JFrame
	 */
	private JFrame my_param_frame;
	
	/**
	 * The button that get all inputs for shape
	 */
	private JButton my_enter_button;
	/**
	 * The default button
	 */
	private JCheckBox my_default_button;
	
	
	public MenuItem() {};
	public MenuItem (Map <String, Object> theParams, World theWorld) {
		myFrame = new JFrame();
		myPanel = new JPanel(new GridLayout(myParameters.keySet().size(), 2));
		myParameters = theParams;
		myFields = new JTextField[myParameters.keySet().size()];
		myReadData = new HashMap <String, JTextField>();
		myWorld = theWorld;
		myParameters.put("Positon", new Vector3f());
		myParameters.put("Mass", new Float(0f));
		addActionListener(this);
	}
	
	public void makePanel() {
		Set<String> varNames = myParameters.keySet();
		int i = 0;
		for (String s : varNames) {
			myPanel.add(new JLabel(s));
			myPanel.add(myFields[i]);
			myReadData.put(s, myFields[i]);
			i++;
		}
	}
	
protected void createParameterMenu() {
		
		my_param_frame = new JFrame("Parameters");
		my_param_frame.setBackground(Color.GRAY);
		my_param_frame.setLayout(new BorderLayout());
		
		Toolkit tk = Toolkit.getDefaultToolkit();
	    Dimension screenSize = tk.getScreenSize();
	    int screenHeight = screenSize.height;
	    int screenWidth = screenSize.width;
	    my_param_frame.setSize(screenWidth / 4, screenHeight / 4);
	    my_param_frame.setLocation(screenWidth / 4, screenHeight / 4);
	    /*
	    my_position_input = new JTextField(10);
	    my_position_input.setText("0,0,0");
	    my_radius_input = new JTextField(10);
	    my_radius_input.setText(".1");
	    my_mass_input = new JTextField(10);
	    my_mass_input.setText("1");
	    */
	    //JLabel blank = new JLabel("");
	    //JLabel position_label = new JLabel("Enter Position:  ");
	    //JLabel radius_label = new JLabel("Enter Radius:  ");
	    //JLabel mass_label = new JLabel("Enter Mass:  ");
	    
	    my_enter_button = new JButton("ENTER");
	    
	    my_default_button = new JCheckBox("Default Shape   ");
	    
	    my_param_frame.add(my_default_button, BorderLayout.NORTH);
	    my_param_frame.add(myPanel, BorderLayout.CENTER);
	    my_param_frame.add(my_enter_button, BorderLayout.SOUTH);

	    my_param_frame.setAlwaysOnTop(true);
	    my_param_frame.pack();
	    my_param_frame.setVisible(isVisible());
	}

	public JCheckBox getDefaultButton() {
		return my_default_button;
	}
	
	public JButton getEnterButton() {
		return my_enter_button;
	}
	
	public JFrame getParamFrame() {
		return my_param_frame;
	}

	/**
	 * Utility method to parse a string formatted as x,y,z into a vector3f.
	 * 
	 * @param input A string to parse.
	 * @return A vector3f.
	 * @author Jesse Morgan, Steve Bradshaw
	 */
	protected Vector3f parseVector(final String input)  {
		String[] split = input.split(",");
		
		float x = Float.parseFloat(split[0]);
		float y = Float.parseFloat(split[1]);
		float z = Float.parseFloat(split[2]);

		return new Vector3f(x, y, z);
	}
	
	protected Vector3f getPosition() {
		return (Vector3f) myParameters.get("Position");
	}
	
	protected float getMass() {
		return ((Float) myParameters.get("Mass")).floatValue();
	}

}
