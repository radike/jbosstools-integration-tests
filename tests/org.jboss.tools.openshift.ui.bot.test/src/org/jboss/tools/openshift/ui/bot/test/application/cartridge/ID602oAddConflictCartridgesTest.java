package org.jboss.tools.openshift.ui.bot.test.application.cartridge;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.NoButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.DeleteApplication;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.wizard.application.Templates;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ID602oAddConflictCartridgesTest {

	private String applicationName = "eap" + System.currentTimeMillis();	
	
	@Before
	public void createApplication() {
		new Templates(Datastore.USERNAME, Datastore.DOMAIN, false).createSimpleApplicationOnBasicCartridges(
				OpenShiftLabel.Cartridge.JBOSS_EAP, applicationName, true, true, true);
	}
	
	@Test
	public void testAddConflictCartridge() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		TreeItem application = explorer.getApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName);
		application.select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.EMBED_CARTRIDGE).select();
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_CARTRIDGES);
		
		// Unappropriate application
		new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.PHP_MYADMIN).select();
		new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.PHP_MYADMIN).setChecked(true);
		
		try {
			new WaitUntil(new ShellWithTextIsAvailable("Inappropriate application " + applicationName), 
					TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Shell warning about inappropriate application has not been shown");
		}
		
		new DefaultShell("Inappropriate application " + applicationName);
		
		new NoButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable("Inappropriate application " + applicationName), 
				TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_CARTRIDGES);
		
		// Another cartridge required
		new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.GEN_MONGO).select();
		new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.GEN_MONGO).setChecked(true);
		
		try {
			new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.ADD_CARTRIDGE_DIALOG), 
					TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Shell offering embedding required cartridge has not been shown");
		}
		
		new DefaultShell(OpenShiftLabel.Shell.ADD_CARTRIDGE_DIALOG);
		
		new PushButton(OpenShiftLabel.Button.APPLY).click();
		
		assertTrue("Cartridge has not been added after confirmation of adding embeddable cartridge.",
				new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.MONGO_DB).isChecked());
	
		new CancelButton().click(); 
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EDIT_CARTRIDGES), TimePeriod.LONG);
	}
	
	@After
	public void deleteApplication() {
		new DeleteApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName).perform();
	}
}
