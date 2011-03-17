package tesseract.objects.remote;

import javax.vecmath.Vector3f;

import tesseract.objects.blimp.Blimp;
import tesseract.objects.tank.Tank;

public class BlimpMenuItem extends RemoteObjectMenuItem {
	
	
	public BlimpMenuItem(final RemoteObjectMenu theMenu) {
		super("Blimp", theMenu);
		
	}

	@Override
	protected RemoteObject createRemoteObject() {
		return new Blimp(new Vector3f(), .7f);
	} 
}