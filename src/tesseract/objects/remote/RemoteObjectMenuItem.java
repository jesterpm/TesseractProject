package tesseract.objects.remote;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public abstract class RemoteObjectMenuItem extends JMenuItem {
	protected RemoteObjectMenuItem(final String theLabel, 
			final RemoteObjectMenu theMenu) {
		super(theLabel);
		
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RemoteObject o = createRemoteObject();
				theMenu.addObject(o);
			}
		});
	}
	
	protected abstract RemoteObject createRemoteObject();
}
