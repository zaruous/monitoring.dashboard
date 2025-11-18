package org.kyj.fx.monitoring.dashboard;

public class InterfaceStatusDetail {

    private String id;
    private String name;
    private String timestamp;
    private String duration;
    private String server;
    private INF_STATUS status; // '성공', '실패', '진행중' 등의 상태를 저장할 필드
    private String errorCode;
    private String errorMessage;

    /**
     * 모든 필드를 초기화하는 생성자
     */
    public InterfaceStatusDetail(String id, String name, String timestamp, String duration, String server, INF_STATUS status,
            String errorCode, String errorMessage) {
        this.id = id;
        this.name = name;
        this.timestamp = timestamp;
        this.duration = duration;
        this.server = server;
        this.status = status;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    /**
     * 실패가 아닌 경우 (성공, 진행중 등) 사용하는 생성자
     */
    public InterfaceStatusDetail(String id, String name, String timestamp, String duration, String server, INF_STATUS status) {
        this(id, name, timestamp, duration, server, status, null, null);
    }
    
    // Getter methods for all fields
    public String getId() { return id; }
    public String getName() { return name; }
    public String getTimestamp() { return timestamp; }
    public String getDuration() { return duration; }
    public String getServer() { return server; }
    public INF_STATUS getStatus() { return status; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
}
