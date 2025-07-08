package org.kyj.fx.monitoring.dashboard;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class DevelopmentItemControl extends CardControl {

    private final Label kpiTotalValueLabel;
    private final Label kpiInProgressValueLabel;
    private final Label kpiCompletedValueLabel;
    private final ProgressIndicatorControl devProgressBar;
//    private final GoogleSheetsManager sheetsManager;

    // --- 설정이 필요한 부분 ---
    // 여기에 데이터를 가져올 구글 시트의 ID를 입력하세요.
    private static final String SPREADSHEET_ID = "YOUR_SPREADSHEET_ID_HERE";
    // 전체 항목(N8), 완료 건(P8)이 포함된 시트 이름과 범위를 입력하세요.
    private static final String SPREADSHEET_RANGE = "Sheet1!N8:P8";


    public DevelopmentItemControl() {
        super("개발 아이템 정보");
//        this.sheetsManager = new GoogleSheetsManager();
        this.setSpacing(15);

        GridPane kpiGrid = new GridPane();
        kpiGrid.setHgap(10);
        kpiGrid.setVgap(10);

        kpiTotalValueLabel = new Label("-");
        kpiInProgressValueLabel = new Label("-");
        kpiCompletedValueLabel = new Label("-");

        Node totalCard = createKpiCard(kpiTotalValueLabel, "전체", Color.web("#A5B4FC"), Color.web("#312E81"));
        Node inProgressCard = createKpiCard(kpiInProgressValueLabel, "진행중", Color.web("#FCD34D"), Color.web("#78350F"));
        Node completedCard = createKpiCard(kpiCompletedValueLabel, "완료", Color.web("#A7F3D0"), Color.web("#065F46"));

        kpiGrid.add(totalCard, 0, 0);
        kpiGrid.add(inProgressCard, 1, 0);
        kpiGrid.add(completedCard, 2, 0);

        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(100.0 / 3);
        kpiGrid.getColumnConstraints().addAll(cc, cc, cc);

        Label progressTitleLabel = new Label("진척률");
        progressTitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

        devProgressBar = new ProgressIndicatorControl();
        HBox progressBarBox = new HBox(devProgressBar);
        progressBarBox.setSpacing(5);
        progressBarBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(devProgressBar, Priority.ALWAYS);

        this.getChildren().addAll(kpiGrid, progressTitleLabel, progressBarBox);
        VBox.setVgrow(this, Priority.ALWAYS);
        HBox.setHgrow(this, Priority.ALWAYS);

        reloadData();
    }

    public void reloadData() {
        // 백그라운드 스레드에서 네트워크 작업 수행
        new Thread(() -> {
            try {
//                List<List<Object>> values = sheetsManager.getSheetData(SPREADSHEET_ID, SPREADSHEET_RANGE);

//                if (values == null || values.isEmpty()) {
//                    showError("시트에서 데이터를 가져올 수 없습니다.");
//                    return;
//                }
                
                // N8(0,0), O8(0,1), P8(0,2)
//                List<Object> row = values.get(0);
                int total = 100; //Integer.parseInt(row.get(0).toString()); // N8
                int completed = 50; //Integer.parseInt(row.get(2).toString()); // P8
                int inProgress = total - completed;

                // UI 업데이트는 JavaFX Application Thread에서 수행
                Platform.runLater(() -> {
                    kpiTotalValueLabel.setText(String.valueOf(total));
                    kpiInProgressValueLabel.setText(String.valueOf(inProgress));
                    kpiCompletedValueLabel.setText(String.valueOf(completed));

                    devProgressBar.setTotalItems(total);
                    devProgressBar.setCompletedItems(completed);
                });

            } catch (Exception e) {
                e.printStackTrace();
                showError("데이터 로딩 중 오류 발생: " + e.getMessage());
            }
        }).start();
    }

    private void showError(String message) {
        Platform.runLater(() -> {
            kpiTotalValueLabel.setText("오류");
            kpiInProgressValueLabel.setText("-");
            kpiCompletedValueLabel.setText("-");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("오류");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
    
    // createKpiCard, toWebColor 메서드는 기존과 동일
    private Node createKpiCard(Label valueLabel, String description, Color bgColor, Color textColor) {
		VBox card = new VBox(5);
		card.setAlignment(Pos.CENTER);
		card.setPadding(new Insets(10));
		String style = String.format("-fx-background-color: %s; -fx-background-radius: 8px;", toWebColor(bgColor));
		card.setStyle(style);

		valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		valueLabel.setTextFill(textColor);

		Label descLabel = new Label(description);
		descLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
		descLabel.setTextFill(textColor.deriveColor(0, 1, 0.7, 1));

		card.getChildren().addAll(descLabel, valueLabel);
		return card;
	}

	private String toWebColor(Color color) {
		return String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255),
				(int) (color.getBlue() * 255));
	}
}
