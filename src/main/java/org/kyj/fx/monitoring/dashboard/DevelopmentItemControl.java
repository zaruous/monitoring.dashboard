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

	// KPI 값을 표시할 Label 멤버 변수들
	private Label kpiTotalValueLabel;
	private Label kpiInProgressValueLabel;
	private Label kpiCompletedValueLabel;

	private ProgressIndicatorControl devProgressBar;
//	private ProgressBar devProgressBar;
//	private Label progressTextLabel;
	// GridPane kpiGrid; // GridPane을 멤버로 둘 필요는 없어짐

	public DevelopmentItemControl() {
		super("개발 아이템 정보");
		this.setSpacing(15); // 내부 요소 간 간격 조정

		// KPI 표시를 위한 GridPane
		GridPane kpiGrid = new GridPane();
		kpiGrid.setHgap(10);
		kpiGrid.setVgap(10);

		// 멤버 Label 초기화
		kpiTotalValueLabel = new Label("0");
		kpiInProgressValueLabel = new Label("0");
		kpiCompletedValueLabel = new Label("0");

		// 각 KPI 카드를 VBox에 담아 스타일 적용
		// createKpiCard 메서드가 Label을 받도록 수정됨
		Node totalCard = createKpiCard(kpiTotalValueLabel, "전체", Color.web("#A5B4FC"), Color.web("#312E81")); // bg-blue-100,
																												// text-blue-900
		Node inProgressCard = createKpiCard(kpiInProgressValueLabel, "진행중", Color.web("#FCD34D"), Color.web("#78350F")); // bg-yellow-100,
																															// text-yellow-900
		Node completedCard = createKpiCard(kpiCompletedValueLabel, "완료", Color.web("#A7F3D0"), Color.web("#065F46")); // bg-green-100,
																														// text-green-900

		kpiGrid.add(totalCard, 0, 0);
		kpiGrid.add(inProgressCard, 1, 0);
		kpiGrid.add(completedCard, 2, 0);

		// GridPane의 컬럼들이 동일한 너비를 갖도록 설정
		ColumnConstraints cc = new ColumnConstraints();
		cc.setPercentWidth(100.0 / 3); // 3개의 컬럼
		kpiGrid.getColumnConstraints().addAll(cc, cc, cc);

		// 진척률
		Label progressTitleLabel = new Label("진척률");
		progressTitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

//		devProgressBar = new ProgressBar(0);
//		devProgressBar.setMaxWidth(Double.MAX_VALUE); // 프로그레스바 너비 확장
//		devProgressBar.setStyle("-fx-accent: #3b82f6;"); // 파란색 (HTML의 progress-bar)

//		progressTextLabel = new Label("0%");
//		progressTextLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));

		
		devProgressBar = new ProgressIndicatorControl();
		devProgressBar.setTotalItems(0); // 초기 전체 값 설정
		devProgressBar.setCompletedItems(0); // 초기 완료 값 설정
		 
		HBox progressBarBox = new HBox(devProgressBar);
		progressBarBox.setSpacing(5);
		progressBarBox.setAlignment(Pos.CENTER_LEFT);
		HBox.setHgrow(devProgressBar, Priority.ALWAYS);

		
        
		loadDevelopmentData(); // 데이터 로드 (이제 Label 텍스트만 업데이트)

		this.getChildren().addAll(kpiGrid, progressTitleLabel, progressBarBox);
		VBox.setVgrow(this, Priority.ALWAYS);
		HBox.setHgrow(this, Priority.ALWAYS);
	}

	// createKpiCard는 이제 Label 객체를 파라미터로 받음
	private Node createKpiCard(Label valueLabel, String description, Color bgColor, Color textColor) {
		VBox card = new VBox(5);
		card.setAlignment(Pos.CENTER);
		card.setPadding(new Insets(10));
		String style = String.format("-fx-background-color: %s; -fx-background-radius: 8px;", toWebColor(bgColor));
		card.setStyle(style);

		// 전달받은 Label에 스타일 적용
		valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		valueLabel.setTextFill(textColor);

		Label descLabel = new Label(description);
		descLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
		// descLabel의 텍스트 색상은 textColor에서 약간 조정 (예: 더 어둡게 또는 밝게)
		descLabel.setTextFill(textColor.deriveColor(0, 1, 0.7, 1)); // 예시: 채도를 약간 낮춤

		card.getChildren().addAll(descLabel, valueLabel); // 설명 레이블을 먼저 추가
		return card;
	}

	// JavaFX Color를 웹 색상 문자열로 변환 (투명도 제외)
	private String toWebColor(Color color) {
		return String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255),
				(int) (color.getBlue() * 255));
	}

	// createKpiLabel 메서드는 더 이상 필요 없음

	private void loadDevelopmentData() {
		int total = 25;
		int inProgress = 7;
		int completed = 15;

		// 멤버 Label들의 텍스트를 직접 업데이트
		kpiTotalValueLabel.setText(String.valueOf(total));
		kpiInProgressValueLabel.setText(String.valueOf(inProgress));
		kpiCompletedValueLabel.setText(String.valueOf(completed));

		// GridPane을 다시 그리거나 clear할 필요 없음

//		double progressRate = (total == 0) ? 0 : (double) completed / total;
//		devProgressBar.setProgress(progressRate);
		devProgressBar.setTotalItems(total); // 초기 전체 값 설정
		devProgressBar.setCompletedItems(completed); // 초기 완료 값 설정
		
//		progressTextLabel.setText(String.format("%.1f%%", progressRate * 100));
	}

}
