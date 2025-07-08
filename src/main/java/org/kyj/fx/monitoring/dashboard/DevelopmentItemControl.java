package org.kyj.fx.monitoring.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class DevelopmentItemControl extends CardControl {

	private Label kpiTotalValueLabel;
	private Label kpiInProgressValueLabel;
	private Label kpiCompletedValueLabel;
	private ProgressIndicatorControl devProgressBar;
    private DatabaseManager dbManager;

	public DevelopmentItemControl() {
		super("개발 아이템 정보");
        dbManager = new DatabaseManager();
		this.setSpacing(15);

		GridPane kpiGrid = new GridPane();
		kpiGrid.setHgap(10);
		kpiGrid.setVgap(10);

		kpiTotalValueLabel = new Label("0");
		kpiInProgressValueLabel = new Label("0");
		kpiCompletedValueLabel = new Label("0");

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

		loadDevelopmentData(); 

		this.getChildren().addAll(kpiGrid, progressTitleLabel, progressBarBox);
		VBox.setVgrow(this, Priority.ALWAYS);
		HBox.setHgrow(this, Priority.ALWAYS);
	}

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

	private void loadDevelopmentData() {
		int total = dbManager.getDevelopmentItemCount("total");
		int inProgress = dbManager.getDevelopmentItemCount("inProgress");
		int completed = dbManager.getDevelopmentItemCount("completed");

		kpiTotalValueLabel.setText(String.valueOf(total));
		kpiInProgressValueLabel.setText(String.valueOf(inProgress));
		kpiCompletedValueLabel.setText(String.valueOf(completed));
        
		devProgressBar.setTotalItems(total);
		devProgressBar.setCompletedItems(completed);
	}
}