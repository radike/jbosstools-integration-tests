/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.aerogear.ui.bot.test.app;

import org.jboss.tools.aerogear.ui.bot.test.AerogearBotTest;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.junit.Test;

@Require(clearWorkspace = true)
public class OpenConfigEditor extends AerogearBotTest {
	@Test
	public void canOpenConfigXmlEditor() {
		projectExplorer.selectProject(CORDOVA_PROJECT_NAME);
		openInConfigEditor(CORDOVA_PROJECT_NAME);

		assertTrue(bot.activeEditor().getTitle()
				.equalsIgnoreCase(CORDOVA_APP_NAME));
	}
	@Override
	public void tearDown() {
		// close config editor before deleting project
		bot.activeEditor().close();
		super.tearDown();
	}

}
