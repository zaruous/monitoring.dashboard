/**
 * 
 */
package org.kyj.fx.monitoring.dashboard.web;

import java.lang.reflect.Method;
import java.util.Set;

import org.reflections.Reflections;

/**
 * 
 */
import io.javalin.Javalin;
import io.javalin.http.Context;

public class RouteScanner {

	public static void scanAndRegister(Javalin app, String basePackage) {
		// 1. 해당 패키지 하위의 모든 클래스 스캔
		Reflections reflections = new Reflections(basePackage);
		Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(WebController.class);
		
		System.out.println("Found " + controllers.size() + " controllers in package: " + basePackage);
		
		for (Class<?> controllerClass : controllers) {
			System.out.println("Registering controller: " + controllerClass.getName());
			try {
				// 2. 컨트롤러 인스턴스 생성 (기본 생성자 필요)
				Object instance = controllerClass.getDeclaredConstructor().newInstance();
				// 3. 클래스 레벨의 Base Path 가져오기
				WebController classAnnotation = controllerClass.getAnnotation(WebController.class);
				String basePath = classAnnotation.path();

				// 4. 메서드 스캔 및 등록
				for (Method method : controllerClass.getDeclaredMethods()) {
					if (method.isAnnotationPresent(HttpGet.class)) {
						String path = basePath + method.getAnnotation(HttpGet.class).value();
						app.get(path, ctx -> invokeMethod(instance, method, ctx));
						System.out.println("[Mapped GET] " + path);
					} else if (method.isAnnotationPresent(HttpPost.class)) {
						String path = basePath + method.getAnnotation(HttpPost.class).value();
						app.post(path, ctx -> invokeMethod(instance, method, ctx));
						System.out.println("[Mapped POST] " + path);
					} else if (method.isAnnotationPresent(HttpPut.class)) {
						String path = basePath + method.getAnnotation(HttpPut.class).value();
						app.put(path, ctx -> invokeMethod(instance, method, ctx));
						System.out.println("[Mapped PUT] " + path);
					} else if (method.isAnnotationPresent(HttpDelete.class)) {
						String path = basePath + method.getAnnotation(HttpDelete.class).value();
						app.delete(path, ctx -> invokeMethod(instance, method, ctx));
						System.out.println("[Mapped DELETE] " + path);
					}
					// 필요시 PUT, DELETE 추가
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("컨트롤러 등록 실패: " + controllerClass.getName());
			}
		}
	}

	// 실제 메서드 실행 로직
	private static void invokeMethod(Object instance, Method method, Context ctx) throws Exception {
		method.setAccessible(true);
		method.invoke(instance, ctx);
	}
}