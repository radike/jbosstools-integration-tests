/*******************************************************************************
 * Copyright (c) 2010-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.utils;

import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardPage;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.ws.reddeer.ui.wizards.CreateNewFileWizardPage;
import org.jboss.tools.ws.reddeer.ui.wizards.jst.j2ee.EARProjectWizard;
import org.jboss.tools.ws.reddeer.ui.wizards.jst.servlet.DynamicWebProjectWizard;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.NewWsdlFileWizard;

/**
 * @author jjankovi
 * @author Radoslav Rabara
 */
public class ProjectHelper {

	private final ProjectExplorer projectExplorer = new ProjectExplorer();

	/**
	 * Method creates basic java class for entered project with 
	 * entered package and class name
	 * @param projectName
	 * @param pkg
	 * @param cName
	 * @return
	 */
	public TextEditor createClass(String projectName, String pkg, String className) {
		NewJavaClassWizardDialog wizard = new NewJavaClassWizardDialog();
		wizard.open();

		NewJavaClassWizardPage page = wizard.getFirstPage();
		page.setPackage(pkg);
		page.setName(className);
		page.setSourceFolder(projectName + "/src");
		wizard.finish();

		return new TextEditor(className + ".java");
	}

	/**
	 * Method creates wsdl file for entered project with 
	 * entered package name
	 * @param projectName
	 * @param wsdlFileName
	 */
	public TextEditor createWsdl(String projectName, String wsdlFileName) {
		NewWsdlFileWizard wizard = new NewWsdlFileWizard();
		wizard.open();

		CreateNewFileWizardPage page = new CreateNewFileWizardPage();
		page.setFileName(wsdlFileName + ".wsdl");
		page.setParentFolder(projectName + "/src");

		wizard.next();
		wizard.finish();

		return new TextEditor(wsdlFileName + ".wsdl");
	}
	
	/**
	 * Method creates new Dynamic Web Project with entered name
	 * @param name
	 */
	public void createProject(String name) {
		DynamicWebProjectWizard wizard = new DynamicWebProjectWizard();
		wizard.open();

		wizard.setProjectName(name);
		wizard.next();
		wizard.next();
		wizard.setGenerateDeploymentDescriptor(true);
		wizard.finish();

		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		projectExplorer.getProject(name).select();
	}
	
	/**
	 * Method creates new Dynamic Web Project with entered name for
	 * ear project
	 * @param name
	 */
	public void createProjectForEAR(String name, String earProject) {
		DynamicWebProjectWizard wizard = new DynamicWebProjectWizard();
		wizard.open();

		wizard.setProjectName(name);
		wizard.addProjectToEar(earProject);
		wizard.finish();

		new WaitWhile(new JobIsRunning());
		projectExplorer.getProject(name).select();
	}

	/**
	 * Method creates new EAR Project with entered name
	 * @param name
	 */
	public void createEARProject(String name) {
		EARProjectWizard wizard = new EARProjectWizard();
		wizard.open();

		new LabeledText("Project name:")
			.setText(name);
		// set EAR version
		DefaultCombo combo = new DefaultCombo(1);
		combo.setSelection(combo.getItems().size()-1);

		wizard.next();

		new CheckBox("Generate application.xml deployment descriptor").click();

		wizard.finish();
	}

	/**
	 * Method generates Deployment Descriptor for entered project 
	 * @param projectName
	 */
	public void createDD(String projectName) {
		Project project = projectExplorer.getProject(projectName);
		expandProject(project.getTreeItem());
		String dd = "Deployment Descriptor: " + projectName;
		project.getProjectItem(dd).select();

        new ContextMenu("Generate Deployment Descriptor Stub").select();

        new WaitWhile(new JobIsRunning());
    }

	private void expandProject(TreeItem projectItem) {
		do {
			projectItem.collapse();
			projectItem.expand();
		} while (!projectItem.isExpanded());
	}
	
}
