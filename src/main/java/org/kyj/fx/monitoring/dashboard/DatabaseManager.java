package org.kyj.fx.monitoring.dashboard;

import java.time.LocalDate;
import java.util.List;

/**
 * 데이터 제공자를 관리하고 애플리케이션에 데이터를 제공하는 중앙 클래스입니다.
 * Strategy Pattern의 Context 역할을 하며, Singleton Pattern으로 구현되어
 * 애플리케이션 전체에서 단일 인스턴스를 통해 데이터에 접근할 수 있도록 합니다.
 *
 * 사용된 디자인 패턴:
 * 1. Strategy Pattern: DataProvider 인터페이스를 통해 데이터 소스를 동적으로 교체할 수 있습니다.
 *    (예: SqliteDataProvider, MockDataProvider). 이를 통해 데이터베이스 구현의 변경이나
 *    테스트 환경으로의 전환이 유연해집니다.
 *    - Context: DatabaseManager
 *    - Strategy: DataProvider
 *    - ConcreteStrategy: SqliteDataProvider, MockDataProvider
 *
 * 2. Singleton Pattern: 애플리케이션 전체에서 단 하나의 DatabaseManager 인스턴스만 존재하도록 보장합니다.
 *    이를 통해 데이터 제공자 설정(Strategy 설정)을 일관되게 유지하고, 어디서든 동일한 데이터 소스에
 *    접근할 수 있습니다.
 */
public class DatabaseManager {

    private static final DatabaseManager instance = new DatabaseManager();
    private DataProvider dataProvider;

    // private 생성자로 외부에서 인스턴스 생성을 막음 (Singleton)
    private DatabaseManager() {
        // 기본 데이터 제공자를 SqliteDataProvider로 설정합니다.
        this.dataProvider = new SqliteDataProvider();
    }

    /**
     * DatabaseManager의 싱글턴 인스턴스를 반환합니다.
     *
     * @return DatabaseManager 인스턴스
     */
    public static DatabaseManager getInstance() {
        return instance;
    }

    /**
     * 사용할 데이터 제공자(Strategy)를 설정합니다.
     *
     * @param dataProvider 사용할 DataProvider 구현체
     */
    public void setDataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    /**
     * 현재 설정된 데이터 제공자를 사용하여 데이터베이스를 초기화합니다.
     */
    public void initializeDatabase() {
        if (dataProvider == null) {
            throw new IllegalStateException("DataProvider가 설정되지 않았습니다.");
        }
        dataProvider.initializeDatabase();
    }

    /**
     * 특정 상태의 모든 인터페이스 상세 정보를 조회합니다.
     *
     * @param status 조회할 상태 ('성공', '실패', '진행중')
     * @return 해당 상태의 InterfaceStatusDetail 리스트
     */
    public List<InterfaceStatusDetail> getInterfaceStatusDetails(LocalDate date, INF_STATUS status) {
        if (dataProvider == null) {
            throw new IllegalStateException("DataProvider가 설정되지 않았습니다.");
        }
        return dataProvider.getInterfaceStatusDetails(date, status);
    }

    /**
     * 특정 날짜의 스케줄 엔트리 목록을 조회합니다.
     *
     * @param date 조회할 날짜
     * @return 해당 날짜의 ScheduleEntry 리스트
     */
    public List<ScheduleEntry> getScheduleEntries(LocalDate date) {
        if (dataProvider == null) {
            throw new IllegalStateException("DataProvider가 설정되지 않았습니다.");
        }
        return dataProvider.getScheduleEntries(date);
    }

    /**
     * 테이블 데이터 변동 현황 목록을 조회합니다.
     *
     * @return TableFluctuation 리스트
     */
    public List<TableFluctuation> getTableFluctuations() {
        if (dataProvider == null) {
            throw new IllegalStateException("DataProvider가 설정되지 않았습니다.");
        }
        return dataProvider.getTableFluctuations();
    }

    /**
     * 개발 항목의 상태별 개수를 조회합니다.
     *
     * @param status 조회할 상태 ('total', 'inProgress', 'completed')
     * @return 해당 상태의 개발 항목 개수
     */
    public int getDevelopmentItemCount(String status) {
        if (dataProvider == null) {
            throw new IllegalStateException("DataProvider가 설정되지 않았습니다.");
        }
        return dataProvider.getDevelopmentItemCount(status);
    }

    /**
     * 새로운 인터페이스 상태 정보를 추가합니다.
     *
     * @param statusDetail 추가할 인터페이스 상태 상세 정보 DTO
     * @return 데이터 추가 성공 시 true, 실패 시 false
     */
    public boolean addInterfaceStatus(InterfaceStatusDetail statusDetail) {
        if (dataProvider == null) {
            throw new IllegalStateException("DataProvider가 설정되지 않았습니다.");
        }
        return dataProvider.addInterfaceStatus(statusDetail);
    }

	public List<ServiceErrorEntry> getServiceErrorEntries(LocalDate date) {
		if (dataProvider == null) {
            throw new IllegalStateException("DataProvider가 설정되지 않았습니다.");
        }
        return dataProvider.getServiceErrorEntries(date);
	}
}