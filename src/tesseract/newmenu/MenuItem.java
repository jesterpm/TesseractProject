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

/**
 * NewIcosahedronMenutItem
 * 
 * Defines a menu item to add an Icosahedron to the world.
 * Code recycled from TesseractMenuItem by Steve Bradshaw and Jessie Morgan
 * 
 * @author Phillip Cardon
 */
public abstract class MenuItem extends JMenuItem implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4191575308469669044L;
	/**
	 * default position.
	 */
	protected static final Vector3f DEFAULT_POSITION = new Vector3f(0, 0, 0);
	/**
	 * Jframe to hold panel.
	 */
	private JFrame myFrame;
	/**
	 * Jpanel for variable text boxes.
	 */
	private JPanel myPanel;
	/**
	 * JTextFields.
	 */
	private JTextField[] myFields;
	/**
	 * Variable name/type mapping.
	 */
	protected Map <String, Object> myParameters;
	/**
	 * Variable/Textfield mapping.
	 */
	protected Map <String, JTextField> myReadData;
	/**
	 * World to add objects to.
	 */
	protected World myWorld;
	/**
	 * A Parameter setting JFrame.
	 */
	private JFrame myParamFrame;
	
	/**
	 * The button that get all inputs for shape.
	 */
	private JButton myEnterButton;
	/**
	 * The default button.
	 */
	private JCheckBox myDefaultButton;
	
	/**
	 * Default constructor.
	 */
	public MenuItem() { }
	
	/**
	 * Constructor.
	 * @param theWorld world parameter.
	 */
	public MenuItem(final World theWorld, String theLabel) {
		super(theLabel);
		myFrame = new JFrame();
		myParameters = new HashMap<String, Object>();
		myPanel = new JPanel();
		
		myFields = new JTextField[myParameters.keySet().size()];
		myReadData = new HashMap <String, JTextField>();
		myWorld = theWorld;
		myParameters.put("Positon", new Vector3f());
		myParameters.put("Mass", new Float(0f));
		addActionListener(this);
	}
	
	/**
	 * Create Panel.
	 */
	protected void makePanel() {
		Set<String> varNames = myParameters.keySet();
		myFields = new JTextField[myParameters.keySet().size()];
		myPanel.setLayout(new GridLayout(myParameters.size(), 2));
		int i = 0;
		for (String s : varNames) {
			myFields[i] = new JTextField();
			myPanel.add(new JLabel(s));
			myPanel.add(myFields[i]);
			myReadData.put(s, myFields[i]);
			i++;
		}
		//myPanel = new JPanel(new GridLayout(myParameters.keySet().size(), 2));
	}
	
	/**
	 * 
	 */
	protected void createParameterMenu() {
		
		myParamFrame = new JFrame("Parameters");
		myParamFrame.setBackground(Color.GRAY);
		myParamFrame.setLayout(new BorderLayout());
		
		Toolkit tk = Toolkit.getDefaultToolkit();
	    Dimension screenSize = tk.getScreenSize();
	    int screenHeight = screenSize.height;
	    int screenWidth = screenSize.width;
	    myParamFrame.setSize(screenWidth / 4, screenHeight / 4);
	    myParamFrame.setLocation(screenWidth / 4, screenHeight / 4);
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
	    
	    myEnterButton = new JButton("ENTER");
	    
	    myDefaultButton = new JCheckBox("Default Shape   ");
	    
	    myParamFrame.add(myDefaultButton, BorderLayout.NORTH);
	    myParamFrame.add(myPanel, BorderLayout.CENTER);
	    myParamFrame.add(myEnterButton, BorderLayout.SOUTH);

	    myParamFrame.setAlwaysOnTop(true);
	    myParamFrame.pack();
	    myParamFrame.setVisible(isVisible());
	}
	
	/**
	 * 
	 * @return
	 */
	public JCheckBox getDefaultButton() {
		return myDefaultButton;
	}
	
	/**
	 * 
	 * @return
	 */
	public JButton getEnterButton() {
		return myEnterButton;
	}
	
	/**
	 * 
	 * @return
	 */
	public JFrame getParamFrame() {
		return myParamFrame;
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
