/*******************************************************************************
 * Copyright (c) 2010-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.bot.test.quickfix.dialog;

import static org.junit.Assert.*;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTableItem;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.cdi.text.ext.hyperlink.AssignableBeansDialog;
import org.junit.Test;

@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
@OpenPerspective(JavaEEPerspective.class)
@CleanWorkspace
public class AllAssignableDialogTest extends CDITestBase {

	private String appClass = "App.java";

	@Override
	public String getProjectName() {
		return "CDIAssignableDialogTest";
	}

	@Test
	public void testDecorator() {
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(getProjectName()).getProjectItem(CDIConstants.SRC,
				getPackageName(), appClass).open();
		TextEditor editor = new TextEditor(appClass);
		editor.selectText("manager");
		new ShellMenu("Navigate","Open Hyperlink").select();
		new DefaultShell();
		new DefaultTableItem(CDIConstants.SHOW_ALL_ASSIGNABLE).select();
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.CR);
		AssignableBeansDialog assignDialog = new AssignableBeansDialog();
		assignDialogShowAll(assignDialog);
		assertTrue(assignDialog.getAllBeans().size() == 6);
		assertTrue(assignDialog.getAllBeans().contains(
				"@Decorator D1 - " + getPackageName() + " - /"
						+ getProjectName() + "/src"));

		assignDialog.hideDecorators();
		assertTrue(assignDialog.getAllBeans().size() == 5);
		assertFalse(assignDialog.getAllBeans().contains(
				"@Decorator D1 - " + getPackageName() + " - /"
						+ getProjectName() + "/src"));
		assignDialog.close();

	}

	@Test
	public void testInterceptor() {

		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(getProjectName()).getProjectItem(CDIConstants.SRC,
				getPackageName(), appClass).open();
		TextEditor editor = new TextEditor(appClass);
		editor.selectText("manager");
		new ShellMenu("Navigate","Open Hyperlink").select();
		new DefaultShell();
		new DefaultTableItem(CDIConstants.SHOW_ALL_ASSIGNABLE).select();
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.CR);
		AssignableBeansDialog assignDialog = new AssignableBeansDialog();
		assignDialogShowAll(assignDialog);
		assertTrue(assignDialog.getAllBeans().size() == 6);
		assertTrue(assignDialog.getAllBeans().contains(
				"@Interceptor I1 - " + getPackageName() + " - /"
						+ getProjectName() + "/src"));
		assignDialog.hideInterceptors();
		assertTrue(assignDialog.getAllBeans().size() == 5);
		assertFalse(assignDialog.getAllBeans().contains(
				"@Interceptor I1 - " + getPackageName() + " - /"
						+ getProjectName() + "/src"));
		assignDialog.close();

	}

	@Test
	public void testUnselectedAlternative() {

		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(getProjectName()).getProjectItem(CDIConstants.SRC,
				getPackageName(), appClass).open();
		TextEditor editor = new TextEditor(appClass);
		editor.selectText("manager");
		new ShellMenu("Navigate","Open Hyperlink").select();
		new DefaultShell();
		new DefaultTableItem(CDIConstants.SHOW_ALL_ASSIGNABLE).select();
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.CR);
		AssignableBeansDialog assignDialog = new AssignableBeansDialog();
		assignDialogShowAll(assignDialog);
		assertTrue(assignDialog.getAllBeans().size() == 6);
		assertTrue(assignDialog.getAllBeans().contains(
				"@Alternative BasicManager - " + getPackageName() + " - /"
						+ getProjectName() + "/src"));
		assignDialog.hideUnselectedAlternatives();
		assertTrue(assignDialog.getAllBeans().size() == 5);
		assertFalse(assignDialog.getAllBeans().contains(
				"@Alternative BasicManager - " + getPackageName() + " - /"
						+ getProjectName() + "/src"));
		assignDialog.close();

	}

	@Test
	public void testUnavailableProducer() {

		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(getProjectName()).getProjectItem(CDIConstants.SRC,
				getPackageName(), appClass).open();
		TextEditor editor = new TextEditor(appClass);
		editor.selectText("manager");
		new ShellMenu("Navigate","Open Hyperlink").select();
		new DefaultShell();
		new DefaultTableItem(CDIConstants.SHOW_ALL_ASSIGNABLE).select();
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.CR);
		AssignableBeansDialog assignDialog = new AssignableBeansDialog();
		assignDialogShowAll(assignDialog);
		assertTrue(assignDialog.getAllBeans().size() == 6);
		assertTrue(assignDialog.getAllBeans().contains(
				"@Produces BasicManager.getManager() - " + getPackageName()
						+ " - /" + getProjectName() + "/src"));
		assignDialog.hideUnavailableProducers();
		assertTrue(assignDialog.getAllBeans().size() == 5);
		assertFalse(assignDialog.getAllBeans().contains(
				"@Produces BasicManager.getManager() - " + getPackageName()
						+ " - /" + getProjectName() + "/src"));
		assignDialog.close();

	}

	@Test
	public void testSpecializedBeans() {

		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(getProjectName()).getProjectItem(CDIConstants.SRC,
				getPackageName(), appClass).open();
		TextEditor editor = new TextEditor(appClass);
		editor.selectText("manager");
		new ShellMenu("Navigate","Open Hyperlink").select();
		new DefaultShell();
		new DefaultTableItem(CDIConstants.SHOW_ALL_ASSIGNABLE).select();
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.CR);
		AssignableBeansDialog assignDialog = new AssignableBeansDialog();
		assignDialogShowAll(assignDialog);
		assertTrue(assignDialog.getAllBeans().size() == 6);
		assertTrue(assignDialog.getAllBeans().contains(
				"AbstractManager - " + getPackageName() + " - /"
						+ getProjectName() + "/src"));
		assignDialog.hideSpecializedBeans();
		assertTrue(assignDialog.getAllBeans().size() == 5);
		assertFalse(assignDialog.getAllBeans().contains(
				"AbstractManager - " + getPackageName() + " - /"
						+ getProjectName() + "/src"));
		assignDialog.close();

	}

	@Test
	public void testAmbiguousBeans() {
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(getProjectName()).getProjectItem(CDIConstants.SRC,
				getPackageName(), appClass).open();
		TextEditor editor = new TextEditor(appClass);
		editor.selectText("managerImpl");
		new ShellMenu("Navigate","Open Hyperlink").select();
		new DefaultShell();
		new DefaultTableItem(CDIConstants.SHOW_ALL_ASSIGNABLE).select();
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.CR);
		AssignableBeansDialog assignDialog = new AssignableBeansDialog();
		assignDialogShowAll(assignDialog);
		assertTrue(assignDialog.getAllBeans().size() == 3);
		assertTrue(assignDialog.getAllBeans().contains(
				"@Alternative Manager1 - " + getPackageName() + " - /"
						+ getProjectName() + "/src"));
		assertTrue(assignDialog.getAllBeans().contains(
				"Manager2 - " + getPackageName() + " - /" + getProjectName()
						+ "/src"));
		assertTrue(assignDialog.getAllBeans().contains(
				"Manager3 - " + getPackageName() + " - /" + getProjectName()
						+ "/src"));
		assignDialog.hideAmbiguousBeans();
		assertTrue(assignDialog.getAllBeans().size() == 1);
		assertTrue(assignDialog.getAllBeans().contains(
				"@Alternative Manager1 - " + getPackageName() + " - /"
						+ getProjectName() + "/src"));
		assertFalse(assignDialog.getAllBeans().contains(
				"Manager2 - " + getPackageName() + " - /" + getProjectName()
						+ "/src"));
		assertFalse(assignDialog.getAllBeans().contains(
				"Manager3 - " + getPackageName() + " - /" + getProjectName()
						+ "/src"));
		assignDialog.close();

	}
	
	private void assignDialogShowAll(AssignableBeansDialog assignDialog){
		assignDialog.showAmbiguousBeans();
		assignDialog.showDecorators();
		assignDialog.showInterceptors();
		assignDialog.showSpecializedBeans();
		assignDialog.showUnavailableBeans();
		assignDialog.showUnavailableProducers();
		assignDialog.showUnselectedAlternatives();
	}

}
