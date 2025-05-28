/**
 * 
 */
package org.kyj.fx.monitoring.dashboard;

/**
 * 
 */
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class ProgressIndicatorControl extends Control {

    // --- 속성 정의 ---
    private final IntegerProperty totalItems;
    private final IntegerProperty completedItems;

    // --- 생성자 ---
    public ProgressIndicatorControl() {
        this.totalItems = new SimpleIntegerProperty(this, "totalItems", 100);
        this.completedItems = new SimpleIntegerProperty(this, "completedItems", 0);

        // 기본 스타일 클래스 설정 (CSS 적용 시 사용)
        getStyleClass().add("progress-indicator-control");
    }

    // --- 속성 Getter/Setter 및 Property 메서드 ---
    public final int getTotalItems() {
        return totalItems.get();
    }

    public final void setTotalItems(int value) {
        totalItems.set(value);
    }

    public final IntegerProperty totalItemsProperty() {
        return totalItems;
    }

    public final int getCompletedItems() {
        return completedItems.get();
    }

    public final void setCompletedItems(int value) {
        completedItems.set(value);
    }

    public final IntegerProperty completedItemsProperty() {
        return completedItems;
    }

    // --- 스킨 생성 ---
    @Override
    protected Skin<?> createDefaultSkin() {
        return new ProgressIndicatorSkin(this);
    }

    // --- 사용자 에이전트 스타일시트 (선택 사항) ---
    @Override
    public String getUserAgentStylesheet() {
        // 필요하다면 기본 CSS 파일 경로를 반환합니다.
        // return getClass().getResource("progress-indicator.css").toExternalForm();
        return null;
    }
}