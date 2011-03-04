package tesseract.chatbox;

import java.awt.BorderLayout;
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

public class Chatbox extends JFrame implements Observer {
	
	private final JPanel textPanel;
	private final JScrollPane scroller;
	private final JTextArea textArea;
	private final JButton submitButton;
	private final JTextField chatField;
	private String myName;
	private String toSend;
	private StringBuilder chats;
	
	public Chatbox() {
		textPanel = new JPanel(new BorderLayout());
		textArea = new JTextArea(10, 80);
		textArea.setEditable(false);
		scroller = new JScrollPane(textArea);
		submitButton = new JButton("Submit");
		chatField = new JTextField();
		this.setName("Chat");
		chats = new StringBuilder();
		buildFrame();
		attachListeners();
	}
	
	private void attachListeners() {
		submitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//TODO: SEND MESSAGE IN chatField.
				if (arg0.getSource().equals(submitButton)) {
					toSend = chatField.getText();
					chatField.setText("");
					// PeerMessage message = new PeerMessage(Message, null, toSend);
					// Send(message);
				}
			}
		});
		
		
		//chatField.addKeyListener()
		
	}

	public boolean setMyName(final String theName) {
		myName = theName;
		return myName.equals(theName);
	}
	
	private void buildFrame() {
		textPanel.add(scroller, BorderLayout.CENTER);
		JPanel southPanel = new JPanel();
		GroupLayout layout = new GroupLayout(southPanel);
		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addComponent(chatField)
				.addComponent(submitButton)
		);
		textPanel.add(southPanel, BorderLayout.SOUTH);
		this.add(textPanel);
		this.pack();
		this.setVisible(false);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		String inc = " ";
		String source = " ";
		// TODO Listen for PeerMessage with string in it.
		/*
		if (((PeerMessage) arg0).getExtra().getClass == inc.getClass()) {
				inc = (PeerMessage) arg0).getExtra();
				source = ((PeerMessage) arg0).getPeer.getName();
		      chats.append("\n");
		      chats.append(source + ": " + inc);
				
		 }//*/
		
		
	}
	
	private class ChatFieldListener extends KeyAdapter {
					
		public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					toSend = chatField.getText();
					chatField.setText("");
				}
		    }
	}

}
