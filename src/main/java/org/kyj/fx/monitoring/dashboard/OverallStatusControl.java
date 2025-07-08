package org.kyj.fx.monitoring.dashboard;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

public class OverallStatusControl extends CardControl {
	private PieChart pieChart;
	private Label totalCountsLabel;
	private List<InterfaceStatusDetail> successDetails;
	private List<InterfaceStatusDetail> failDetails;
    private List<InterfaceStatusDetail> inProgressDetails; // 진행중 리스트 추가

	private DatabaseManager dbManager;
	private OverallStatusController controller;

	public OverallStatusControl() {
		super("전체 인터페이스 현황");
		dbManager = new DatabaseManager();
		controller = new OverallStatusController();
		
		// 데이터베이스에서 데이터 로드
		loadDataFromDB();

		pieChart = new PieChart();
		PieChart.Data successSlice = new PieChart.Data("성공", successDetails.size());
		PieChart.Data failSlice = new PieChart.Data("실패", failDetails.size());
        PieChart.Data inProgressSlice = new PieChart.Data("진행중", inProgressDetails.size()); // 진행중 슬라이스 추가

		pieChart.getData().addAll(successSlice, failSlice, inProgressSlice);
		pieChart.setLegendVisible(true);

		// 파이 차트 조각 색상 설정
		successSlice.getNode().setStyle("-fx-pie-color: #4CAF50;"); // 녹색
		failSlice.getNode().setStyle("-fx-pie-color: #F44336;");   // 빨간색
        inProgressSlice.getNode().setStyle("-fx-pie-color: #FFC107;"); // 노란색

		// 파이 차트 클릭 이벤트
		for (final PieChart.Data data : pieChart.getData()) {
			data.getNode().addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
				String statusType = data.getName();
				List<InterfaceStatusDetail> detailsToShow;
				String modalTitle;
				boolean isFail = false;

				switch (statusType) {
					case "성공":
						detailsToShow = successDetails;
						modalTitle = "성공 인터페이스 상세 정보";
						break;
					case "실패":
						detailsToShow = failDetails;
						modalTitle = "실패 인터페이스 상세 정보";
						isFail = true;
						break;
                    case "진행중":
                        detailsToShow = inProgressDetails;
                        modalTitle = "진행중 인터페이스 상세 정보";
                        break;
					default:
						return;
				}
				showDetailsPopup(modalTitle, detailsToShow, isFail);
			});
		}

		pieChart.setMaxHeight(300);
		pieChart.setPrefHeight(300);

		totalCountsLabel = new Label();
		updateTotalCounts();
		totalCountsLabel.setAlignment(Pos.CENTER);
		totalCountsLabel.setPadding(new Insets(5, 0, 0, 0));

		Button btnReload = new Button("재조회");
		btnReload.setOnAction(ev ->{
			controller.reloadData();
		});
		this.getChildren().addAll(pieChart, totalCountsLabel, new HBox(btnReload));
		VBox.setVgrow(this, Priority.ALWAYS);
		HBox.setHgrow(this, Priority.ALWAYS);
	}

	private void loadDataFromDB() {
		successDetails = dbManager.getInterfaceStatusDetails("성공");
		failDetails = dbManager.getInterfaceStatusDetails("실패");
        inProgressDetails = dbManager.getInterfaceStatusDetails("진행중");
	}

	private void updateTotalCounts() {
		int successCount = successDetails.size();
		int failCount = failDetails.size();
        int inProgressCount = inProgressDetails.size();
		int total = successCount + failCount + inProgressCount;
		totalCountsLabel.setText(String.format("총 호출: %d건 (성공: %d, 실패: %d, 진행중: %d)", total, successCount, failCount, inProgressCount));
	}
    
    // showDetailsPopup 메서드는 기존과 동일 (isFail 파라미터로 에러 컬럼 표시 여부 결정)
	private void showDetailsPopup(String title, List<InterfaceStatusDetail> details, boolean isFail) {
		Dialog<Void> dialog = new Dialog<>();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.setTitle(title);
		dialog.setHeaderText(null);

		TableView<InterfaceStatusDetail> tableView = new TableView<>();
		TableColumn<InterfaceStatusDetail, String> idCol = new TableColumn<>("ID");
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		TableColumn<InterfaceStatusDetail, String> nameCol = new TableColumn<>("이름");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumn<InterfaceStatusDetail, String> timeCol = new TableColumn<>("시간");
		timeCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
		TableColumn<InterfaceStatusDetail, String> durationCol = new TableColumn<>("소요시간");
		durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
		TableColumn<InterfaceStatusDetail, String> serverCol = new TableColumn<>("서버");
		serverCol.setCellValueFactory(new PropertyValueFactory<>("server"));

		tableView.getColumns().addAll(idCol, nameCol, timeCol, durationCol, serverCol);

		if (isFail) {
			TableColumn<InterfaceStatusDetail, String> errCodeCol = new TableColumn<>("에러코드");
			errCodeCol.setCellValueFactory(new PropertyValueFactory<>("errorCode"));
			TableColumn<InterfaceStatusDetail, String> errMsgCol = new TableColumn<>("에러메시지");
			errMsgCol.setCellValueFactory(new PropertyValueFactory<>("errorMessage"));
			tableView.getColumns().addAll(errCodeCol, errMsgCol);
		}

		tableView.setItems(FXCollections.observableArrayList(details));
		tableView.setPrefHeight(250);
		tableView.setPrefWidth(650); // 테이블 너비 조정

		dialog.getDialogPane().setContent(tableView);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

		dialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		dialog.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);

		dialog.showAndWait();
	}
}