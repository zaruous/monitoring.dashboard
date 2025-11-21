/**
 * 
 */
package org.kyj.fx.monitoring.dashboard;

/**
 * 
 */
public class InterfaceStatusSummary {

	private int total;
	private int success;
	private int fail;
	private int retray;
	private int inProgress;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public int getFail() {
		return fail;
	}

	public void setFail(int fail) {
		this.fail = fail;
	}

	public int getRetray() {
		return retray;
	}

	public void setRetray(int retray) {
		this.retray = retray;
	}

	public int getInProgress() {
		return inProgress;
	}

	public void setInProgress(int inProgress) {
		this.inProgress = inProgress;
	}

}
