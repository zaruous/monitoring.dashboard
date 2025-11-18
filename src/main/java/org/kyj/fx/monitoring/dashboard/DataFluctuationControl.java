package org.kyj.fx.monitoring.dashboard;

import java.time.LocalDate;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class DataFluctuationControl extends CardControl {
	private TableView<TableFluctuation> fluctuationTableView;
    private DatabaseManager dbManager;
    
	public DataFluctuationControl() {
		super("데이터 변동률 (테이블 로우 수)");
        dbManager = DatabaseManager.getInstance();

		fluctuationTableView = new TableView<>();
		// 컬럼 설정 (기존과 동일)
		TableColumn<TableFluctuation, String> nameCol = new TableColumn<>("테이블 명");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("tableName"));
		TableColumn<TableFluctuation, Integer> prevCol = new TableColumn<>("이전 로우 수");
		prevCol.setCellValueFactory(new PropertyValueFactory<>("previousRowCount"));
		TableColumn<TableFluctuation, Integer> currCol = new TableColumn<>("현재 로우 수");
		currCol.setCellValueFactory(new PropertyValueFactory<>("currentRowCount"));
		TableColumn<TableFluctuation, String> rateCol = new TableColumn<>("변동률");
		rateCol.setCellValueFactory(new PropertyValueFactory<>("changeRate"));
		rateCol.setCellFactory(column -> new TableCell<>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText(null); setStyle("");
				} else {
					setText(item);
					if (item.startsWith("+")) setTextFill(Color.GREEN);
					else if (item.startsWith("-")) setTextFill(Color.RED);
					else setTextFill(Color.BLACK);
				}
			}
		});

		fluctuationTableView.getColumns().addAll(nameCol, prevCol, currCol, rateCol);
		VBox.setVgrow(fluctuationTableView, Priority.ALWAYS);

		reloadData(); // 초기 데이터 로드

        this.getChildren().add(fluctuationTableView);
        VBox.setVgrow(this, Priority.ALWAYS);
        HBox.setHgrow(this, Priority.ALWAYS);
	}

	public void reloadData() {
        fluctuationTableView.setItems(FXCollections.observableArrayList(dbManager.getTableFluctuations()));
    }
	
	
	public List<TableFluctuation> getFluctuationData() {
        return fluctuationTableView.getItems();
    }
}