package org.kyj.fx.monitoring.dashboard;

import java.time.LocalDate;
import java.time.LocalTime;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ServiceErrorEntry {
	private StringProperty errorId = new SimpleStringProperty();
	private StringProperty errorCode = new SimpleStringProperty();
	private StringProperty errorMsg = new SimpleStringProperty();
	private StringProperty errorDesc = new SimpleStringProperty();
	private IntegerProperty count = new SimpleIntegerProperty();
	private StringProperty errorLog = new SimpleStringProperty();
	private ObjectProperty<LocalTime> regDate = new SimpleObjectProperty();

	public ServiceErrorEntry(String errorId, String errorCode, String errorMsg, String errorDesc, int count, String errorLog, LocalTime regDate) {
		this.errorId.set(errorId);
		this.errorCode.set(errorCode);
		this.errorMsg.set(errorMsg);
		this.errorDesc.set(errorDesc);
		this.count.set(count);
		this.errorLog.set(errorLog);
		this.regDate.set(regDate);
	}
	
	public final StringProperty errorCodeProperty() {
		return this.errorCode;
	}

	public final String getErrorCode() {
		return this.errorCodeProperty().get();
	}

	public final void setErrorCode(final String errorCode) {
		this.errorCodeProperty().set(errorCode);
	}

	public final StringProperty errorMsgProperty() {
		return this.errorMsg;
	}

	public final String getErrorMsg() {
		return this.errorMsgProperty().get();
	}

	public final void setErrorMsg(final String errorMsg) {
		this.errorMsgProperty().set(errorMsg);
	}

	public final StringProperty errorDescProperty() {
		return this.errorDesc;
	}

	public final String getErrorDesc() {
		return this.errorDescProperty().get();
	}

	public final void setErrorDesc(final String errorDesc) {
		this.errorDescProperty().set(errorDesc);
	}

	public final IntegerProperty countProperty() {
		return this.count;
	}

	public final int getCount() {
		return this.countProperty().get();
	}

	public final void setCount(final int count) {
		this.countProperty().set(count);
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

	public final ObjectProperty<LocalTime> regDateProperty() {
		return this.regDate;
	}

	public final LocalTime getRegDate() {
		return this.regDateProperty().get();
	}

	public final void setRegDate(final LocalTime regDate) {
		this.regDateProperty().set(regDate);
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

}
