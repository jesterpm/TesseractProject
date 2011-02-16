package tesseract.newmenu;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

abstract public class MenuItem extends JMenuItem implements ActionListener{
	
	private JFrame myFrame;
	private JPanel myPanel;
	private JTextField[] myFields;
	private HashMap <String, String> myParameters;
	private HashMap <String, JTextField> myReadData;
	
	public MenuItem (HashMap <String, String> theParams) {
		myFrame = new JFrame();
		myPanel = new JPanel(new GridLayout(myParameters.keySet().size(), 2));
		myParameters = (HashMap<String, String>) theParams;
		myFields = new JTextField[myParameters.keySet().size()];
		myReadData = new HashMap <String, JTextField>();
		makePanel();
	}
	
	private void makePanel() {
		Set<String> varNames = myParameters.keySet();
		int i = 0;
		for (String s : varNames) {
			myPanel.add(new JLabel(s));
			myPanel.add(myFields[i]);
			myReadData.put(s, myFields[i]);
			i++;
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
