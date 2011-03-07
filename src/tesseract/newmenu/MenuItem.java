package tesseract.newmenu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
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
	 * 
	 */
	protected static final float DEFAULT_MASS = 2f;
	/**
	 * Jframe to hold panel.
	 */
	//private JFrame myFrame;
	/**
	 * Jpanel for variable text boxes.
	 */
	private JPanel myPanel;

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
	 * Color to make object.
	 */
	protected Color myColor;
	
	/**
	 * A Parameter setting JFrame.
	 */
	private JFrame myParamFrame;
	
	/**
	 * The button that get all inputs for shape.
	 */
	private JButton myEnterButton;
	
	/**
	 * The button that get all inputs for shape.
	 */
	private JButton myColorButton;
	
	/**
	 * The default button.
	 */
	private JButton myDefaultButton;
	
	private boolean useColorButton;
	
	/**
	 * Parent frame
	 */
	private JFrame myParent;
	
	/**
	 * Constructor.
	 * @param theWorld world parameter.
	 * @param theLabel for menu item.
	 */
	public MenuItem(final World theWorld, final String theLabel) {
		super(theLabel);
		useColorButton = true;
		//myFrame = new JFrame();
		myParameters = new HashMap<String, Object>();
		myPanel = new JPanel();
		myReadData = new HashMap <String, JTextField>();
		myWorld = theWorld;
		myParameters.put("Position", DEFAULT_POSITION);
		myParameters.put("Mass", new Float(DEFAULT_MASS));
		addActionListener(this);
		myParamFrame = new JFrame("Parameters");
		myParamFrame.setBackground(Color.GRAY);
		myParamFrame.setLayout(new BorderLayout());
		myColorButton = new JButton("Color");
		myColor = Color.RED;
		myParent = new JFrame();
	}
	
	/**
	 * Constructor.
	 * @param theWorld world parameter.
	 * @param theLabel for menu item.
	 * @param theColor use color button.
	 */
	public MenuItem(final World theWorld, final String theLabel,
			boolean theColorButton) {
		this(theWorld, theLabel);
		useColorButton = theColorButton;
	}
	
	/**
	 * Create Panel.
	 */
	protected void makePanel() {
		Set<String> varNames = myParameters.keySet();
		myPanel.setLayout(new GridLayout(myParameters.size(), 2));
		for (String s : varNames) {
			myPanel.add(new JLabel(s));
			myReadData.put(s, new JTextField());
			myPanel.add(myReadData.get(s));
		}
		myReadData.get("Position").setText(
				MenuItem.DEFAULT_POSITION.toString().substring(1,
						MenuItem.DEFAULT_POSITION.toString().length() - 1));
		myReadData.get("Mass").setText(((Float)
				MenuItem.DEFAULT_MASS).toString());
	}
	
	/**
	 * 
	 */
	protected void createParameterMenu() {
		
		
		myColorButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent arg0) {
				myColor = JColorChooser.showDialog(null, "Particle Color",
						Color.RED);
				
			} });
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
	    
	    myDefaultButton = new JButton("Default Shape");
	    
	    if (useColorButton) {
	    	JPanel temp = new JPanel();
	    	temp.setLayout(new GridLayout(1, 2));
		    temp.add(myColorButton);
		    temp.add(myEnterButton);
		    myParamFrame.add(temp, BorderLayout.SOUTH);
	    } else {
	    	myParamFrame.add(myEnterButton, BorderLayout.SOUTH);
	    }
	    
	    myParamFrame.add(myDefaultButton, BorderLayout.NORTH);
	    myParamFrame.add(myPanel, BorderLayout.CENTER);
	    

	    myParamFrame.setAlwaysOnTop(true);
	    //myParamFrame.pack();
	    //myParamFrame.setVisible(isVisible());
	}
	
	/**
	 * 
	 * @return Default Button
	 */
	public JButton getDefaultButton() {
		return myDefaultButton;
	}
	
	/**
	 * 
	 * @return enter button
	 */
	public JButton getEnterButton() {
		return myEnterButton;
	}
	
	/**
	 * 
	 * @return parameter frame
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
	
	/**
	 * 
	 * @return position from vector.
	 */
	protected Vector3f getPosition() {
		return (Vector3f) myParameters.get("Position");
	}
	
	/**
	 * 
	 * @return mass.
	 */
	protected float getMass() {
		return ((Float) myParameters.get("Mass")).floatValue();
	}
	
	/**
	 * Set Parent.
	 * @param theParent frame.
	 */
	public void setParent(final JFrame theParent) {
		myParamFrame.setLocationRelativeTo(theParent);
		myParent = theParent;
	}
	
	/**
	 * Set Parent.
	 */
	public void setParent() {
		myParamFrame.setLocationRelativeTo(myParent);
	}
}
