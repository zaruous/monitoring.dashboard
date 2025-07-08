package monitoring.dashboard;

import org.kyj.fx.monitoring.dashboard.DatabaseManager;
import org.kyj.fx.monitoring.dashboard.InterfaceStatusDetail;

public class MainApp { // 또는 테스트를 위한 다른 클래스

    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager();

        // 1. '진행중' 상태의 데이터 추가
        InterfaceStatusDetail inProgressData = new InterfaceStatusDetail(
            "IF-201", 
            "대용량 파일 업로드", 
            "2025-07-08 15:00:00", 
            "N/A", 
            "FILE_GW_01", 
            "진행중" // 상태를 명시적으로 지정
        );
        dbManager.addInterfaceStatus(inProgressData);

        // 2. '성공' 상태의 데이터 추가
        InterfaceStatusDetail successData = new InterfaceStatusDetail(
            "IF-202", 
            "사용자 프로필 조회", 
            "2025-07-08 15:02:10", 
            "50ms", 
            "USER_SVC_03",
            "성공" // 상태를 명시적으로 지정
        );
        dbManager.addInterfaceStatus(successData);

        // 3. '실패' 상태의 데이터 추가
        InterfaceStatusDetail failData = new InterfaceStatusDetail(
            "IF-203", 
            "결제 요청 처리", 
            "2025-07-08 15:03:00", 
            "1500ms", 
            "PAYMENT_GW_02",
            "실패", // 상태를 명시적으로 지정
            "E-504", 
            "Gateway Timeout"
        );
        dbManager.addInterfaceStatus(failData);
        
        System.out.println("3개의 새로운 인터페이스 상태 데이터 추가 시도 완료.");
        
        // JavaFX 애플리케이션 실행
        // Application.launch(InterfaceMonitoringDashboardApp.class, args);
    }
}