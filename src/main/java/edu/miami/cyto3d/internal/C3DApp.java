package edu.miami.cyto3d.internal;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class C3DApp {

    public static final String APP_NAME = "Cyto3D";

    private final JDialog      settingsDialog;
    private final JFrame       rendererFrame;

    public C3DApp(JFrame mainAppFrame) {

        // initialize OpenGL context
        GLProfile glp = GLProfile.get(GLProfile.GL2);
        GLCapabilities glc = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(glc);

        // initialize settings dialog
        settingsDialog = new JDialog(mainAppFrame, APP_NAME);
        settingsDialog.setSize(600, 600);
        settingsDialog.setLocationRelativeTo(null);

        // initialize OpenGL renderer frame
        rendererFrame = new JFrame(APP_NAME);
        rendererFrame.setSize(600, 600);
        rendererFrame.setLocationRelativeTo(null);
        rendererFrame.add(canvas);
    }

    public void showSettings() {
        settingsDialog.setVisible(true);
        rendererFrame.setVisible(true);
    }
}
