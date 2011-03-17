package tesseract.objects.remote;

import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.UUID;

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
	protected static final float STEP = 0.01f;
	
	/**
	 * Unique object id.
	 */
	private UUID myId;
	
	/**
	 * The home address.
	 */
	private SocketAddress myHome;
	
	/**
	 * The local computer address.
	 */
	transient private boolean isLocal;
	
	/**
	 * The socket.
	 */
	transient private RemoteObjectReciever myListener;
	
	/**
	 *
	 * @param thePosition
	 * @param mass
	 */	
	public RemoteObject(Vector3f thePosition, float mass) {
		super(thePosition, mass);
		
		myId = UUID.randomUUID();
		isLocal = true;
	}
	
	public void setHome(SocketAddress home) {
		myHome = home;
		
		myListener = new RemoteObjectReciever();
		new Thread(myListener).start();
	}
	
	/**
	 * This method is called when a key event is received.
	 * 
	 * @param event The KeyEvent recieved
	 */
	protected abstract void keyEventReceived(final KeyInfo event);
	
	/**
	 * Get the name of the object for the menu.
	 * 
	 * @return The object's name for the menu.
	 */
	public abstract String getName();
	
	public UUID getId() {
		return myId;
	}

	/**
	 * Send a KeyEvent to this remote object.
	 * 
	 * @param keyEvent The key event
	 */
	public void sendKeyEvent(final KeyInfo keyEvent) {
		keyEventReceived(keyEvent);
		updateTranformGroup();
	} 
	
	
	private void readObject(ObjectInputStream in) 
     throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		
		// Start Socket Thread
		myListener = new RemoteObjectReciever();
		new Thread(myListener).start();
		
	}
	
	public void detach() {
		super.detach();
		
		myListener.stop();
	}
	
	private class RemoteObjectReciever implements Runnable {
		private Socket mySocket;
		
		public void stop() {
			try {
				mySocket.close();
				
			} catch (IOException e) {
			}
		}
		
		public void run() {
			mySocket = new Socket();
			
			try {
				mySocket.connect(myHome);
				
				// Send id
				DataOutputStream out = new DataOutputStream(mySocket.getOutputStream());
				out.writeLong(myId.getMostSignificantBits());
				out.writeLong(myId.getLeastSignificantBits());
				out.flush();
				
				// Wait for data
				DataInputStream in = new DataInputStream(mySocket.getInputStream());
				
				while (true) {
					try {
						int key = in.readInt();
						
						KeyInfo event = new KeyInfo(key);
						
						sendKeyEvent(event);
						
					} catch (SocketException e) {
						// Socket closed from transportation.
						
					} catch (Exception e) {
						System.err.println("Could not read KeyEvent: " + e);
						break;
					}
				}
				
			} catch (IOException e) {
				System.err.println(e);
			}
		}
		
	}
}
