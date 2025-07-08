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
    private final DatePicker scheduleDatePicker;
    private final TableView<ScheduleEntry> scheduleTableView;
    private final DatabaseManager dbManager;

    public ScheduleMonitoringControl() {
        super("스케줄 모니터링");
        dbManager = new DatabaseManager();

        scheduleDatePicker = new DatePicker(LocalDate.now());
        scheduleDatePicker.setOnAction(event -> reloadData());

        HBox datePickerBox = new HBox(new Label("날짜 선택: "), scheduleDatePicker);
        datePickerBox.setSpacing(5);
        datePickerBox.setAlignment(Pos.CENTER_LEFT);

        scheduleTableView = new TableView<>();
        // ... 컬럼 설정 (기존과 동일) ...

        VBox.setVgrow(scheduleTableView, Priority.ALWAYS);

        reloadData(); // 초기 데이터 로드

        this.getChildren().addAll(datePickerBox, scheduleTableView);
        VBox.setVgrow(this, Priority.ALWAYS);
        HBox.setHgrow(this, Priority.ALWAYS);
    }

    public void reloadData() {
        LocalDate selectedDate = scheduleDatePicker.getValue();
        if (selectedDate == null) return;

        List<ScheduleEntry> entries = dbManager.getScheduleEntries(selectedDate);
        scheduleTableView.setItems(FXCollections.observableArrayList(entries));
        if (entries.isEmpty()) {
            scheduleTableView.setPlaceholder(new Label("해당 날짜의 스케줄 정보가 없습니다."));
        }
    }
}
