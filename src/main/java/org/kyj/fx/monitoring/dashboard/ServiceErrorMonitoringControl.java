package org.kyj.fx.monitoring.dashboard;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

public class ServiceErrorMonitoringControl extends CardControl {

	private TableView<ServiceErrorEntry> tableView;
	private DatabaseManager dbManager;
	private DatePicker datePicker;

	public ServiceErrorMonitoringControl() {
		super("서비스 에러 내역");
		dbManager = DatabaseManager.getInstance();

		datePicker = new DatePicker(LocalDate.now());
		datePicker.setOnAction(event -> reloadData());

		HBox filterBox = new HBox(10, new Label("조회일:"), datePicker);
		filterBox.setAlignment(Pos.CENTER_LEFT);

		tableView = new TableView<>();

		TableColumn<ServiceErrorEntry, String> errorCodeCol = new TableColumn<>("ERROR_CODE");
		errorCodeCol.setCellValueFactory(v -> v.getValue().errorCodeProperty());

		TableColumn<ServiceErrorEntry, String> errorDescCol = new TableColumn<>("ERROR_DESC");
		errorDescCol.setCellValueFactory(v -> v.getValue().errorDescProperty());

		TableColumn<ServiceErrorEntry, String> errorMsgCol = new TableColumn<>("ERROR_MSG");
		errorMsgCol.setCellValueFactory(v -> v.getValue().errorMsgProperty());
		
		TableColumn<ServiceErrorEntry, Integer> countCol = new TableColumn<>("COUNT");
		countCol.setCellValueFactory(v -> v.getValue().countProperty().asObject());

		TableColumn<ServiceErrorEntry, LocalTime> regDateCol = new TableColumn<>("발생일");
		regDateCol.setCellValueFactory(v -> v.getValue().regDateProperty());

		tableView.getColumns().addAll(errorCodeCol, errorDescCol,errorMsgCol, countCol, regDateCol);
		VBox.setVgrow(tableView, Priority.ALWAYS);

		reloadData(); // 초기 데이터 로드

		this.getChildren().addAll(filterBox, tableView);
		VBox.setVgrow(this, Priority.ALWAYS);
		HBox.setHgrow(this, Priority.ALWAYS);

		this.tableView.addEventHandler(MouseEvent.MOUSE_CLICKED, this::tableViewOnMouseClicked);
		tableView.setPlaceholder(new Label("서비스 에러 내역 정보가 없습니다."));
	}

	public void reloadData() {
		LocalDate selectedDate = datePicker.getValue();
//		if (selectedDate == null) {
//			selectedDate = LocalDate.now();
//		}
		
		ThreadPoll.getInstance().execute(() -> {
			List<ServiceErrorEntry> entries = dbManager.getServiceErrorEntries(selectedDate);
			Platform.runLater(() -> {
				tableView.setItems(FXCollections.observableArrayList(entries));
			});
		});		
	}

	public List<ServiceErrorEntry> getServiceErrorData() {
		return tableView.getItems();
	}

	public LocalDate getSelectedDate() {
		return datePicker.getValue();
	}

	private <T extends Event> void tableViewOnMouseClicked(MouseEvent me) {
		if (me.getClickCount() == 2 && me.getButton() == MouseButton.PRIMARY) {
			ServiceErrorEntry selectedItem = tableView.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				showDetailsPopup("서비스 에러 내역 상세", selectedItem);
			}
		}
	}

	// showDetailsPopup 메서드는 기존과 동일 (isFail 파라미터로 에러 컬럼 표시 여부 결정)
	private void showDetailsPopup(String title, ServiceErrorEntry details) {
		final String errorId = details.getErrorId();
		ThreadPoll.getInstance().execute(() -> {
			ServiceErrorLog serviceErrorLog = dbManager.getServiceErrorLog(errorId);
			final String errorLog = serviceErrorLog.getErrorLog();

			Platform.runLater(() -> {

				Dialog<Void> dialog = new Dialog<>();
				dialog.initModality(Modality.APPLICATION_MODAL);
				dialog.setTitle(title);
				dialog.setHeaderText(null);
				TextArea textArea = new TextArea(errorLog);
				textArea.setPrefWidth(800);
				textArea.setPrefHeight(650);
				textArea.setWrapText(true);

				dialog.getDialogPane().setContent(textArea);
				dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

				dialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				dialog.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
				dialog.initOwner(ServiceErrorMonitoringControl.this.getScene().getWindow());
				dialog.showAndWait();
			});
		});

	}
}
