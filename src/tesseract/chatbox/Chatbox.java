package tesseract.chatbox;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import common.Peer;
import common.PeerMessage;

/**
 * Chat box.
 * @author Phillip Cardon
 * @version 1.0 working!
 */
public class Chatbox extends JFrame implements Observer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Panel for construction.
	 */
	private final JPanel textPanel;
	
	/**
	 * Scroller for text area.
	 */
	private final JScrollPane scroller;
	
	/**
	 * Text area for chat.
	 */
	private final JTextArea chatRoomDisplay;
	
	/**
	 * Submit button for message.
	 */
	private final JButton submitButton;
	
	/**
	 * Text field to send chat messages.
	 */
	private final JTextField chatField;
	
	/**
	 * my Peer name.
	 */
	private String myName;
	
	/**
	 * String to send to peers.
	 */
	private String toSend;
	
	/**
	 * All chats sent/received.
	 */
	private StringBuilder chats;
	
	/**
	 * Peer object.
	 */
	private Peer myPeer;
	
	/**
	 * Constructor.
	 * @param thePeer object.
	 */
	public Chatbox(final Peer thePeer) {
		myPeer = thePeer;
		textPanel = new JPanel(new BorderLayout());
		chatRoomDisplay = new JTextArea(10, 80);
		chatRoomDisplay.setEditable(false);
		scroller = new JScrollPane(chatRoomDisplay);
		submitButton = new JButton("Submit");
		chatField = new JTextField(70);
		this.setTitle("Tesseract Chatbox");
		chats = new StringBuilder();
		buildFrame();
		attachListeners();
		myName = "OFFLINE";
		this.setName("Chatbox");
	}
	
	/**
	 * Attaches Listeners to button and text field.
	 */
	private void attachListeners() {
		submitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent arg0) {
				//TODO: SEND MESSAGE IN chatField.
				if (arg0.getSource().equals(submitButton)) {
					toSend = chatField.getText();
					chatField.setText("");
					chats.append("\n");
					chats.append(myName + ": " + toSend);
					chatRoomDisplay.setText(chats.toString());
					chatField.setText("");
					myPeer.sendExtraToAllPeers(toSend);
				}
			}
		});
		chatField.addKeyListener(new ChatFieldListener());
	}
	
	/**
	 * Sets name of this client (local use only).
	 */
	public void setMyName() {
		myName = myPeer.getMyName();
		StringBuilder split = new StringBuilder();
		split.append(myName);
		int start;
		int end;
		start = split.indexOf("(");
		end = split.indexOf(")") + 1;
		myName = split.substring(start, end);
		this.setTitle("Tesseract Chatbox @ " + myPeer.getMyName());
	}
	
	/**
	 * Builds Chat frame.
	 */
	private void buildFrame() {
		textPanel.add(scroller, BorderLayout.CENTER);
		JPanel southPanel = new JPanel(new FlowLayout());
		//FlowLayout layout = 
		
		/*layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addComponent(chatField)
				.addComponent(submitButton)
		);*/
		southPanel.add(chatField);
		southPanel.add(submitButton);
		textPanel.add(southPanel, BorderLayout.SOUTH);
		this.add(textPanel);
		this.pack();
		this.setVisible(false);
	}

	@Override
	public void update(final Observable arg0, final Object arg1) {
		if (arg1.getClass().equals(PeerMessage.class)) {
			PeerMessage msg = (PeerMessage) arg1;
			if (msg.extra.getClass().equals(String.class)) {
				String incMsg = (String) msg.extra;
				String source = msg.sender.toString();
				StringBuilder split = new StringBuilder();
				split.append(source);
				int start;
				int end;
				start = split.indexOf("(");
				end = split.indexOf(")") + 1;
				source = split.substring(start, end);
				chats.append("\n");
				chats.append(source + ": " + incMsg);
				chatRoomDisplay.setText(chats.toString());
				chatRoomDisplay.setCaretPosition(chats.toString().length() - 1);
			} else {
				System.err.println("Message Extra Field is"
						+ " of unsupported type.");
			}
		}
		//Object extra = 
		//String inc = " ";
		//String source = " ";
		// TODO Listen for PeerMessage with string in it.
		/*
		if (arg1.getClass() == inc.getClass()) {
				inc = arg1;
				source = ((PeerMessage) arg0).getPeer.getName();
		      chats.append("\n");
		      chats.append(source + ": " + inc);
				
		 }//*/
		
		
	}
	
	/**
	 * KeyAdapter inner class for textfield.
	 * @author Phillip Cardon
	 * @verson 1.0
	 */
	private class ChatFieldListener extends KeyAdapter {
		
		/**
		 * keyReleased, activates on enter key released.
		 * @param e key event.
		 */
		public void keyReleased(final KeyEvent e) {
				if (e.getKeyCode() == 10) {
					toSend = chatField.getText();
					if (!toSend.equals("")) {
						chats.append("\n");
						chats.append(myName + ": " + toSend);
						chatRoomDisplay.setText(chats.toString());
						chatField.setText("");
						myPeer.sendExtraToAllPeers(toSend);
					}
				}
		    }
	}

}
