package org.jboss.tools.ws.reddeer.ws.ui;

import org.jboss.reddeer.eclipse.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;

/**
 * 
 * @author Radoslav Rabara
 *
 */
public class WsCxf2xPreferencePage extends PreferencePage {
	
	public WsCxf2xPreferencePage() {
		super("Web Services", "CXF 2.x Preferences");
	}
	
	/**
	 * Added CXF is not selected by default...
	 * @param cxfHome
	 */
	public void add(String cxfHome) {
		new PushButton("Add...").click();
		new DefaultText(0).setText(cxfHome);
		new PushButton("Finish").click();
	}
	
	public void remove() {
		new PushButton("Remove").click();
	}
	
	public void remove(String cxfHome) {
		Table table = new DefaultTable();
		for(int i=0;i<table.rowCount();i++) {
			TableItem item = table.getItem(i);
			if(item.getText(1).equals(cxfHome)) {
				table.select(i);
				remove();
				return;
			}
		}
		throw new IllegalArgumentException("Row with given CXF Home was not found in CXF 2.x Preference Page");
	}
	
	public void select(String cxfHome) {
		Table table = new DefaultTable();
		for(TableItem item : table.getItems()) {
			if(item.getText(1).equals(cxfHome)) {
				item.setChecked(true);
				return;
			}
		}
		throw new IllegalArgumentException("Row with given CXF Home was not found in CXF 2.x Preference Page");
	}
}
