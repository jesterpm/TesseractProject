package tesseract.objects.remote;

import java.io.Serializable;

public class KeyInfo implements Serializable {
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -1378329597453071920L;
	
	private int keyCode;
	
	public KeyInfo(int keyCode) {
		this.keyCode = keyCode;
	}
	
	public int getKeyCode() {
		return keyCode;
	}
}
