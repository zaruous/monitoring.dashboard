package org.kyj.fx.monitoring.dashboard;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CardControl extends VBox {
	public CardControl(String title) {
		setPadding(new Insets(15));
		setSpacing(10);
		setStyle(
				"-fx-background-color: white; -fx-border-radius: 8; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0.5, 0, 0);");
		// VBox가 자식 요소의 크기에 맞게 늘어나도록 설정
		setFillWidth(true);

		Label titleLabel = new Label(title);
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		titleLabel.setTextFill(Color.web("#4A5568")); // text-gray-700
		getChildren().add(titleLabel);
	}
}
