package org.jboss.tools.central.test.ui.reddeer;

import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.m2e.core.ui.preferences.MavenSettingsPreferencePage;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.handler.ShellHandler;
import org.jboss.reddeer.swt.impl.toolbar.WorkbenchToolItem;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.central.reddeer.api.ExamplesOperator;
import org.jboss.tools.central.reddeer.projects.ArchetypeProject;
import org.jboss.tools.central.test.ui.reddeer.projects.HTML5Project;
import org.jboss.tools.central.test.ui.reddeer.projects.JavaEEWebProject;
import org.jboss.tools.central.test.ui.reddeer.projects.RichfacesProject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

@JBossServer(type = ServerReqType.EAP, state = ServerReqState.RUNNING)
public class ArchetypesTest {

	private static Map<org.jboss.tools.central.reddeer.projects.Project, List<String>> projectWarnings = new HashMap<org.jboss.tools.central.reddeer.projects.Project, List<String>>();
	
	@InjectRequirement
	ServerRequirement req;

	@Before
	public void setup() {
		MavenSettingsPreferencePage prefPage = new MavenSettingsPreferencePage();
		String mvnConfigFileName = System.getProperty("maven.config.file");
		prefPage.open();
		prefPage.setUserSettingsLocation(mvnConfigFileName);
		prefPage.ok();
		new WorkbenchToolItem("JBoss Central").click();
		// activate central editor
		new DefaultEditor("JBoss Central");
	}

	@After
	public void teardown() {
		for (Project p : new ProjectExplorer().getProjects()) {
			p.delete(true);
		}
		ShellHandler.getInstance().closeAllNonWorbenchShells();
	}
	
	@AfterClass
	public static void teardownClass() {
		StringBuilder sb = new StringBuilder();
		boolean fail = false;
		for (Entry<org.jboss.tools.central.reddeer.projects.Project, List<String>> projectWarning : projectWarnings
				.entrySet()) {
			sb.append(projectWarning.getKey().getName() + "\n\r");
			if (!projectWarning.getValue().isEmpty()) fail = true;
			for (String warning : projectWarning.getValue()) {
				sb.append("\t" + warning + "\n\r");
			}
		}
		projectWarnings.clear();
		assertFalse(sb.toString(), fail);
	}


	@Test
	public void HTML5ProjectTest() {
		ArchetypeProject project = new HTML5Project();
		importArchetypeProject(project);
	}

	@Test
	public void RichfacesProjectTest() {
		importArchetypeProject(new RichfacesProject());
	}

	@Test
	public void JavaEEWebProjectTest() {
		importArchetypeProject(new JavaEEWebProject(false));
	}

	@Test
	public void JavaEEWebProjectBlankTest() {
		importArchetypeProject(new JavaEEWebProject(true));
	}

	private void importArchetypeProject(ArchetypeProject project) {
		ExamplesOperator.getInstance().importArchetypeProject(project);
		ExamplesOperator.getInstance().deployProject(project.getProjectName(),
				req.getServerNameLabelText(req.getConfig()));
		ExamplesOperator.getInstance().checkDeployedProject(
				project.getProjectName(),
				req.getServerNameLabelText(req.getConfig()));
		projectWarnings.put(project, ExamplesOperator.getInstance().getAllWarnings());
	}
}
