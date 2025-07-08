package org.kyj.fx.monitoring.dashboard;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class InterfaceMonitoringDashboardApp extends Application {

	@Override
	public void start(Stage primaryStage) {
        // 데이터베이스 초기화
        DatabaseManager dbManager = new DatabaseManager();
        dbManager.initializeDatabase();
        
		primaryStage.setTitle("인터페이스 모니터링 보드 (JavaFX)");

		// 전체 레이아웃을 위한 GridPane
		GridPane gridPane = new GridPane();
		gridPane.setHgap(20); // 가로 간격
		gridPane.setVgap(20); // 세로 간격
		gridPane.setPadding(new Insets(20));
		gridPane.setStyle("-fx-background-color: #f3f4f6;");

		// 1. 전체 인터페이스 현황 컨트롤
		OverallStatusControl overallStatusControl = new OverallStatusControl();
		GridPane.setConstraints(overallStatusControl, 0, 0);

		// 2. 스케줄 모니터링 컨트롤
		ScheduleMonitoringControl scheduleMonitoringControl = new ScheduleMonitoringControl();
		GridPane.setConstraints(scheduleMonitoringControl, 1, 0);

		// 3. 데이터 변동률 컨트롤
		DataFluctuationControl dataFluctuationControl = new DataFluctuationControl();
		GridPane.setConstraints(dataFluctuationControl, 0, 1);

		// 4. 개발 아이템 정보 컨트롤
		DevelopmentItemControl developmentItemControl = new DevelopmentItemControl();
		GridPane.setConstraints(developmentItemControl, 1, 1);

		gridPane.getChildren().addAll(overallStatusControl, scheduleMonitoringControl, dataFluctuationControl,
				developmentItemControl);

		// 컬럼 및 로우 제약 조건 설정 (균등하게 공간 분배)
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(50);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(50);
		gridPane.getColumnConstraints().addAll(col1, col2);

		RowConstraints row1 = new RowConstraints();
		row1.setPercentHeight(50);
		RowConstraints row2 = new RowConstraints();
		row2.setPercentHeight(50);
		gridPane.getRowConstraints().addAll(row1, row2);

		// 헤더 추가
		Label titleLabel = new Label("모니터링 보드");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		titleLabel.setTextFill(Color.web("#333333"));
		HBox titleBox = new HBox(titleLabel);
		titleBox.setAlignment(Pos.CENTER);
		titleBox.setPadding(new Insets(10, 0, 10, 0));

		BorderPane rootLayout = new BorderPane();
		rootLayout.setTop(titleBox);
		rootLayout.setCenter(gridPane);
		rootLayout.setStyle("-fx-background-color: #f3f4f6;");

		Scene scene = new Scene(rootLayout, 1200, 850);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}