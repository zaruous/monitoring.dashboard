package org.kyj.fx.monitoring.dashboard;

import java.time.LocalDate;
import java.util.List;

/**
 * 데이터 제공자 인터페이스.
 * 다양한 데이터 소스(DB, Mock 등)로부터 데이터를 가져오는 방법을 정의합니다.
 * Strategy Pattern의 Strategy 인터페이스 역할을 합니다.
 */
public interface DataProvider {

    void initializeDatabase();

    List<InterfaceStatusDetail> getInterfaceStatusDetails(String status);

    List<ScheduleEntry> getScheduleEntries(LocalDate date);

    List<TableFluctuation> getTableFluctuations();

    int getDevelopmentItemCount(String status);

    boolean addInterfaceStatus(InterfaceStatusDetail statusDetail);
}
