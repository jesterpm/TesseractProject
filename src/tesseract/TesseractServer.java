package tesseract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import alden.Peer;

/**
 * This class is not part of the deliverable. This class is simply a server to
 * help peers get into the network.
 * 
 * @author jesse
 */
public class TesseractServer {
	public static void main(String[] args) {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		Peer server = new Peer();
		server.createNetwork();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
		}
		while (true) {
			System.out.print("Enter text to send: ");
			try {
				String input = in.readLine();
				server.sendExtraToAllPeers(input);
			} catch (IOException e) {
			}
		}
	}
}
