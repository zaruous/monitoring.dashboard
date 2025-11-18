/**
 * 
 */
package org.kyj.fx.monitoring.dashboard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 
 */
public class AkcDataProvider implements DataProvider {

	private static String DB_URL = "jdbc:sqlserver://hostname:1433;databaseName=dbname;encrypt=false";
	private static String USERID = "";
	private static String USERPWD = "";
	Properties orLoad;
	public AkcDataProvider() {
		orLoad = PropertiesUtil.createOrLoad(AkcDataProvider.class, ()->{
			return PropertiesUtil.of(Map.of("db.url", "", "db.user", "", "db.password", ""));
		});
		DB_URL = orLoad.getProperty("db.url");
		USERID = orLoad.getProperty("db.user");
		USERPWD = orLoad.getProperty("db.password");
	}
	
    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USERID, USERPWD);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
	@Override
	public void initializeDatabase() {
		/*Nothing*/
		
	}

	private String mapStatusToAkcCode(INF_STATUS status) {
		switch(status) {
		case SUCCESS:
			return "S";
		case FAIL:
			return "E";
		case IN_PROGRESS:
			return "W";
		case RETRY:
			return "R";
		default:
			return "U";
		}
	}
	
	private INF_STATUS mapAkcCodeToStatus(String code) {
		switch(code) {
		case "S":
			return INF_STATUS.SUCCESS;
		case "E":
			return INF_STATUS.FAIL;
		case "W":
			return INF_STATUS.IN_PROGRESS;
		case "R":
			return INF_STATUS.RETRY;
		default:
			return INF_STATUS.UNKNOWN;
		}
	}
	
	
	@Override
	public List<InterfaceStatusDetail> getInterfaceStatusDetails(LocalDate date, INF_STATUS status) {
		
		String akcStatus = mapStatusToAkcCode(status);
		StringBuffer sb = new StringBuffer();
		sb.append("select EAI_KEY, IF_ID, if_process_status  from dbo.IINFLOGDAT (nolock)\n");
		sb.append("		where 1=1 \n");
		sb.append("	            	AND IF_DATE >= ?\n");
		sb.append("	            	AND IF_DATE <= ?\n");
		sb.append("	            	AND if_process_status = ? \n");
		sb.append("		order by if_id\n");
		
		String fromDate = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String toDate = date.plusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		
		List<InterfaceStatusDetail> details = new ArrayList<>();
		try (Connection conn = this.connect();
	             PreparedStatement pstmt = conn.prepareStatement(sb.toString())) {
				pstmt.setString(1, fromDate);
				pstmt.setString(2, toDate);
				pstmt.setString(3, akcStatus);
	            ResultSet rs = pstmt.executeQuery();
	            while (rs.next()) {
	                details.add(new InterfaceStatusDetail(
	                        rs.getString("EAI_KEY"),
	                        rs.getString("IF_ID"),
	                        null,
	                        null,
	                        "akc",
	                        mapAkcCodeToStatus(rs.getString("if_process_status")),
	                        "",
	                        "")
	                		);
	            }
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
		
		return details;
	}

	@Override
	public List<ScheduleEntry> getScheduleEntries(LocalDate date) {
		StringBuffer sb = new StringBuffer();
		sb.append("select\n");
		sb.append("sched_name,\n");
		sb.append("job_name,\n");
		sb.append("job_group,\n");
		sb.append("job_class_name,\n");
		sb.append("job_data_map,\n");
		sb.append("result_status,\n");
		sb.append("result_msg,\n");
		sb.append("error_msg,\n");
		sb.append("start_time,\n");
		sb.append("end_time,\n");
		sb.append("elapsed_millis,\n");
		sb.append("job_grp_1,\n");
		sb.append("job_grp_2,\n");
		sb.append("job_grp_3,\n");
		sb.append("job_grp_4,\n");
		sb.append("job_grp_5,\n");
		sb.append("job_cmf_1,\n");
		sb.append("job_cmf_2,\n");
		sb.append("job_cmf_3,\n");
		sb.append("job_cmf_4,\n");
		sb.append("job_cmf_5,\n");
		sb.append("job_cmf_6,\n");
		sb.append("job_cmf_7,\n");
		sb.append("job_cmf_8,\n");
		sb.append("job_cmf_9,\n");
		sb.append("job_cmf_10 \n");
		sb.append("from qrtz_job_log \n");
		sb.append("where 1=1\n");
		sb.append("and start_time >= ? \n");
		sb.append("and start_time <= ? \n");
		sb.append("order by\n");
		sb.append("start_time desc \n");
		String fromDate = date.format(DateTimeFormatter.ISO_DATE);
		String toDate = date.plusDays(1).format(DateTimeFormatter.ISO_DATE);
		List<ScheduleEntry> details = new ArrayList<>();
		try (Connection conn = this.connect();
	             PreparedStatement pstmt = conn.prepareStatement(sb.toString())) {
	            pstmt.setString(1, fromDate);
	            pstmt.setString(2, toDate);
	            ResultSet rs = pstmt.executeQuery();
	            while (rs.next()) {
	                details.add(new ScheduleEntry(
	                        rs.getString("sched_name"),
	                        rs.getString("job_name"),
	                        rs.getString("result_status"),
	                        rs.getString("start_time"),
	                        rs.getString("elapsed_millis")
	                        ));
	            }
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
		
		return details;
	}

	@Override
	public List<TableFluctuation> getTableFluctuations() {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

	@Override
	public int getDevelopmentItemCount(String status) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean addInterfaceStatus(InterfaceStatusDetail statusDetail) {
		// TODO Auto-generated method stub
		return false;
	}

}
