/**
 * 
 */
package org.kyj.fx.monitoring.dashboard;

/**
 * 
 */
public class ScheduleEntry {
	private String scheduleId;
	private String interfaceName;
	private String status;
	private String executionTime; // 시간 부분만 표시
	private String duration;

	public ScheduleEntry(String scheduleId, String interfaceName, String status, String fullExecutionTime,
			String duration) {
		this.scheduleId = scheduleId;
		this.interfaceName = interfaceName;
		this.status = status;
		// fullExecutionTime에서 시간 부분만 추출 (예: "2024-05-28 02:00:15" -> "02:00:15")
		this.executionTime = fullExecutionTime.split(" ").length > 1 ? fullExecutionTime.split(" ")[1]
				: fullExecutionTime;
		this.duration = duration;
	}

	public String getScheduleId() {
		return scheduleId;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public String getStatus() {
		return status;
	}

	public String getExecutionTimeDisplay() {
		return executionTime + (duration != null && !duration.isEmpty() ? " (" + duration + ")" : "");
	}

	public String getExecutionTime() {
		return executionTime;
	} // 정렬 등을 위해 순수 시간 값

	public String getDuration() {
		return duration;
	}
}
