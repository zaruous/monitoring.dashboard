package org.kyj.fx.monitoring.dashboard;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.kyj.fx.monitoring.dashboard.plugin.MonitoringPlugin;
import org.kyj.fx.monitoring.dashboard.plugin.PluginManager;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class InterfaceMonitoringDashboardApp extends Application {
    // 각 컨트롤에 대한 참조를 저장할 필드
    private OverallStatusControl overallStatusControl;
    private ScheduleMonitoringControl scheduleMonitoringControl;
    private DataFluctuationControl dataFluctuationControl;
    private ServiceErrorMonitoringControl serviceErrorMonitoringControl; // 변경
    private GridPane mainGridPane;
    private Scene rootScene;
    
    public static void main(String[] args) {
		if (args.length > 0) {
			String mode = args[0];
			if ("batch".equalsIgnoreCase(mode)) {
				runBatchMode();
			} else if ("web".equalsIgnoreCase(mode)) {
				org.kyj.fx.monitoring.dashboard.web.WebApp.main(args);
			} else {
				launch(args);
			}
		} else {
			launch(args);
		}
	}
    
	@Override
	public void start(Stage primaryStage) {
		
		
		Properties orLoad = PropertiesUtil.createOrLoad(InterfaceMonitoringDashboardApp.class, ()->{
			return PropertiesUtil.of(Map.of("provider.class.name", "org.kyj.fx.monitoring.dashboard.MockDataProvider"));
		});
		String clazz = orLoad.getProperty("provider.class.name", "org.kyj.fx.monitoring.dashboard.MockDataProvider");
		DataProvider provider = new MockDataProvider();
		try {
			Class<?> forName = Class.forName(clazz);
			Constructor<?> constructor = forName.getConstructor();
			provider = (DataProvider) constructor.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        DatabaseManager dbManager = DatabaseManager.getInstance();
        dbManager.setDataProvider(provider);
        dbManager.initializeDatabase();

        primaryStage.setTitle("인터페이스 모니터링 보드 (JavaFX)");

        GridPane gridPane = new GridPane();
        this.mainGridPane = gridPane;
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setPadding(new Insets(20));
        gridPane.setStyle("-fx-background-color: #f3f4f6;");

        // 컨트롤 초기화 및 필드에 할당
        overallStatusControl = new OverallStatusControl();
        scheduleMonitoringControl = new ScheduleMonitoringControl();
        dataFluctuationControl = new DataFluctuationControl();
        serviceErrorMonitoringControl = new ServiceErrorMonitoringControl(); // 변경

        GridPane.setConstraints(overallStatusControl, 0, 0);
        GridPane.setConstraints(scheduleMonitoringControl, 1, 0);
        GridPane.setConstraints(dataFluctuationControl, 0, 1);
        GridPane.setConstraints(serviceErrorMonitoringControl, 1, 1); // 변경

        gridPane.getChildren().addAll(overallStatusControl, scheduleMonitoringControl, dataFluctuationControl,
                serviceErrorMonitoringControl); // 변경

		// 컬럼 및 로우 제약 조건 설정 (균등하게 공간 분배)
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(50);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(50);
		gridPane.getColumnConstraints().addAll(col1, col2);

		RowConstraints row1 = new RowConstraints();
		row1.setPercentHeight(50);
		RowConstraints row2 = new RowConstraints();
		row2.setPercentHeight(50);
		gridPane.getRowConstraints().addAll(row1, row2);

		// --- 헤더 수정 ---
        Label titleLabel = new Label("모니터링 보드");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#333333"));

        // 버튼을 오른쪽으로 밀기 위한 스페이서
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // 새로고침 버튼 생성
        Button btnReload = new Button("새로고침");
        btnReload.setStyle("-fx-font-size: 14px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");
        btnReload.setOnAction(ev -> reloadAllData()); // 액션 설정

        // 리포트 생성 버튼 추가
        Button btnReport = new Button("리포트 생성");
        btnReport.setStyle("-fx-font-size: 14px; -fx-background-color: #008CBA; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");
        btnReport.setOnAction(ev -> generateMarkdownReport());

        HBox titleBox = new HBox(titleLabel, spacer, btnReport, btnReload);
        titleBox.setSpacing(10); // 버튼 사이 간격
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(10, 20, 10, 20));

        BorderPane rootLayout = new BorderPane();
        
        // 메뉴바 생성
        MenuBar menuBar = createMenuBar();
        
        VBox topContainer = new VBox(menuBar, titleBox);
        rootLayout.setTop(topContainer);
        rootLayout.setCenter(gridPane);
        rootLayout.setStyle("-fx-background-color: #f3f4f6;");

        rootScene = new Scene(rootLayout, 1200, 850);
        primaryStage.setScene(rootScene);
        primaryStage.show();
	}

	private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        
        Menu homeMenu = new Menu("Home");
        MenuItem dashboardItem = new MenuItem("Dashboard");
        dashboardItem.setOnAction(e -> {
            if (mainGridPane != null) {
                BorderPane rootLayout = (BorderPane) rootScene.getRoot();
                rootLayout.setCenter(mainGridPane);
            }
        });
        homeMenu.getItems().add(dashboardItem);
        
        Menu pluginsMenu = new Menu("Plugins");
        loadPlugins(pluginsMenu);
        
        menuBar.getMenus().addAll(homeMenu, pluginsMenu);
        return menuBar;
    }

    private void loadPlugins(Menu pluginsMenu) {
        List<MonitoringPlugin> plugins = PluginManager.loadPlugins();
        Map<String, List<MonitoringPlugin>> categorizedPlugins = plugins.stream()
                .collect(Collectors.groupingBy(MonitoringPlugin::getCategory));

        categorizedPlugins.forEach((category, pluginList) -> {
            Menu categoryMenu = new Menu(category);
            pluginList.forEach(plugin -> {
                MenuItem menuItem = new MenuItem(plugin.getName());
                menuItem.setOnAction(e -> showPluginView(plugin));
                categoryMenu.getItems().add(menuItem);
            });
            pluginsMenu.getItems().add(categoryMenu);
        });
    }

    private void showPluginView(MonitoringPlugin plugin) {
        Node pluginView = plugin.getMonitoringView();
//        if (mainGridPane != null) {
            BorderPane rootLayout = (BorderPane) rootScene.getRoot();
            rootLayout.setCenter(pluginView);
//        }
    }

	private void generateMarkdownReport() {
	    FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Save Report");
	    fileChooser.setInitialFileName("Monitoring_Report_" + java.time.LocalDate.now() + ".md");
	    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Markdown Files", "*.md"));
	    File file = fileChooser.showSaveDialog(null);

	    if (file != null) {
	        try (java.io.PrintWriter writer = new java.io.PrintWriter(file)) {
	            StringBuilder report = new StringBuilder();
	            
	            // 1. 전체 인터페이스 현황
	            report.append("# 모니터링 보고서\n\n");
	            report.append("## 1. 전체 인터페이스 현황\n");
	            report.append(overallStatusControl.getSummaryText()).append("\n\n");
	            LocalDate selectedDate = overallStatusControl.getSelectedDate();
	            report.append("### 조회일: ").append(selectedDate != null ? selectedDate.toString() : "N/A").append("\n\n");
	            
	            // 2. 데이터 변동률
	            report.append("## 2. 데이터 변동률\n");
	            report.append("| 테이블 명 | 이전 로우 수 | 현재 로우 수 | 변동률 |\n");
	            report.append("|---|---|---|---|\n");
	            for (TableFluctuation item : dataFluctuationControl.getFluctuationData()) {
	                report.append(String.format("| %s | %d | %d | %s |\n",
	                        item.getTableName(), item.getPreviousRowCount(), item.getCurrentRowCount(), item.getChangeRate()));
	            }
	            report.append("\n");

	            // 3. 스케줄 모니터링 현황
	            selectedDate = scheduleMonitoringControl.getSelectedDate();
	            report.append("## 3. 스케줄 모니터링 현황\n");
	            report.append("### 조회일: ").append(selectedDate != null ? selectedDate.toString() : "N/A").append("\n\n");
	            report.append("| 스케줄 ID | 인터페이스 명 | 상태 | 실행 시간 (소요 시간) |\n");
	            report.append("|---|---|---|---|\n");
	            for (ScheduleEntry item : scheduleMonitoringControl.getScheduleData()) {
	                report.append(String.format("| %s | %s | %s | %s |\n",
	                        item.getScheduleId(), item.getInterfaceName(), item.getStatus(), item.getExecutionTimeDisplay()));
	            }
	            
	            // 4. 서비스 에러 내역
	            report.append("\n## 4. 서비스 에러 내역\n");
	            selectedDate = serviceErrorMonitoringControl.getSelectedDate();
	            report.append("### 조회일: ").append(selectedDate != null ? selectedDate.toString() : "N/A").append("\n\n");
	            report.append("| 에러 코드 | 에러 메시지 | 에러 설명 | 횟수 |\n");
	            report.append("|---|---|---|---|\n");
	            for (ServiceErrorEntry item : serviceErrorMonitoringControl.getServiceErrorData()) {
	                report.append(String.format("| %s | %s | %s | %d |\n",
	                        item.getErrorCode(), item.getErrorMsg(), item.getErrorDesc(), item.getCount()));
	            }

	            writer.println(report.toString());
	            
	        } catch (java.io.IOException e) {
	            e.printStackTrace();
	        }
	    }
	}

	 /**
     * 모든 대시보드 컴포넌트의 데이터를 다시 로드합니다.
     */
    private void reloadAllData() {
        System.out.println("Reloading all dashboard data...");
        overallStatusControl.reloadData();
        scheduleMonitoringControl.reloadData();
        dataFluctuationControl.reloadData();
        serviceErrorMonitoringControl.reloadData();
        System.out.println("Data reloaded.");
    }
    
    public LocalDate getSelectedDate() {
		return overallStatusControl.getSelectedDate();
	}
    
	

	private static void runBatchMode() {
		System.out.println("Running in batch mode...");
		
		Properties orLoad = PropertiesUtil.createOrLoad(InterfaceMonitoringDashboardApp.class, ()->{
			return PropertiesUtil.of(Map.of("provider.class.name", "org.kyj.fx.monitoring.dashboard.MockDataProvider"));
		});
		String clazz = orLoad.getProperty("provider.class.name", "org.kyj.fx.monitoring.dashboard.MockDataProvider");
		DataProvider provider = new MockDataProvider();
		try {
			Class<?> forName = Class.forName(clazz);
			Constructor<?> constructor = forName.getConstructor();
			provider = (DataProvider) constructor.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		DatabaseManager dbManager = DatabaseManager.getInstance();
		dbManager.setDataProvider(provider);
		dbManager.initializeDatabase();

		List<ServiceErrorEntry> serviceErrors = dbManager.getServiceErrorEntries(LocalDate.now());

		if (serviceErrors != null && !serviceErrors.isEmpty()) {
			System.out.println("Found " + serviceErrors.size() + " service errors. Generating notification.");
			generateAndSaveErrorHtmlReport(serviceErrors);
		} else {
			System.out.println("No service errors found.");
		}
	}

	private static void generateAndSaveErrorHtmlReport(List<ServiceErrorEntry> errors) {
		StringBuilder html = new StringBuilder();
		html.append("<html><head><title>서비스 에러 내역</title>");
		html.append("<style>");
		html.append("body { font-family: sans-serif; }");
		html.append("table { border-collapse: collapse; width: 100%; }");
		html.append("th, td { border: 1px solid #dddddd; text-align: left; padding: 8px; }");
		html.append("th { background-color: #f2f2f2; }");
		html.append("</style>");
		html.append("</head><body>");
		html.append("<h2>서비스 에러 내역 - ").append(LocalDate.now()).append("</h2>");
		html.append("<table>");
		html.append("<tr><th>에러 코드</th><th>에러 메시지</th><th>에러 설명</th><th>횟수</th></tr>");

		for (ServiceErrorEntry error : errors) {
			html.append("<tr>");
			html.append("<td>").append(error.getErrorCode()).append("</td>");
			html.append("<td>").append(error.getErrorMsg()).append("</td>");
			html.append("<td>").append(error.getErrorDesc()).append("</td>");
			html.append("<td>").append(error.getCount()).append("</td>");
			html.append("</tr>");
		}

		html.append("</table>");
		html.append("</body></html>");

		String fileName = String.format("%s_서비스에러 내역.html", LocalDate.now());
		try (java.io.PrintWriter writer = new java.io.PrintWriter(fileName, "UTF-8")) {
			writer.println(html.toString());
			System.out.println("Successfully generated error report: " + fileName);
		} catch (java.io.IOException e) {
			System.err.println("Failed to generate error report.");
			e.printStackTrace();
		}
	}
}