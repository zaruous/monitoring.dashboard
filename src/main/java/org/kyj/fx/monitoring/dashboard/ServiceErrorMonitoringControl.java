package org.kyj.fx.monitoring.dashboard;

import java.time.LocalDate;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

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
        errorCodeCol.setCellValueFactory(new PropertyValueFactory<>("errorCode"));

        TableColumn<ServiceErrorEntry, String> errorMsgCol = new TableColumn<>("ERROR_MSG");
        errorMsgCol.setCellValueFactory(new PropertyValueFactory<>("errorMsg"));

        TableColumn<ServiceErrorEntry, String> errorDescCol = new TableColumn<>("ERROR_DESC");
        errorDescCol.setCellValueFactory(new PropertyValueFactory<>("errorDesc"));

        TableColumn<ServiceErrorEntry, Integer> countCol = new TableColumn<>("COUNT");
        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));

        TableColumn<ServiceErrorEntry, LocalDate> regDateCol = new TableColumn<>("발생일");
        regDateCol.setCellValueFactory(new PropertyValueFactory<>("regDate"));

        tableView.getColumns().addAll(errorCodeCol, errorMsgCol, errorDescCol, countCol, regDateCol);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        reloadData(); // 초기 데이터 로드

        this.getChildren().addAll(filterBox, tableView);
        VBox.setVgrow(this, Priority.ALWAYS);
        HBox.setHgrow(this, Priority.ALWAYS);
    }

    public void reloadData() {
		LocalDate selectedDate = datePicker.getValue();
		if (selectedDate == null) {
			selectedDate = LocalDate.now();
		}
		List<ServiceErrorEntry> entries = dbManager.getServiceErrorEntries(selectedDate);
         tableView.setItems(FXCollections.observableArrayList(entries));
        
        // 현재는 플레이스홀더만 설정
        tableView.setPlaceholder(new Label("서비스 에러 내역 정보가 없습니다."));
    }

    public List<ServiceErrorEntry> getServiceErrorData() {
        return tableView.getItems();
    }
}
