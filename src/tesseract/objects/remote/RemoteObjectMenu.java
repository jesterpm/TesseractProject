package tesseract.objects.remote;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

import tesseract.World;

/**
 * Remote object menu.
 * 
 * @author jesse
 */
public class RemoteObjectMenu extends JMenu {
	private ArrayList<RemoteObject> myControlledObjects;
	
	private World myWorld;
	
	private RemoteObjectCommunicator myCommunicator;
	
	private SocketAddress myHome;
	
	public RemoteObjectMenu(final World theWorld) {
		super("RC Objects");
		// Added by Steve: Fixes viewing menu problem with Canvas3D on both my windows machines
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		myWorld = theWorld;
		myControlledObjects = new ArrayList<RemoteObject>();
		myCommunicator = new RemoteObjectCommunicator();
		new Thread(myCommunicator).start();
		
		try {
			myHome = new InetSocketAddress(InetAddress.getLocalHost(), myCommunicator.getPort());
			
		} catch (UnknownHostException e) {
			System.err.println(e);
		}
		
		// Home submenu
		JMenu home = new JMenu("Home Address");
		populateHomeMenu(home);
		add(home);
		
		// Objects that can be added
		add(new TankMenuItem(this));
		add(new BlimpMenuItem(this));
		
		// Separator
		addSeparator();
		
		// Living Objects here...
	}
	
	private void populateHomeMenu(JMenu home) {
		List<InetAddress> addrList = new ArrayList<InetAddress>();
		
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			
			ButtonGroup group = new ButtonGroup();
			
			while (interfaces.hasMoreElements()) {
				NetworkInterface i = interfaces.nextElement();
				
				try {
					if (i.isUp()) {
						// Get addresses
						Enumeration<InetAddress> ips = i.getInetAddresses();
						while (ips.hasMoreElements()) {
							final InetAddress ip = ips.nextElement();
							
							JRadioButtonMenuItem item = new JRadioButtonMenuItem(ip.toString());
							item.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									myHome = new InetSocketAddress(ip, myCommunicator.getPort());
								}
							});
							
							try {
								if (ip.equals(InetAddress.getLocalHost())) {
									item.setSelected(true);
								}
							} catch (UnknownHostException e1) {
							}
							
							home.add(item);
							group.add(item);
						}
					}
				} catch (SocketException e) {
					// I suppose we'll ignore this address.
				}
			}
			
		} catch (SocketException e) {
			// I suppose we'll ignore all addresses?
		}
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
		theObject.setHome(myHome);
		item.setSelected(true);
		add(item);
	}
	
	public void sendKeyToObjects(final KeyEvent e) {
		for (RemoteObject o : myControlledObjects) {
			myCommunicator.sendKeyToObject(o.getId(), e);
		}
	}
}
