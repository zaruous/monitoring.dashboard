package org.kyj.fx.monitoring.dashboard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:monitoring.db";

    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void initializeDatabase() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            // 테이블 생성
            stmt.execute("DROP TABLE IF EXISTS interface_status;");
            stmt.execute("CREATE TABLE interface_status (" +
                         "id TEXT PRIMARY KEY, " +
                         "name TEXT NOT NULL, " +
                         "timestamp TEXT NOT NULL, " +
                         "duration TEXT, " +
                         "server TEXT, " +
                         "status TEXT NOT NULL, " + // '성공', '실패', '진행중'
                         "error_code TEXT, " +
                         "error_message TEXT);");

            stmt.execute("DROP TABLE IF EXISTS schedule_entries;");
            stmt.execute("CREATE TABLE schedule_entries (" +
                         "schedule_id TEXT PRIMARY KEY, " +
                         "interface_name TEXT NOT NULL, " +
                         "status TEXT NOT NULL, " +
                         "execution_time TEXT NOT NULL, " +
                         "duration TEXT, " +
                         "schedule_date TEXT NOT NULL);");
            
            stmt.execute("DROP TABLE IF EXISTS table_fluctuation;");
            stmt.execute("CREATE TABLE table_fluctuation (" +
                         "table_name TEXT PRIMARY KEY, " +
                         "previous_row_count INTEGER NOT NULL, " +
                         "current_row_count INTEGER NOT NULL);");
            
             stmt.execute("DROP TABLE IF EXISTS development_items;");
             stmt.execute("CREATE TABLE development_items (" +
                          "status TEXT PRIMARY KEY, " + // 'total', 'inProgress', 'completed'
                          "count INTEGER NOT NULL);");


            // 기본 데이터 삽입
            insertInitialData(conn);

        } catch (SQLException e) {
            System.out.println("Database initialization error: " + e.getMessage());
        }
    }

    private void insertInitialData(Connection conn) throws SQLException {
        // Interface Status Data
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO interface_status VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
        // 성공
        pstmt.setString(1, "IF-001"); pstmt.setString(2, "주문 접수"); pstmt.setString(3, "2024-05-28 10:00:00"); pstmt.setString(4, "120ms"); pstmt.setString(5, "API_GW_01"); pstmt.setString(6, "성공"); pstmt.setString(7, null); pstmt.setString(8, null); pstmt.addBatch();
        pstmt.setString(1, "IF-003"); pstmt.setString(2, "상품 조회"); pstmt.setString(3, "2024-05-28 10:02:00"); pstmt.setString(4, "80ms"); pstmt.setString(5, "PRODUCT_SVC_02"); pstmt.setString(6, "성공"); pstmt.setString(7, null); pstmt.setString(8, null); pstmt.addBatch();
        // 실패
        pstmt.setString(1, "IF-002"); pstmt.setString(2, "고객 인증"); pstmt.setString(3, "2024-05-28 10:05:00"); pstmt.setString(4, "500ms"); pstmt.setString(5, "AUTH_SVC_01"); pstmt.setString(6, "실패"); pstmt.setString(7, "E401"); pstmt.setString(8, "인증 토큰 만료"); pstmt.addBatch();
        pstmt.setString(1, "IF-004"); pstmt.setString(2, "재고 확인"); pstmt.setString(3, "2024-05-28 10:15:00"); pstmt.setString(4, "1200ms"); pstmt.setString(5, "INVENTORY_SVC_01"); pstmt.setString(6, "실패"); pstmt.setString(7, "E503"); pstmt.setString(8, "재고 서비스 응답 없음"); pstmt.addBatch();
        // 진행중
        pstmt.setString(1, "IF-005"); pstmt.setString(2, "배송 상태 업데이트"); pstmt.setString(3, "2024-05-28 11:00:00"); pstmt.setString(4, "N/A"); pstmt.setString(5, "SHIPPING_SVC_01"); pstmt.setString(6, "진행중"); pstmt.setString(7, null); pstmt.setString(8, null); pstmt.addBatch();
        pstmt.executeBatch();

        // Schedule Entries Data
        pstmt = conn.prepareStatement("INSERT INTO schedule_entries VALUES (?, ?, ?, ?, ?, ?);");
        pstmt.setString(1, "SCH-001"); pstmt.setString(2, "일일 판매 집계"); pstmt.setString(3, "성공"); pstmt.setString(4, "2024-05-28 02:00:15"); pstmt.setString(5, "5분 10초"); pstmt.setString(6, "2024-05-28"); pstmt.addBatch();
        pstmt.setString(1, "SCH-002"); pstmt.setString(2, "데이터 백업"); pstmt.setString(3, "성공"); pstmt.setString(4, "2024-05-28 03:00:05"); pstmt.setString(5, "12분 30초"); pstmt.setString(6, "2024-05-28"); pstmt.addBatch();
        pstmt.setString(1, "SCH-003"); pstmt.setString(2, "사용자 활동 로그 분석"); pstmt.setString(3, "실패"); pstmt.setString(4, "2024-05-27 04:00:00"); pstmt.setString(5, "2분 (오류 발생)"); pstmt.setString(6, "2024-05-27"); pstmt.addBatch();
        pstmt.executeBatch();
        
        // Table Fluctuation Data
        pstmt = conn.prepareStatement("INSERT INTO table_fluctuation VALUES (?, ?, ?);");
        pstmt.setString(1, "TB_ORDERS"); pstmt.setInt(2, 10250); pstmt.setInt(3, 10570); pstmt.addBatch();
        pstmt.setString(1, "TB_CUSTOMERS"); pstmt.setInt(2, 5120); pstmt.setInt(3, 5135); pstmt.addBatch();
        pstmt.setString(1, "TB_PRODUCTS"); pstmt.setInt(2, 850); pstmt.setInt(3, 845); pstmt.addBatch();
        pstmt.executeBatch();

        // Development Items Data
        pstmt = conn.prepareStatement("INSERT INTO development_items VALUES (?, ?);");
        pstmt.setString(1, "total"); pstmt.setInt(2, 25); pstmt.addBatch();
        pstmt.setString(1, "inProgress"); pstmt.setInt(2, 7); pstmt.addBatch();
        pstmt.setString(1, "completed"); pstmt.setInt(2, 15); pstmt.addBatch();
        pstmt.executeBatch();
        
        pstmt.close();
    }
    
    public List<InterfaceStatusDetail> getInterfaceStatusDetails(String status) {
        String sql = "SELECT * FROM interface_status WHERE status = ?";
        List<InterfaceStatusDetail> details = new ArrayList<>();
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                // 생성자에 DB에서 읽은 status 필드를 추가해야 합니다.
                details.add(new InterfaceStatusDetail(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("timestamp"),
                        rs.getString("duration"),
                        rs.getString("server"),
                        rs.getString("status"), // 누락되었던 status 필드 추가
                        rs.getString("error_code"),
                        rs.getString("error_message")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return details;
    }

    public List<ScheduleEntry> getScheduleEntries(LocalDate date) {
        String sql = "SELECT * FROM schedule_entries WHERE schedule_date = ?";
        List<ScheduleEntry> entries = new ArrayList<>();
        String dateString = date.format(DateTimeFormatter.ISO_DATE);
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dateString);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                entries.add(new ScheduleEntry(
                    rs.getString("schedule_id"), rs.getString("interface_name"),
                    rs.getString("status"), rs.getString("execution_time"),
                    rs.getString("duration")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return entries;
    }
    
    public List<TableFluctuation> getTableFluctuations() {
        String sql = "SELECT * FROM table_fluctuation";
        List<TableFluctuation> fluctuations = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                fluctuations.add(new TableFluctuation(
                    rs.getString("table_name"),
                    rs.getInt("previous_row_count"),
                    rs.getInt("current_row_count")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return fluctuations;
    }
    
    public int getDevelopmentItemCount(String status) {
        String sql = "SELECT count FROM development_items WHERE status = ?";
        int count = 0;
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt("count");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return count;
    }
    
    /**
     * 새로운 인터페이스 상태 정보를 데이터베이스에 추가합니다.
     * @param statusDetail 추가할 인터페이스 상태 상세 정보 DTO
     * @return 데이터 추가 성공 시 true, 실패 시 false
     */
    public boolean addInterfaceStatus(InterfaceStatusDetail statusDetail) {
        String sql = "INSERT INTO interface_status(id, name, timestamp, duration, server, status, error_code, error_message) VALUES(?,?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, statusDetail.getId());
            pstmt.setString(2, statusDetail.getName());
            pstmt.setString(3, statusDetail.getTimestamp());
            pstmt.setString(4, statusDetail.getDuration());
            pstmt.setString(5, statusDetail.getServer());
            
            // 에러 코드가 있으면 '실패', 없으면 '성공'으로 기본 상태 지정
            String status = (statusDetail.getErrorCode() != null) ? "실패" : "성공";
            pstmt.setString(6, status);
            
            pstmt.setString(7, statusDetail.getErrorCode());
            pstmt.setString(8, statusDetail.getErrorMessage());
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error adding interface status: " + e.getMessage());
            return false;
        }
    }
}