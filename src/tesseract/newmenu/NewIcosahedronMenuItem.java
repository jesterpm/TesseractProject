package tesseract.newmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
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
	private static final float DEFAULT_MASS = 2f;
	private static final long serialVersionUID = 1936364496102891064L;
	private static Map <String, Object> myParams = new HashMap<String, Object>();
	
	
	public NewIcosahedronMenuItem (World theWorld) {
		super (myParams, theWorld);
		buildParams();
	}
	
	private void buildParams() {
		myParams.put("Scale", new Float(0f));
		this.makePanel();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final JCheckBox defaultButton = getDefaultButton();
		final JFrame params = getParamFrame();
		final JButton enterButton = getEnterButton();

		defaultButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if (defaultButton.isSelected()) {
					myWorld.addObject(new Icosahedron(MenuItem.DEFAULT_POSITION,
							DEFAULT_MASS, Icosahedron.DEFAULT_SCALE));
					params.dispose();
				}
			}
		});
		enterButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				Set<String> itr = myParams.keySet();	
				//List<Object> cParams = new LinkedList<Object>();
				for (String s : itr) {
					Object o = myParams.get(s);
					String p = myReadData.get(s).getText();
					if (o.getClass().equals(new Float(0f).getClass())) {
						myParams.put(s, new Float(Float.parseFloat(p)));
					} else if (o.getClass().equals(new Vector3f().getClass())) {
						myParams.put(s, parseVector(p));
					}
						
				}
				/*
					String string = getPositionField().getText();
					Vector3f pos = parseVector(string);
					setPosition(pos);
				
					String string2 = getRadiusField().getText();
					float radius = Float.parseFloat(string2);
					setRadius(radius);

					String string3 = getMassField().getText();
					float mass = Float.parseFloat(string3);
					setMass(mass);
	*/
				if (event.getSource() == enterButton) {
					myWorld.addObject(new Icosahedron(getPosition(), getMass(), getScale()));
					params.dispose();
				}
			}
		});

	}
	
	private float getScale() {
		return ((Float) myParams.get("Scale")).floatValue();
	}

}
