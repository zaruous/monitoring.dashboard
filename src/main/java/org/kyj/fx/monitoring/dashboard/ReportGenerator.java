package org.kyj.fx.monitoring.dashboard;

import java.time.LocalDate;
import java.util.List;

//import org.commonmark.node.Node;
//import org.commonmark.parser.Parser;
//import org.commonmark.renderer.html.HtmlRenderer;

public class ReportGenerator {

    private DatabaseManager dbManager;

    public ReportGenerator() {
        this.dbManager = DatabaseManager.getInstance();
        // Ensure data provider is set for batch mode.
//        if (dbManager.getDataProvider() == null) {
//            dbManager.setDataProvider(new AkcDataProvider());
//            dbManager.initializeDatabase();
//        }
    }

    public String generateMarkdownReport(LocalDate date) {
        StringBuilder report = new StringBuilder();

        // 1. 전체 인터페이스 현황
        report.append("# 모니터링 보고서\n\n");
        report.append("## 1. 전체 인터페이스 현황\n");
        
        List<InterfaceStatusDetail> overallStatus = dbManager.getInterfaceStatusDetails(date, null);
        long total = overallStatus.size();
        long success = overallStatus.stream().filter(v ->{
        	return v.getStatus() == INF_STATUS.SUCCESS;
        }).count();
        
        long retray = overallStatus.stream().filter(v ->{
        	return v.getStatus() == INF_STATUS.RETRY;
        }).count();
        
        long failure = overallStatus.stream().filter(v ->{
        	return v.getStatus() == INF_STATUS.FAIL;
        }).count();
        
        
        long etc = total - success - retray - failure;
        String summary = String.format("총 %d건 중 성공 %d건, 재전송 %d건 실패 %d건 기타 %d건 ", total, success, retray, failure, etc);
        report.append(summary).append("\n\n");
        report.append("### 조회일: ").append(date.toString()).append("\n\n");

        // 2. 데이터 변동률
        report.append("## 2. 데이터 변동률\n");
        report.append("| 테이블 명 | 이전 로우 수 | 현재 로우 수 | 변동률 |\n");
        report.append("|---|---|---|---|");
        List<TableFluctuation> fluctuationData = dbManager.getTableFluctuations();
        for (TableFluctuation item : fluctuationData) {
            report.append(String.format("| %s | %d | %d | %s |\n",
                    item.getTableName(), item.getPreviousRowCount(), item.getCurrentRowCount(), item.getChangeRate()));
        }
        report.append("\n");

        // 3. 스케줄 모니터링 현황
        report.append("## 3. 스케줄 모니터링 현황\n");
        report.append("### 조회일: ").append(date.toString()).append("\n\n");
        report.append("| 스케줄 ID | 인터페이스 명 | 상태 | 실행 시간 (소요 시간) |\n");
        report.append("|---|---|---|---|");
        List<ScheduleEntry> scheduleData = dbManager.getScheduleEntries(date);
        for (ScheduleEntry item : scheduleData) {
            report.append(String.format("| %s | %s | %s | %s |\n",
                    item.getScheduleId(), item.getInterfaceName(), item.getStatus(), item.getExecutionTimeDisplay()));
        }
        report.append("\n");
        
        // 4. 서비스 에러 내역
        report.append("\n## 4. 서비스 에러 내역\n");
        report.append("### 조회일: ").append(date.toString()).append("\n\n");
        report.append("| 에러 코드 | 에러 메시지 | 에러 설명 | 횟수 |\n");
        report.append("|---|---|---|---|");
        List<ServiceErrorEntry> serviceErrorData = dbManager.getServiceErrorEntries(date);
        for (ServiceErrorEntry item : serviceErrorData) {
            report.append(String.format("| %s | %s | %s | %d |\n",
                    item.getErrorCode(), item.getErrorMsg(), item.getErrorDesc(), item.getCount()));
        }

        return report.toString();
    }

//    public String convertMarkdownToHtml(String markdown) {
//        Parser parser = Parser.builder().build();
//        Node document = parser.parse(markdown);
//        HtmlRenderer renderer = HtmlRenderer.builder().build();
//        return renderer.render(document);
//    }
    
    public String generateHtmlReport(LocalDate date) {
    	String markdown = generateMarkdownReport(date);
    	return markdown;
//    	String htmlContent = convertMarkdownToHtml(markdown);
    	
//    	StringBuilder html = new StringBuilder();
//		html.append("<html><head><title>모니터링 보고서</title>");
//		html.append("<style>");
//		html.append("body { font-family: sans-serif; }");
//		html.append("table { border-collapse: collapse; width: 100%; }");
//		html.append("th, td { border: 1px solid #dddddd; text-align: left; padding: 8px; }");
//		html.append("thead { background-color: #f2f2f2; }");
//		html.append("</style>");
//		html.append("</head><body>");
//		html.append(htmlContent);
//		html.append("</body></html>");
//		
//		return html.toString();
    }
}
