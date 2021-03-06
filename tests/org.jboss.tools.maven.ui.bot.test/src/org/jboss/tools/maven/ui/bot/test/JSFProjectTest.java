/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.maven.ui.bot.test;


import org.jboss.tools.jsf.reddeer.ui.JSFNewProjectFirstPage;
import org.jboss.tools.jsf.reddeer.ui.JSFNewProjectSecondPage;
import org.jboss.tools.jsf.reddeer.ui.JSFNewProjectWizard;
import org.junit.Before;
import org.junit.Test;

public class JSFProjectTest extends AbstractMavenSWTBotTest{
	public static final String JBOSS7_AS_HOME=System.getProperty("jbosstools.test.jboss.home.7.1");
	public static final String POM_FILE = "pom.xml";
	public static final String PROJECT_NAME7="JSFProject7";
	public static final String PROJECT_NAME7_v1="JSFProject7_1.2";
	public static final String SERVER_RUNTIME7="JBoss 7.1 Runtime";
	public static final String SERVER7="JBoss AS 7.1";
	public static final String GROUPID ="javax.faces";
	public static final String ARTIFACTID ="jsf-api";
	public static final String JSF_VERSION_1_1_02 ="1.1.02";
	public static final String JSF_VERSION_1_2 ="2.0";
	public static final String JSF_VERSION_2 ="2.0";
	
	@Before
	public void before(){
		setPerspective("Web Development");
	}
	
	@Test
	public void createJSFProjectTest_AS7_JSFv2(){
		createJSFProject(PROJECT_NAME7, "JSF 2.0", "JSFKickStartWithoutLibs", runtimeName);
		convertToMavenProject(PROJECT_NAME7, "war", false);
		addDependency(PROJECT_NAME7, GROUPID,ARTIFACTID,JSF_VERSION_2);
		buildProject(PROJECT_NAME7,"..Maven build...","clean package",true);
		checkWebTarget(PROJECT_NAME7, PROJECT_NAME7+"-0.0.1-SNAPSHOT");
	}
	
	@Test
	public void createJSFProjectTest_AS7_JSFv1() {
		createJSFProject(PROJECT_NAME7_v1, "JSF 1.2", "JSFKickStartWithoutLibs", runtimeName);
		convertToMavenProject(PROJECT_NAME7_v1, "war", false);
		addDependency(PROJECT_NAME7_v1, GROUPID,ARTIFACTID,JSF_VERSION_1_2);
		buildProject(PROJECT_NAME7_v1,"..Maven build...","clean package",true);
		checkWebTarget(PROJECT_NAME7_v1,PROJECT_NAME7_v1+"-0.0.1-SNAPSHOT");
	}

	private void createJSFProject(String name, String version, String jsfType, String runtime){
		JSFNewProjectWizard jsfd = new JSFNewProjectWizard();
		jsfd.open();
		JSFNewProjectFirstPage fp = (JSFNewProjectFirstPage)jsfd.getWizardPage(0);
		fp.setProjectName(name);
		fp.setJSFVersion(version);
		fp.setJSFType(jsfType);
		JSFNewProjectSecondPage sp = (JSFNewProjectSecondPage)jsfd.getWizardPage(1);
		sp.setRuntime(runtime);
		jsfd.finish();
		
	}
}