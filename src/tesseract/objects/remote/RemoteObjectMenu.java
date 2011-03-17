package tesseract.objects.remote;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import tesseract.World;

/**
 * Remote object menu.
 * 
 * @author jesse
 */
public class RemoteObjectMenu extends JMenu {
	private ArrayList<RemoteObject> myControlledObjects;
	
	private World myWorld;
	
	public RemoteObjectMenu(final World theWorld) {
		super("RC Objects");
		// Added by Steve: Fixes viewing menu problem with Canvas3D on both my windows machines
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		myWorld = theWorld;
		myControlledObjects = new ArrayList<RemoteObject>();
		
		// Objects that can be added
		add(new TankMenuItem(this));
		add(new BlimpMenuItem(this));
		
		// Separator
		addSeparator();
		
		// Living Objects here...
	}
	
	public void addObject(final RemoteObject theObject) {
		JCheckBoxMenuItem item = new JCheckBoxMenuItem();
		
		item.setText(theObject.getName());
		
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (((JCheckBoxMenuItem) e.getSource()).isSelected()) {
					myControlledObjects.add(theObject);
					
				} else {
					myControlledObjects.remove(theObject);
				}
			}
		});
		
		myWorld.addObject(theObject);
		myControlledObjects.add(theObject);
		item.setSelected(true);
		add(item);
	}
	
	public void sendKeyToObjects(final KeyEvent e) {
		for (RemoteObject o : myControlledObjects) {
			o.sendKeyEvent(e);
		}
	}
}
