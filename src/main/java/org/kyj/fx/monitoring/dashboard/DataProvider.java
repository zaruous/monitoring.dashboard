package org.kyj.fx.monitoring.dashboard;

import java.time.LocalDate;
import java.util.List;

import org.kyj.fx.monitoring.dashboard.web.Parameter;

/**
 * 데이터 제공자 인터페이스.
 * 다양한 데이터 소스(DB, Mock 등)로부터 데이터를 가져오는 방법을 정의합니다.
 * Strategy Pattern의 Strategy 인터페이스 역할을 합니다.
 */
public interface DataProvider {

    void initializeDatabase();

    InterfaceStatusSummary getInterfaceStatusSummary(LocalDate date);
//    InterfaceStatusSummary getInterfaceStatusSummary(Parameter pathParamMap);
    
    List<InterfaceStatusDetail> getInterfaceStatusDetails(Parameter pathParamMap);
    List<InterfaceStatusDetail> getInterfaceStatusDetails(LocalDate date, INF_STATUS status);

    
    List<ScheduleEntry> getScheduleEntries(LocalDate date);
    List<TableFluctuation> getTableFluctuations();

    int getDevelopmentItemCount(String status);

    boolean addInterfaceStatus(InterfaceStatusDetail statusDetail);

    
    // Service Error Log 관련 메서드
	List<ServiceErrorEntry> getServiceErrorEntries(LocalDate date);
	ServiceErrorLog getServiceErrorLog(String errorId);

	
}
