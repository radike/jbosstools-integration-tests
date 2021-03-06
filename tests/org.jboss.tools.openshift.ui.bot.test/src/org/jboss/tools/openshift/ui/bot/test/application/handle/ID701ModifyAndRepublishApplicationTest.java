package org.jboss.tools.openshift.ui.bot.test.application.handle;

import static org.junit.Assert.fail;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID407CreateApplicationFromExistingAndChangeRemoteNameTest;
import org.jboss.tools.openshift.ui.bot.test.application.create.IDXXXCreateTestingApplication;
import org.jboss.tools.openshift.ui.condition.ApplicationIsDeployedSuccessfully;
import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.junit.Test;

/**
 * Test capabilities of modifying and republishing an application.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID701ModifyAndRepublishApplicationTest extends IDXXXCreateTestingApplication {

	@Test
	public void testModifyAndRepublishApplication() {
		modifyAndRepublishApplication(applicationName);
	}
	
	public static void modifyAndRepublishApplication(String applicationName) {
		TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
		ProjectExplorer explorer = new ProjectExplorer();
		explorer.getProject(applicationName).getProjectItem("diy", "index.html").open();
		
		TextEditor editor = new TextEditor("index.html");
		editor.setText(ID407CreateApplicationFromExistingAndChangeRemoteNameTest.HTML_TEXT);
		editor.save();
		editor.close();
		
		ServersView servers = new ServersView();
		servers.open();
		treeViewerHandler.getTreeItem(new DefaultTree(), applicationName + " at OpenShift").select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.PUBLISH).select();
		
		try {
			new WaitUntil(new ShellWithTextIsAvailable("Identify Yourself"), TimePeriod.NORMAL);
			new DefaultShell("Identify Yourself").setFocus();
			new PushButton("OK").click();
		} catch (WaitTimeoutExpiredException ex) {}
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.PUBLISH_CHANGES), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.PUBLISH_CHANGES);
		new DefaultStyledText(0).setText("Commit message");
		
		new WaitUntil(new ButtonWithTextIsActive(new PushButton(OpenShiftLabel.Button.COMMIT_PUBLISH)),
				TimePeriod.NORMAL);
		
		new PushButton(OpenShiftLabel.Button.COMMIT_PUBLISH).click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		AbstractWait.sleep(TimePeriod.NORMAL);
		
		try {
			new WaitUntil(new ApplicationIsDeployedSuccessfully(Datastore.USERNAME, 
				Datastore.DOMAIN, applicationName, "OpSh"), TimePeriod.VERY_LONG);
			// PASS
		} catch (WaitTimeoutExpiredException ex) {
			fail("Application has not been deployed successfully. Browser does not "
					+ "contain text of existing project which has been deployed.");
		}
	}
}
