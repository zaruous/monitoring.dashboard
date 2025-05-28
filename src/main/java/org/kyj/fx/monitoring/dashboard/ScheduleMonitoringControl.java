package org.kyj.fx.monitoring.dashboard;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ScheduleMonitoringControl extends CardControl {
	private DatePicker scheduleDatePicker;
	private TableView<ScheduleEntry> scheduleTableView;
	private Map<String, List<ScheduleEntry>> scheduleData; // 날짜별 스케줄 데이터

	public ScheduleMonitoringControl() {
		super("스케줄 모니터링");

		initializeDummyData();

		scheduleDatePicker = new DatePicker(LocalDate.now()); // 오늘 날짜 기본 선택
		scheduleDatePicker.setOnAction(event -> loadScheduleData(scheduleDatePicker.getValue()));

		HBox datePickerBox = new HBox(new Label("날짜 선택: "), scheduleDatePicker);
		datePickerBox.setSpacing(5);
		datePickerBox.setAlignment(Pos.CENTER_LEFT);

		scheduleTableView = new TableView<>();
		TableColumn<ScheduleEntry, String> idCol = new TableColumn<>("스케줄 ID");
		idCol.setCellValueFactory(new PropertyValueFactory<>("scheduleId"));
		TableColumn<ScheduleEntry, String> nameCol = new TableColumn<>("인터페이스 명");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("interfaceName"));
		TableColumn<ScheduleEntry, String> statusCol = new TableColumn<>("상태");
		statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
		// 상태에 따라 글자색 변경
		statusCol.setCellFactory(column -> new TableCell<ScheduleEntry, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText(null);
					setStyle("");
				} else {
					setText(item);
					if (item.equals("성공")) {
						setTextFill(Color.GREEN);
					} else if (item.equals("실패")) {
						setTextFill(Color.RED);
					} else {
						setTextFill(Color.BLACK);
					}
				}
			}
		});

		TableColumn<ScheduleEntry, String> timeCol = new TableColumn<>("실행 시간 (소요 시간)");
		timeCol.setCellValueFactory(new PropertyValueFactory<>("executionTimeDisplay"));

		scheduleTableView.getColumns().addAll(idCol, nameCol, statusCol, timeCol);
		VBox.setVgrow(scheduleTableView, Priority.ALWAYS); // 테이블이 가능한 많은 공간을 차지하도록

		loadScheduleData(LocalDate.now()); // 초기 데이터 로드

		this.getChildren().addAll(datePickerBox, scheduleTableView);
		VBox.setVgrow(this, Priority.ALWAYS);
		HBox.setHgrow(this, Priority.ALWAYS);
	}

	private void initializeDummyData() {
		scheduleData = Map.of("2024-05-28",
				Arrays.asList(new ScheduleEntry("SCH-001", "일일 판매 집계", "성공", "2024-05-28 02:00:15", "5분 10초"),
						new ScheduleEntry("SCH-002", "데이터 백업", "성공", "2024-05-28 03:00:05", "12분 30초")),
				"2024-05-27",
				Arrays.asList(new ScheduleEntry("SCH-001", "일일 판매 집계", "성공", "2024-05-27 02:00:20", "5분 05초"),
						new ScheduleEntry("SCH-003", "사용자 활동 로그 분석", "실패", "2024-05-27 04:00:00", "2분 (오류 발생)")),
				"2024-05-26",
				Arrays.asList(new ScheduleEntry("SCH-005", "외부 API 데이터 수집", "성공", "2024-05-26 01:00:00", "8분 00초")));
	}

	private void loadScheduleData(LocalDate date) {
		String dateString = date.format(DateTimeFormatter.ISO_DATE);
		List<ScheduleEntry> entries = scheduleData.getOrDefault(dateString, new ArrayList<>());
		scheduleTableView.setItems(FXCollections.observableArrayList(entries));
		if (entries.isEmpty()) {
			scheduleTableView.setPlaceholder(new Label("해당 날짜의 스케줄 정보가 없습니다."));
		}
	}
}
