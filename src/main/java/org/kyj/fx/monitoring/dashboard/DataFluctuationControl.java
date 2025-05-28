package org.kyj.fx.monitoring.dashboard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

	public DataFluctuationControl() {
		super("데이터 변동률 (테이블 로우 수)");

		fluctuationTableView = new TableView<>();
		TableColumn<TableFluctuation, String> nameCol = new TableColumn<>("테이블 명");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("tableName"));
		TableColumn<TableFluctuation, Integer> prevCol = new TableColumn<>("이전 로우 수");
		prevCol.setCellValueFactory(new PropertyValueFactory<>("previousRowCount"));
		TableColumn<TableFluctuation, Integer> currCol = new TableColumn<>("현재 로우 수");
		currCol.setCellValueFactory(new PropertyValueFactory<>("currentRowCount"));
		TableColumn<TableFluctuation, String> rateCol = new TableColumn<>("변동률");
		rateCol.setCellValueFactory(new PropertyValueFactory<>("changeRate"));

		// 변동률에 따라 글자색 변경
		rateCol.setCellFactory(column -> new TableCell<TableFluctuation, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText(null);
					setStyle("");
				} else {
					setText(item);
					if (item.startsWith("+")) {
						setTextFill(Color.GREEN);
					} else if (item.startsWith("-")) {
						setTextFill(Color.RED);
					} else {
						setTextFill(Color.BLACK);
					}
				}
			}
		});

		fluctuationTableView.getColumns().addAll(nameCol, prevCol, currCol, rateCol);
		VBox.setVgrow(fluctuationTableView, Priority.ALWAYS);

		loadFluctuationData();

		this.getChildren().add(fluctuationTableView);
		VBox.setVgrow(this, Priority.ALWAYS);
		HBox.setHgrow(this, Priority.ALWAYS);
	}

	private void loadFluctuationData() {
		ObservableList<TableFluctuation> data = FXCollections.observableArrayList(
				new TableFluctuation("TB_ORDERS", 10250, 10570), new TableFluctuation("TB_CUSTOMERS", 5120, 5135),
				new TableFluctuation("TB_PRODUCTS", 850, 845));
		fluctuationTableView.setItems(data);
	}
}
