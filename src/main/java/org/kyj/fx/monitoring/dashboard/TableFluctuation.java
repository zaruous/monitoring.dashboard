package org.kyj.fx.monitoring.dashboard;

public class TableFluctuation {

	private String tableName;
	private int previousRowCount;
	private int currentRowCount;
	private String changeRate; // 변동률은 문자열로 (예: "+2.5%")

	public TableFluctuation(String tableName, int previousRowCount, int currentRowCount) {
		this.tableName = tableName;
		this.previousRowCount = previousRowCount;
		this.currentRowCount = currentRowCount;
		double rate = 0;
		if (previousRowCount != 0) {
			rate = ((double) (currentRowCount - previousRowCount) / previousRowCount) * 100;
		} else if (currentRowCount > 0) {
			rate = 100.0; // 이전이 0이고 현재가 0보다 크면 100% 증가로 간주 (또는 다른 논리)
		}
		this.changeRate = String.format("%+.1f%%", rate); // 부호와 소수점 첫째자리까지
	}

	public String getTableName() {
		return tableName;
	}

	public int getPreviousRowCount() {
		return previousRowCount;
	}

	public int getCurrentRowCount() {
		return currentRowCount;
	}

	public String getChangeRate() {
		return changeRate;
	}

}
