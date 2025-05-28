package org.kyj.fx.monitoring.dashboard;

import javafx.geometry.Pos;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ProgressIndicatorSkin extends SkinBase<ProgressIndicatorControl> {

    private final Pane progressPane;
    private final Circle backgroundCircle;
    private final Arc progressArc;
    private final Text percentageText;
    private final VBox layout;

    private static final double DEFAULT_RADIUS = 80;
    private static final double STROKE_WIDTH = 30;

    /**
     * 스킨 생성자.
     *
     * @param control 이 스킨이 속한 컨트롤
     */
    protected ProgressIndicatorSkin(ProgressIndicatorControl control) {
        super(control);

        // 시각적 요소 초기화
        backgroundCircle = new Circle();
        progressArc = new Arc();
        percentageText = new Text();
        progressPane = new Pane(backgroundCircle, progressArc);
        layout = new VBox(5, progressPane, percentageText); // VBox로 묶어 중앙 정렬
        layout.setAlignment(Pos.CENTER);

        // 초기 그리기 및 리스너 등록
        updateGraphics();
        registerListeners(control);

        // 스킨의 루트 노드로 VBox 설정
        getChildren().add(layout);
    }

    /**
     * 컨트롤의 속성 변경을 감지하는 리스너를 등록합니다.
     * @param control 컨트롤
     */
    private void registerListeners(ProgressIndicatorControl control) {
        control.totalItemsProperty().addListener((obs, oldVal, newVal) -> updateGraphics());
        control.completedItemsProperty().addListener((obs, oldVal, newVal) -> updateGraphics());

        // 컨트롤의 너비/높이 변경 시 다시 그리기 (선택 사항, 레이아웃에 따라 필요)
        control.widthProperty().addListener((obs, oldVal, newVal) -> updateGraphics());
        control.heightProperty().addListener((obs, oldVal, newVal) -> updateGraphics());
    }

    /**
     * 컨트롤의 속성 값을 기반으로 시각적 요소를 업데이트합니다.
     */
    private void updateGraphics() {
        ProgressIndicatorControl control = getSkinnable();
        int total = control.getTotalItems();
        int completed = control.getCompletedItems();

        // 0으로 나누는 것을 방지
        if (total <= 0) {
            total = 1; // 또는 오류 처리
        }
        if (completed < 0) {
            completed = 0;
        }
        if (completed > total) {
            completed = total;
        }

        double progress = (double) completed / total;
        double angle = progress * 360;
        double percentage = progress * 100;

        // 크기 계산 (컨트롤 크기나 고정값 사용)
        // 여기서는 고정값을 사용하지만, 컨트롤 크기에 반응하도록 수정할 수 있습니다.
        double radius = DEFAULT_RADIUS - STROKE_WIDTH / 2;
        double centerX = DEFAULT_RADIUS;
        double centerY = DEFAULT_RADIUS;

        progressPane.setPrefSize(DEFAULT_RADIUS * 2, DEFAULT_RADIUS * 2);
        progressPane.setMinSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
        progressPane.setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);

        // 배경 원 업데이트
        backgroundCircle.setCenterX(centerX);
        backgroundCircle.setCenterY(centerY);
        backgroundCircle.setRadius(radius);
        backgroundCircle.setStroke(Color.LIGHTGRAY);
        backgroundCircle.setStrokeWidth(STROKE_WIDTH);
        backgroundCircle.setFill(null);

        // 진행률 아크 업데이트
        progressArc.setCenterX(centerX);
        progressArc.setCenterY(centerY);
        progressArc.setRadiusX(radius);
        progressArc.setRadiusY(radius);
        progressArc.setStartAngle(90);
        progressArc.setLength(-angle); // 시계 방향
        progressArc.setType(ArcType.OPEN);
        progressArc.setStroke(Color.DODGERBLUE);
        progressArc.setStrokeWidth(STROKE_WIDTH);
        progressArc.setFill(null);

        // 퍼센트 텍스트 업데이트
        percentageText.setText(String.format("%.1f%%", percentage));
        percentageText.setFont(Font.font("Arial Bold", 36));
        percentageText.setFill(Color.DODGERBLUE);

        // 레이아웃 재요청
        getSkinnable().requestLayout();
    }

    /**
     * 스킨의 루트 노드를 반환합니다.
     * Control의 getChildren() 메서드가 SkinBase에 정의되어 있고,
     * SkinBase의 생성자에서 getChildren().add()를 통해 노드를 추가하므로
     * 별도의 getNode() 구현은 필요하지 않습니다.
     */

    @Override
    public void dispose() {
        // 리스너 제거 등 정리 작업 (필요 시)
        getChildren().clear();
        super.dispose();
    }
}