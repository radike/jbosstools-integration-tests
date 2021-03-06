package org.jboss.tools.hibernate.reddeer.test;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.tools.hibernate.reddeer.console.EditConfigurationMainPage;
import org.jboss.tools.hibernate.reddeer.console.EditConfigurationMainPage.PredefinedConnection;
import org.jboss.tools.hibernate.reddeer.console.EditConfigurationShell;
import org.jboss.tools.hibernate.reddeer.console.KnownConfigurationsView;
import org.jboss.tools.hibernate.reddeer.console.NewConfigurationLocationPage;
import org.jboss.tools.hibernate.reddeer.console.NewConfigurationSettingPage;
import org.jboss.tools.hibernate.reddeer.console.NewHibernateConfigurationWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Console configuration test
 * Creates Hibernate Configuration file (cfg.xml)
 * @author Jiri Peterka
 *
 */
@RunWith(RedDeerSuite.class)
@Database(name="testdb")
public class ConsoleConfigurationTest extends HibernateRedDeerTest {

	private String PROJECT_NAME = "consoletest";
	private String PROJECT_LIBS = "hibernatelib"; 
	private String HIBERNATE_CFG_FILE="/" + PROJECT_NAME + "/src/hibernate.cfg.xml";
	private String CONSOLE_NAME="hibernateconsoletest";
	
	@Before 
	public void prepare() {
		importProject(PROJECT_LIBS);
		importProject(PROJECT_NAME);		
		prepareConsoleConfiguration();
	}
	
	private void prepareConsoleConfiguration() {
		NewHibernateConfigurationWizard wizard = new NewHibernateConfigurationWizard();
		wizard.open();
		NewConfigurationLocationPage p1 = (NewConfigurationLocationPage)wizard.getFirstPage();
		p1.setLocation(PROJECT_NAME,"src");		
		wizard.next();

		NewConfigurationSettingPage p2 = (NewConfigurationSettingPage)wizard.getWizardPage();
		p2.setDatabaseDialect(p.getProperty(RuntimeDBProperty.dialect));
		p2.setDriverClass(p.getProperty(RuntimeDBProperty.driver));
		p2.setConnectionURL(p.getProperty(RuntimeDBProperty.jdbc));
		p2.setUsername(p.getProperty(RuntimeDBProperty.username));			
		wizard.finish();
	}

	@Test
	public void testCreateConsoleConfiguration() {
		KnownConfigurationsView v = new KnownConfigurationsView();
		v.open();
		v.triggerAddConfigurationDialog();
		
		EditConfigurationShell s = new EditConfigurationShell();
		s.setName(CONSOLE_NAME);
		
		
		EditConfigurationMainPage p = s.getMainPage();		
				
		p.setProject(PROJECT_NAME);
		p.setDatabaseConnection(PredefinedConnection.JPA_PROJECT_CONFIGURED_CONNECTION);
		p.setDatabaseConnection(PredefinedConnection.HIBERNATE_CONFIGURED_CONNECTION);		
		p.setConfigurationFile(HIBERNATE_CFG_FILE);
		
		s.ok();
		
		v.open();
		EditConfigurationShell s2 = v.openConsoleConfiguration(CONSOLE_NAME);
		s2.close();
		
		v.open();
		v.selectNode(CONSOLE_NAME,"Database","SAKILA.PUBLIC","ACTOR");
	}
	
	

	@After 
	public void clean() {			
		ProjectExplorer pe = new ProjectExplorer();
		pe.getProject(PROJECT_NAME).delete(true);
	}
}
