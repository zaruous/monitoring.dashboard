package org.kyj.fx.monitoring.dashboard;

import java.time.LocalDate;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ScheduleMonitoringControl extends CardControl {
	private DatePicker scheduleDatePicker;
	private TableView<ScheduleEntry> scheduleTableView;
	private DatabaseManager dbManager;

	public ScheduleMonitoringControl() {
		super("스케줄 모니터링");
		dbManager = new DatabaseManager();

		scheduleDatePicker = new DatePicker(LocalDate.now());
		scheduleDatePicker.setOnAction(event -> loadScheduleData(scheduleDatePicker.getValue()));

		HBox datePickerBox = new HBox(new Label("날짜 선택: "), scheduleDatePicker);
		datePickerBox.setSpacing(5);
		datePickerBox.setAlignment(Pos.CENTER_LEFT);

		scheduleTableView = new TableView<>();
		// 컬럼 설정 (기존과 동일)
		TableColumn<ScheduleEntry, String> idCol = new TableColumn<>("스케줄 ID");
		idCol.setCellValueFactory(new PropertyValueFactory<>("scheduleId"));
		TableColumn<ScheduleEntry, String> nameCol = new TableColumn<>("인터페이스 명");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("interfaceName"));
		TableColumn<ScheduleEntry, String> statusCol = new TableColumn<>("상태");
		statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
		statusCol.setCellFactory(column -> new TableCell<>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText(null); setStyle("");
				} else {
					setText(item);
					if (item.equals("성공")) setTextFill(Color.GREEN);
					else if (item.equals("실패")) setTextFill(Color.RED);
					else setTextFill(Color.BLACK);
				}
			}
		});
		TableColumn<ScheduleEntry, String> timeCol = new TableColumn<>("실행 시간 (소요 시간)");
		timeCol.setCellValueFactory(new PropertyValueFactory<>("executionTimeDisplay"));

		scheduleTableView.getColumns().addAll(idCol, nameCol, statusCol, timeCol);
		VBox.setVgrow(scheduleTableView, Priority.ALWAYS);

		loadScheduleData(LocalDate.now()); // 초기 데이터 로드

		this.getChildren().addAll(datePickerBox, scheduleTableView);
		VBox.setVgrow(this, Priority.ALWAYS);
		HBox.setHgrow(this, Priority.ALWAYS);
	}

	private void loadScheduleData(LocalDate date) {
		List<ScheduleEntry> entries = dbManager.getScheduleEntries(date);
		scheduleTableView.setItems(FXCollections.observableArrayList(entries));
		if (entries.isEmpty()) {
			scheduleTableView.setPlaceholder(new Label("해당 날짜의 스케줄 정보가 없습니다."));
		}
	}
}