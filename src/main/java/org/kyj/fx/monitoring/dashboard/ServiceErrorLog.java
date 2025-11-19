package org.kyj.fx.monitoring.dashboard;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ServiceErrorLog {
	private StringProperty errorId = new SimpleStringProperty();
	private StringProperty errorLog = new SimpleStringProperty();

	public ServiceErrorLog() {
	}
	
	public ServiceErrorLog(String errorId, String errorLog) {
		this.errorId.set(errorId);
		this.errorLog.set(errorLog);
	}

	public final StringProperty errorIdProperty() {
		return this.errorId;
	}

	public final String getErrorId() {
		return this.errorIdProperty().get();
	}

	public final void setErrorId(final String errorId) {
		this.errorIdProperty().set(errorId);
	}

	public final StringProperty errorLogProperty() {
		return this.errorLog;
	}

	public final String getErrorLog() {
		return this.errorLogProperty().get();
	}

	public final void setErrorLog(final String errorLog) {
		this.errorLogProperty().set(errorLog);
	}

}
