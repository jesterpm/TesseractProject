package tesseract.objects.remote;

import java.awt.event.KeyEvent;

import javax.vecmath.Vector3f;

import tesseract.objects.PhysicalObject;

/**
 * Parent class of network controlled objects.
 * 
 * @author jesse
 */
public abstract class RemoteObject extends PhysicalObject {
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -6966379446377480998L;
	
	/**
	 * Privately used by the key controller.
	 */
	private static final float STEP = 0.01f;
	
	public RemoteObject(Vector3f thePosition, float mass) {
		super(thePosition, mass);
	}

	/**
	 * This method is called when a key event is received.
	 * 
	 * @param event The KeyEvent recieved
	 */
	protected void keyEventReceived(final KeyEvent event) {
		switch (event.getKeyCode()) {
			case KeyEvent.VK_W:
				position.z -= STEP;
				break;
				
			case KeyEvent.VK_S:
				position.z += STEP;
				break;
				
			case KeyEvent.VK_A:
				position.x -= STEP;
				break;
				
			case KeyEvent.VK_D:
				position.x += STEP;
				break;
		}
	}
	
	/**
	 * Get the name of the object for the menu.
	 * 
	 * @return The object's name for the menu.
	 */
	public abstract String getName();

	/**
	 * Send a KeyEvent to this remote object.
	 * 
	 * @param keyEvent The key event
	 */
	public void sendKeyEvent(final KeyEvent keyEvent) {
		// TODO: Send this event over the network if necessary.
		keyEventReceived(keyEvent);
		
		updateTranformGroup();
	}

}
