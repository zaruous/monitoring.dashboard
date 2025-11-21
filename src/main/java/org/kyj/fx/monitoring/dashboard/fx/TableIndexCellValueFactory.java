/**
 * 
 */
package org.kyj.fx.monitoring.dashboard.fx;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class TableIndexCellValueFactory<S> implements Callback<CellDataFeatures<S, Integer>, ObservableValue<Integer>> {

	@Override
	public ObservableValue<Integer> call(CellDataFeatures<S, Integer> features) {
		TableView<S> table = features.getTableView();
		S item = features.getValue();

		// 현재 테이블 아이템 리스트에서 해당 객체의 인덱스를 찾음 (0-based -> 1-based)
		// 주의: indexOf는 O(N) 연산이므로 데이터가 많으면 느려질 수 있음
		int index = table.getItems().indexOf(item);

		return new ReadOnlyObjectWrapper<>(index + 1);
	}
	
	
	public static <S> TableIndexCellValueFactory<S> forTableColumn() {
		return new TableIndexCellValueFactory<>();
	}
}