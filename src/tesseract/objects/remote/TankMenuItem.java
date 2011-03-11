package tesseract.objects.remote;

import javax.vecmath.Vector3f;

import tesseract.objects.tank.Tank;

public class TankMenuItem extends RemoteObjectMenuItem {
	public TankMenuItem(final RemoteObjectMenu theMenu) {
		super("Tank", theMenu);
	}

	@Override
	protected RemoteObject createRemoteObject() {
		return new Tank(new Vector3f(), 1);
	}
}
