package org.jboss.tools.openshift.ui.bot.test.application.create;

import static org.junit.Assert.assertTrue;

import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.DeleteApplication;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.wizard.application.NewApplicationWizard;
import org.jboss.tools.openshift.ui.wizard.application.OpenNewApplicationWizard;
import org.junit.After;
import org.junit.Test;

/**
 * Test capabilities of switching a domain in New application wizard for a new
 * application. Opens New application wizard on first domain but selects second domain.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID412UseAnotherDomainTest {

	private String applicationName = "diy" + System.currentTimeMillis();
	
	@Test
	public void testSwitchDomain() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		OpenNewApplicationWizard.openWizardFromExplorer(Datastore.USERNAME, Datastore.DOMAIN);
		new NewApplicationWizard().createNewApplicationOnBasicCartridge(OpenShiftLabel.Cartridge.DIY,
				Datastore.SECOND_DOMAIN, applicationName, false, true, false, false, 
				null, null, true, null, null, null, (String[]) null);
		
		new NewApplicationWizard().postCreateSteps(true);
		
		assertTrue("Application has not been created on selected domain.",
				explorer.applicationExists(Datastore.USERNAME, Datastore.SECOND_DOMAIN, applicationName));
	}
	
	@After
	public void deleteApplication() {
		new DeleteApplication(Datastore.USERNAME, Datastore.SECOND_DOMAIN, applicationName).perform();
	}
	
}
