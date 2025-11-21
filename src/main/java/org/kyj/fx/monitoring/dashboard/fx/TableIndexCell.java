/**
 * 
 */
package org.kyj.fx.monitoring.dashboard.fx;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * 
 */
public class TableIndexCell<S, T> extends TableCell<S, T> {
	
	public TableIndexCell() {
		// 셀 내용 가운데 정렬
		setAlignment(Pos.CENTER_RIGHT);
	}
	
	@Override
	protected void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);

		if (empty || getIndex() < 0) {
			setText(null);
		} else {
			// getIndex()는 0부터 시작하므로 +1 하여 1부터 표시
			setText(String.valueOf(getIndex() + 1));
		}
	}

	// 컬럼에 쉽게 적용하기 위한 정적 팩토리 메서드
	public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn() {
		return column -> new TableIndexCell<>();
	}
}
