/**
 * 
 */
package org.kyj.fx.monitoring.dashboard;

/**
 * 
 */
public enum INF_STATUS {
	SUCCESS("성공"), FAIL("실패"), IN_PROGRESS("진행중"), UNKNOWN("알수없음"), RETRY("재전송");
	
	private String name;
	INF_STATUS(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
}
