package tesseract.objects.remote;

import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;

public class RemoteObjectCommunicator implements Runnable {

	private static final int BASE_PORT = 5551;
	
	private ServerSocket mySocket;
	
	private HashMap<UUID, Socket> mySockets;
	
	private boolean myRunning;
	
	public RemoteObjectCommunicator() {
		mySockets = new HashMap<UUID, Socket>();
		myRunning = false;
	}

	
	public void run() {
		int port = BASE_PORT;
		
		// Find an open port.
		while (true) {
			try {
				mySocket = new ServerSocket(port);
				myRunning = true;
				break;
			
			} catch (IOException e) {
				port++;
			
			} catch (Exception e) {
				System.err.println(e);
				return;
			}
		}
		
		// Listen for connections
		while (myRunning) {
			try {
				Socket s = mySocket.accept();
				
				handleNewSocket(s);
				
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}
	
	public boolean sendKeyToObject(UUID id, KeyEvent event) {
		Socket s = mySockets.get(id);
		
		if (s != null && s.isConnected()) {
			try {
				DataOutputStream out = new DataOutputStream(s.getOutputStream());
				out.writeInt(event.getKeyCode());
				out.flush();
				
				return true;
				
			} catch (IOException e) {
				mySockets.remove(id);
				// TODO: REMOVE
				System.out.println(id + " Exception. "  + e);
				return false;
			}
			
		} else {
			// TODO: REMOVE
			System.out.println(id + " has not called home.");
			return false;
		}
	}
	
	private void handleNewSocket(final Socket socket) {		
		try {
			// TODO: Remove
			System.out.println("New Socket");
			DataInputStream in = new DataInputStream(socket.getInputStream());
			
			long msb = in.readLong();
			long lsb = in.readLong();
			
			UUID id = new UUID(msb, lsb);
			
			System.out.println("Id is " + id);
			
			mySockets.put(id, socket);
			
		} catch (Exception e) {
			System.err.println(e);
		}
	}


	public int getPort() {
		System.out.print("Blocking for address.");
		while (mySocket == null) {
			// Block until we get a socket.
			System.out.print(".");
		}
		
		System.out.println();
		
		return mySocket.getLocalPort();
	}
	
}
