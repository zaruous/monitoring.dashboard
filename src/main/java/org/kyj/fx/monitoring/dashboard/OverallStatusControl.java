package org.kyj.fx.monitoring.dashboard;

import java.time.LocalDate;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
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
	private List<InterfaceStatusDetail> inProgressDetails;
	private List<InterfaceStatusDetail> retrayDetails;
	private final DatabaseManager dbManager;
	DatePicker datePicker;

	public OverallStatusControl() {
		super("전체 인터페이스 현황");
		dbManager = DatabaseManager.getInstance();
		setupUI();
		reloadData(); // 초기 데이터 로드
	}

	private void setupUI() {
		pieChart = new PieChart();
		pieChart.setLegendVisible(true);
		pieChart.setMaxHeight(300);
		pieChart.setPrefHeight(300);

		totalCountsLabel = new Label();
		totalCountsLabel.setAlignment(Pos.CENTER);
		totalCountsLabel.setPadding(new Insets(5, 0, 0, 0));

		datePicker = new DatePicker(LocalDate.now());
		this.getChildren().addAll(new HBox(new Label("날짜 선택 : "), datePicker), pieChart, totalCountsLabel);
		VBox.setVgrow(this, Priority.ALWAYS);
		HBox.setHgrow(this, Priority.ALWAYS);
	}

	/**
	 * PieChart의 각 범례 항목에 클릭 이벤트를 추가합니다.
	 * 
	 * @param chart 이벤트를 추가할 PieChart 객체
	 */
	private void addLegendClickListener(PieChart chart) {
	    // Scene에 추가된 후 범례가 렌더링될 때까지 대기
	    chart.lookupAll(".chart-legend-item").forEach(node -> {
	        // 각 범례 항목(심볼+레이블을 포함하는 컨테이너)에 대해 처리
	        Label legendItem = (Label) node;
	        Node r = (Region) legendItem.getGraphic();
            String legendText = legendItem.getText();
            legendItem.setCursor(Cursor.HAND);
            
            chart.getData().stream()
            .filter(data -> data.getName().equals(legendText))
            .findFirst()
            .ifPresent(data -> {
                // 클릭 이벤트 추가
            	 legendItem.setOnMouseClicked(event -> {
//                     toggleDataVisibility(data, legendItem);
            		 handleSliceClick(data);
                 });
            });
	    });
	}

	/**
	 * 데이터 항목의 가시성을 토글합니다.
	 */
	private void toggleDataVisibility(PieChart.Data data, Label legendLabel) {
	    Node node = data.getNode();
	    boolean isVisible = node.isVisible();
	    
	    node.setVisible(!isVisible);
	    
	    // 범례 스타일 업데이트
	    if (isVisible) {
	        legendLabel.setStyle("-fx-opacity: 0.5;");
	        // 심볼도 함께 변경
	        Node symbol = legendLabel.getParent().lookup(".chart-legend-item-symbol");
	        if (symbol != null) {
	            symbol.setStyle("-fx-opacity: 0.5;");
	        }
	    } else {
	        legendLabel.setStyle("");
	        Node symbol = legendLabel.getParent().lookup(".chart-legend-item-symbol");
	        if (symbol != null) {
	            symbol.setStyle("");
	        }
	    }
	}

	public void reloadData() {
    	
    	
        // 1. DB에서 새로운 데이터 가져오기
    	LocalDate date = datePicker.getValue();
    	
    	ThreadPoll.getInstance().execute(()->{
    		successDetails = dbManager.getInterfaceStatusDetails(date, INF_STATUS.SUCCESS);
            failDetails = dbManager.getInterfaceStatusDetails(date, INF_STATUS.FAIL);
            inProgressDetails = dbManager.getInterfaceStatusDetails(date, INF_STATUS.IN_PROGRESS);
            retrayDetails = dbManager.getInterfaceStatusDetails(date, INF_STATUS.RETRY);
        
            PieChart.Data successSlice = new PieChart.Data("성공", successDetails.size());
            PieChart.Data failSlice = new PieChart.Data("실패", failDetails.size());
            PieChart.Data inProgressSlice = new PieChart.Data("진행중", inProgressDetails.size());
            PieChart.Data retraySlice = new PieChart.Data("재전송", retrayDetails.size());
            
            Platform.runLater(()->{
                // 2. 파이 차트 데이터 업데이트
                pieChart.getData().setAll(successSlice, failSlice, inProgressSlice, retraySlice);

                // 범례 클릭 리스너 추가
                // 3. 스타일 및 이벤트 핸들러 다시 적용
                applySliceStylesAndEvents();

                // 4. 요약 레이블 업데이트
                updateTotalCounts();
                
                Platform.runLater(() -> {
					addLegendClickListener(pieChart);
                });
                
            });     
    	});
    	
        
       
    }

	private void applySliceStylesAndEvents() {
		for (final PieChart.Data data : pieChart.getData()) {
			// 이벤트 핸들러 추가
			data.getNode().setOnMouseClicked(e -> {
				handleSliceClick(data);
			});
		}
	}

	private void handleSliceClick(PieChart.Data data) {
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
	}

	private void updateTotalCounts() {
		int successCount = successDetails.size();
		int failCount = failDetails.size();
		int inProgressCount = inProgressDetails.size();
		int retrayCount = retrayDetails.size();
		int total = successCount + failCount + inProgressCount;
		totalCountsLabel.setText(String.format("총 호출: %d건 (성공: %d, 실패: %d, 진행중: %d, 재전송: %d)", total, successCount,
				failCount, inProgressCount, retrayCount));
	}

	public String getSummaryText() {
		return totalCountsLabel.getText();
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
	
	public LocalDate getSelectedDate() {
		return datePicker.getValue();
	}
	
}