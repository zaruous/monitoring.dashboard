package org.kyj.fx.monitoring.dashboard;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class InterfaceMonitoringDashboardApp extends Application {
    // 각 컨트롤에 대한 참조를 저장할 필드
    private OverallStatusControl overallStatusControl;
    private ScheduleMonitoringControl scheduleMonitoringControl;
    private DataFluctuationControl dataFluctuationControl;
    private DevelopmentItemControl developmentItemControl;
	@Override
	public void start(Stage primaryStage) {
        DatabaseManager dbManager = DatabaseManager.getInstance();
        dbManager.initializeDatabase();

        primaryStage.setTitle("인터페이스 모니터링 보드 (JavaFX)");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setPadding(new Insets(20));
        gridPane.setStyle("-fx-background-color: #f3f4f6;");

        // 컨트롤 초기화 및 필드에 할당
        overallStatusControl = new OverallStatusControl();
        scheduleMonitoringControl = new ScheduleMonitoringControl();
        dataFluctuationControl = new DataFluctuationControl();
        developmentItemControl = new DevelopmentItemControl();

        GridPane.setConstraints(overallStatusControl, 0, 0);
        GridPane.setConstraints(scheduleMonitoringControl, 1, 0);
        GridPane.setConstraints(dataFluctuationControl, 0, 1);
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

		// --- 헤더 수정 ---
        Label titleLabel = new Label("모니터링 보드");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#333333"));

        // 버튼을 오른쪽으로 밀기 위한 스페이서
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // 새로고침 버튼 생성
        Button btnReload = new Button("새로고침");
        btnReload.setStyle("-fx-font-size: 14px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");
        btnReload.setOnAction(ev -> reloadAllData()); // 액션 설정

        HBox titleBox = new HBox(titleLabel, spacer, btnReload);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(10, 20, 10, 20));

        BorderPane rootLayout = new BorderPane();
        rootLayout.setTop(titleBox);
        rootLayout.setCenter(gridPane);
        rootLayout.setStyle("-fx-background-color: #f3f4f6;");

        Scene scene = new Scene(rootLayout, 1200, 850);
        primaryStage.setScene(scene);
        primaryStage.show();
	}

	 /**
     * 모든 대시보드 컴포넌트의 데이터를 다시 로드합니다.
     */
    private void reloadAllData() {
        System.out.println("Reloading all dashboard data...");
        overallStatusControl.reloadData();
        scheduleMonitoringControl.reloadData();
        dataFluctuationControl.reloadData();
        developmentItemControl.reloadData();
        System.out.println("Data reloaded.");
    }
    
	public static void main(String[] args) {
		launch(args);
	}
}