package org.kyj.fx.monitoring.dashboard;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.kyj.fx.monitoring.dashboard.web.Parameter;

/**
 * Mock 데이터를 제공하는 클래스. DataProvider 인터페이스의 구체적인 구현체입니다. 테스트 또는 개발 환경에서 사용될 수
 * 있습니다. Strategy Pattern의 Concrete Strategy 역할을 합니다.
 */
public class MockDataProvider implements DataProvider {

	private final List<InterfaceStatusDetail> interfaceStatusDetails = new ArrayList<>();
	private final List<ScheduleEntry> scheduleEntries = new ArrayList<>();
	private final List<TableFluctuation> tableFluctuations = new ArrayList<>();
	private final Map<String, Integer> developmentItems = new HashMap<>();
	private final List<ServiceErrorEntry> serviceErrorEntries = new ArrayList<>();

	public MockDataProvider() {
		// Mock 데이터 초기화
		initializeDatabase();
	}

	@Override
	public void initializeDatabase() {
		interfaceStatusDetails.clear();
		scheduleEntries.clear();
		tableFluctuations.clear();
		developmentItems.clear();
		serviceErrorEntries.clear();

		// Interface Status Mock Data
		interfaceStatusDetails.add(new InterfaceStatusDetail("IF-MOCK-001", "Mock 주문 접수", "2024-05-28 10:00:00", "10ms",
				"MOCK_GW_01", INF_STATUS.SUCCESS, null, null));
		interfaceStatusDetails.add(new InterfaceStatusDetail("IF-MOCK-002", "Mock 고객 인증", "2024-05-28 10:05:00", "50ms",
				"MOCK_AUTH_01", INF_STATUS.FAIL, "E401", "Mock 인증 토큰 만료"));
		interfaceStatusDetails.add(new InterfaceStatusDetail("IF-MOCK-003", "Mock 배송 상태", "2024-05-28 11:00:00", "N/A",
				"MOCK_SHIP_01", INF_STATUS.IN_PROGRESS, null, null));

		// Schedule Entries Mock Data
		scheduleEntries.add(new ScheduleEntry("SCH-MOCK-001", "Mock 일일 판매 집계", "성공", "2024-05-28 02:00:00", "1분"));
		scheduleEntries.add(new ScheduleEntry("SCH-MOCK-002", "Mock 데이터 백업", "실패", "2024-05-27 03:00:00", "30초"));

		// Table Fluctuation Mock Data
		tableFluctuations.add(new TableFluctuation("TB_MOCK_ORDERS", 1000, 1200));
		tableFluctuations.add(new TableFluctuation("TB_MOCK_USERS", 500, 505));

		// Development Items Mock Data
		developmentItems.put("total", 10);
		developmentItems.put("inProgress", 3);
		developmentItems.put("completed", 5);

		// Service Error Entries Mock Data
		serviceErrorEntries.add(new ServiceErrorEntry("ERR-MOCK-001", "E-500", "MOCK_SVC_01", "Mock 서비스 오류 1",
				"Critical Error", 1, "Log...", LocalDateTime.of(2024, 5, 28, 10, 30, 0)));
		serviceErrorEntries.add(new ServiceErrorEntry("ERR-MOCK-002", "E-404", "MOCK_SVC_02", "Mock 서비스 오류 2",
				"Not Found", 1, "Log...", LocalDateTime.of(2024, 5, 28, 11, 0, 0)));
		serviceErrorEntries.add(new ServiceErrorEntry("ERR-MOCK-003", "E-500", "MOCK_SVC_01", "Mock 서비스 오류 3",
				"Minor Error", 1, "Log...", LocalDateTime.of(2024, 5, 27, 15, 0, 0)));
	}

	@Override
	public List<ScheduleEntry> getScheduleEntries(LocalDate date) {
		// Mock 데이터는 날짜 필터링을 단순화하여 일부만 반환하거나, 모든 항목을 반환할 수 있습니다.
		// 여기서는 간단히 모든 항목을 반환합니다.
		return new ArrayList<>(scheduleEntries);
	}

	@Override
	public List<TableFluctuation> getTableFluctuations() {
		return new ArrayList<>(tableFluctuations);
	}

	@Override
	public int getDevelopmentItemCount(String status) {
		return developmentItems.getOrDefault(status, 0);
	}

	@Override
	public boolean addInterfaceStatus(InterfaceStatusDetail statusDetail) {
		return interfaceStatusDetails.add(statusDetail);
	}

	@Override
	public List<ServiceErrorEntry> getServiceErrorEntries(LocalDate date) {
		// Mock 데이터는 날짜 필터링을 단순화하여 일부만 반환하거나, 모든 항목을 반환할 수 있습니다.
		// 여기서는 간단히 모든 항목을 반환합니다.
		return serviceErrorEntries.stream()
//                .filter(entry -> entry.getRegDate().toLocalDate().isEqual(date))
				.collect(Collectors.toList());
	}

	@Override
	public ServiceErrorLog getServiceErrorLog(String errorId) {
		return new ServiceErrorLog(errorId, "Not Implemented");
	}

	@Override
	public InterfaceStatusSummary getInterfaceStatusSummary(LocalDate date) {
		return new InterfaceStatusSummary() {
			{
				setSuccess(10);
				setInProgress(5);
				setTotal(15);
			}
		};
	}

	@Override
	public List<InterfaceStatusDetail> getInterfaceStatusDetails(Parameter pathParamMap) {
		return Collections.emptyList();
	}

	@Override
	public List<InterfaceStatusDetail> getInterfaceStatusDetails(LocalDate date, INF_STATUS status) {
		return interfaceStatusDetails.stream()
              .filter(detail -> detail.getStatus().equals(status))
				.collect(Collectors.toList());
	}
}
