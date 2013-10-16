package edu.miami.cyto3d.internal;

import java.awt.event.ActionEvent;
import java.util.Properties;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.service.util.AbstractCyActivator;
import org.osgi.framework.BundleContext;

/** Entry point for the OSGi bundle app */
public class CyActivator extends AbstractCyActivator {

	private C3DApp app;
	private CyApplicationManager cyApplicationManager;

	@Override
	public void start(BundleContext context) throws Exception {
		cyApplicationManager = getService(context, CyApplicationManager.class);

		CySwingApplication cySwingApplication = getService(context, CySwingApplication.class);
		app = new C3DApp(cySwingApplication.getJFrame());

		MenuAction action = new MenuAction();
		Properties properties = new Properties();
		registerAllServices(context, action, properties);
	}

	/** Triggered when the user clicks on the menu item for the app */
	private class MenuAction extends AbstractCyAction {

		public MenuAction() {
			super(C3DApp.APP_NAME, cyApplicationManager, null, null);
			setPreferredMenu("Apps");
		}

		public void actionPerformed(ActionEvent e) {

		}
	}
}
