/**
 * 
 */
package org.kyj.fx.monitoring.dashboard.web.custom.mock.perform;

import org.kyj.fx.monitoring.dashboard.web.HttpGet;
import org.kyj.fx.monitoring.dashboard.web.WebController;

import io.javalin.http.Context;

/**
 * 
 */
@WebController
public class MockPerformController {

	@HttpGet("/status")
	public void getStatus(Context ctx) {
		System.out.println("MockPerformController.getStatus() called");
		ctx.result("Mock Data OK");
	}
}
