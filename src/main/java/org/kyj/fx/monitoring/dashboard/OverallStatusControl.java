/**
 * 
 */
package org.kyj.fx.monitoring.dashboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
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

/**
 * 
 */
public class OverallStatusControl extends CardControl {
	private PieChart pieChart;
	private Label totalCountsLabel;
	private List<InterfaceStatusDetail> successDetails;
	private List<InterfaceStatusDetail> failDetails;

	public OverallStatusControl() {
		super("전체 인터페이스 현황");

		// 더미 데이터 초기화
		initializeDummyData();

		pieChart = new PieChart();
		PieChart.Data successSlice = new PieChart.Data("성공", successDetails.size());
		PieChart.Data failSlice = new PieChart.Data("실패", failDetails.size());
		pieChart.getData().addAll(successSlice, failSlice);
		pieChart.setLegendVisible(true);
		// pieChart.setLabelsVisible(false); // 파이 조각에 직접 레이블 표시 안함

		// 파이 차트 조각 색상 설정
		// CSS로 하는 것이 더 유연하지만, 코드로 간단히 설정
		successSlice.getNode().setStyle("-fx-pie-color: #4CAF50;"); // 녹색 계열
		failSlice.getNode().setStyle("-fx-pie-color: #F44336;"); // 빨간색 계열

		// 파이 차트 클릭 이벤트
		for (final PieChart.Data data : pieChart.getData()) {
			data.getNode().addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
				String statusType = data.getName();
				List<InterfaceStatusDetail> detailsToShow;
				String modalTitle;
				if (statusType.equals("성공")) {
					detailsToShow = successDetails;
					modalTitle = "성공 인터페이스 상세 정보";
				} else {
					detailsToShow = failDetails;
					modalTitle = "실패 인터페이스 상세 정보";
				}
				showDetailsPopup(modalTitle, detailsToShow, statusType.equals("실패"));
			});
		}

		// VBox.setVgrow(pieChart, Priority.ALWAYS); // 파이차트가 가능한 많은 공간을 차지하도록
		pieChart.setMaxHeight(300); // 높이 제한
		pieChart.setPrefHeight(300);

		totalCountsLabel = new Label();
		updateTotalCounts();
		totalCountsLabel.setAlignment(Pos.CENTER);
		totalCountsLabel.setPadding(new Insets(5, 0, 0, 0));

		this.getChildren().addAll(pieChart, totalCountsLabel);
		// VBox가 부모 컨테이너에 맞게 크기 조절되도록 설정
		VBox.setVgrow(this, Priority.ALWAYS);
		HBox.setHgrow(this, Priority.ALWAYS);
	}

	private void initializeDummyData() {
		successDetails = new ArrayList<>(
				Arrays.asList(new InterfaceStatusDetail("IF-001", "주문 접수", "2024-05-28 10:00:00", "120ms", "API_GW_01"),
						new InterfaceStatusDetail("IF-003", "상품 조회", "2024-05-28 10:02:00", "80ms", "PRODUCT_SVC_02")));
		failDetails = new ArrayList<>(Arrays.asList(
				new InterfaceStatusDetail("IF-002", "고객 인증", "2024-05-28 10:05:00", "500ms", "AUTH_SVC_01", "E401",
						"인증 토큰 만료"),
				new InterfaceStatusDetail("IF-004", "재고 확인", "2024-05-28 10:15:00", "1200ms", "INVENTORY_SVC_01",
						"E503", "재고 서비스 응답 없음")));
	}

	private void updateTotalCounts() {
		int successCount = successDetails.size();
		int failCount = failDetails.size();
		int total = successCount + failCount;
		totalCountsLabel.setText(String.format("총 호출: %d건 (성공: %d건, 실패: %d건)", total, successCount, failCount));
	}

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

		// Dialog 크기 자동 조절
		dialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		dialog.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);

		dialog.showAndWait();
	}
}
