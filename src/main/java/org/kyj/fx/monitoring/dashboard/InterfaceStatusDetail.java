package org.kyj.fx.monitoring.dashboard;

public class InterfaceStatusDetail {

	private String id;
	private String name;
	private String timestamp;
	private String duration;
	private String server;
	private String errorCode;
	private String errorMessage;

	public InterfaceStatusDetail(String id, String name, String timestamp, String duration, String server,
			String errorCode, String errorMessage) {
		this.id = id;
		this.name = name;
		this.timestamp = timestamp;
		this.duration = duration;
		this.server = server;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public InterfaceStatusDetail(String id, String name, String timestamp, String duration, String server) {
		this(id, name, timestamp, duration, server, null, null);
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public String getDuration() {
		return duration;
	}

	public String getServer() {
		return server;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
