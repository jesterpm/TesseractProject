package tesseract.objects.remote;

import javax.vecmath.Vector3f;

import tesseract.World;
import tesseract.objects.tank.Tank;

public class TankMenuItem extends RemoteObjectMenuItem {
	private final World myWorld;
	
	public TankMenuItem(final RemoteObjectMenu theMenu, World theWorld) {
		super("Tank", theMenu);
		myWorld = theWorld;
	}

	@Override
	protected RemoteObject createRemoteObject() {
		return new Tank(new Vector3f(), 1, myWorld);
	}
}
