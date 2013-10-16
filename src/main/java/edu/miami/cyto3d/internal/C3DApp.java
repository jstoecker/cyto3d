package edu.miami.cyto3d.internal;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class C3DApp {

	public static final String APP_NAME = "Cyto3D";

	private final JDialog settingsDialog;

	public C3DApp(JFrame mainAppFrame) {
		settingsDialog = new JDialog(mainAppFrame, APP_NAME);
	}
}
