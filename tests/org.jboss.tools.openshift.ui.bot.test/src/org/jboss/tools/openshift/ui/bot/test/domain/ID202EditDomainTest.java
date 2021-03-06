package org.jboss.tools.openshift.ui.bot.test.domain;

import static org.junit.Assert.fail;

import java.util.Random;

import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView;
import org.junit.Test;

/**
 * Test editing domain name via context menu.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID202EditDomainTest {

	@Test
	public void testEditDomainName() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.getDomain(Datastore.USERNAME, Datastore.DOMAIN).select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.EDIT_DOMAIN).select();
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_DOMAIN);
		
		Datastore.DOMAIN = "jbdsqedomain" + new Random().nextInt(100);
		new LabeledText(OpenShiftLabel.TextLabels.DOMAIN_NAME).setText(Datastore.DOMAIN);
		
		new WaitUntil(new ButtonWithTextIsActive(new FinishButton()), TimePeriod.NORMAL);
		
		new FinishButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EDIT_DOMAIN), TimePeriod.LONG);
		
		explorer.getConnection(Datastore.USERNAME).select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.REFRESH).select();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		try {
			explorer.getDomain(Datastore.USERNAME, Datastore.DOMAIN);
			// PASS
		} catch (JFaceLayerException ex) {
			fail("Domain name has not been successfully refreshed in OpenShift explorer after renaming."
					+ " Domain name should be " + Datastore.DOMAIN);
		}
	}
}
