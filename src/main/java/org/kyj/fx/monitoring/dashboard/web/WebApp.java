package org.kyj.fx.monitoring.dashboard.web;

import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.kyj.fx.monitoring.dashboard.AkcDataProvider;
import org.kyj.fx.monitoring.dashboard.DataProvider;
import org.kyj.fx.monitoring.dashboard.INF_STATUS;
import org.kyj.fx.monitoring.dashboard.InterfaceMonitoringDashboardApp;
import org.kyj.fx.monitoring.dashboard.MockDataProvider;
import org.kyj.fx.monitoring.dashboard.PropertiesUtil;
import org.kyj.fx.monitoring.dashboard.ServiceErrorEntry;
import org.kyj.fx.monitoring.dashboard.ServiceErrorLog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class WebApp {
    public static void main(String[] args) {
        
    	Properties orLoad = PropertiesUtil.createOrLoad(InterfaceMonitoringDashboardApp.class, ()->{
			return PropertiesUtil.of(Map.of("provider.class.name", "org.kyj.fx.monitoring.dashboard.MockDataProvider"));
		});
		String clazz = orLoad.getProperty("provider.class.name", "org.kyj.fx.monitoring.dashboard.MockDataProvider");
		DataProvider d = new MockDataProvider();
		try {
			Class<?> forName = Class.forName(clazz);
			Constructor<?> constructor = forName.getConstructor();
			d = (DataProvider) constructor.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		DataProvider dataProvider = d;
        ObjectMapper mapper = new ObjectMapper();

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("public", Location.EXTERNAL);
        }).start(10024);

        System.out.println("Javalin server is running on port 10024.");
        System.out.println("Check out http://localhost:10024/");

        // API endpoint to provide overall status data
        app.get("/api/status/overall", ctx -> {
            LocalDate today = LocalDate.now();
            int successCount = dataProvider.getInterfaceStatusDetails(today, INF_STATUS.SUCCESS).size();
            int failCount = dataProvider.getInterfaceStatusDetails(today, INF_STATUS.FAIL).size();
            int inProgressCount = dataProvider.getInterfaceStatusDetails(today, INF_STATUS.IN_PROGRESS).size();
            int retryCount = dataProvider.getInterfaceStatusDetails(today, INF_STATUS.RETRY).size();
            int totalCount = successCount + failCount + inProgressCount + retryCount;

            ObjectNode status = mapper.createObjectNode();
            status.put("totalInterfaces", totalCount);
            status.put("success", successCount);
            status.put("error", failCount);
            // Assuming 'unused' can be represented by 'in_progress' or another status for now
            status.put("retray", retryCount); 
            ctx.json(status);
        });

        // API endpoint for data fluctuation chart
        app.get("/api/data/fluctuation", ctx -> {
            // TODO: AkcDataProvider.getTableFluctuations() is not implemented. Using mock data.
            ObjectNode data = mapper.createObjectNode();
            data.putArray("labels").add("10:00").add("10:05").add("10:10").add("10:15").add("10:20").add("10:25");
            data.putArray("values").add(120).add(132).add(101).add(134).add(90).add(110);
            ctx.json(data);
        });

        // API endpoint for service error logs
        app.get("/api/errors", ctx -> {
            List<ServiceErrorEntry> entries = dataProvider.getServiceErrorEntries(LocalDate.now());
            
            ObjectNode response = mapper.createObjectNode();
            ArrayNode data = response.putArray("data");

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            for (ServiceErrorEntry entry : entries) {
                ObjectNode node = mapper.createObjectNode();
                node.put("id", entry.getErrorId());
                node.put("service", entry.getErrorCode());
                node.put("message", entry.getErrorMsg());
                node.put("errorDesc", entry.getErrorDesc());
                node.put("timestamp", entry.getRegDate() != null ? entry.getRegDate().format(timeFormatter) : "N/A");
                data.add(node);
            }
            ctx.json(response);
        });

        // API endpoint for a single service error log detail
        app.get("/api/error-log/{id}", ctx -> {
            String errorId = ctx.pathParam("id");
            ServiceErrorLog errorLog = ((AkcDataProvider) dataProvider).getServiceErrorLog(errorId);
            ctx.json(errorLog);
        });

        // API endpoint for schedule logs
        app.get("/api/schedules", ctx -> {
            ObjectNode response = mapper.createObjectNode();
            response.putPOJO("data", dataProvider.getScheduleEntries(LocalDate.now()));
            ctx.json(response);
        });
    }
}
