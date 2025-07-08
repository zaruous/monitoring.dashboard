/**
 * 
 */
package org.kyj.fx.monitoring.dashboard;

/**
 * 
 */
public class OverallStatusController {

	public void reloadData() {
		new DatabaseManager().getInterfaceStatusDetails(null)
	}

}
