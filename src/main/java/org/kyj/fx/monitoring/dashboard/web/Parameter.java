/**
 * 
 */
package org.kyj.fx.monitoring.dashboard.web;

import java.util.Map;

/**
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class Parameter {

	private Map<String, Object> pathParamMap;
	public int page = 0;
	private int pageSize = 10;

	public Parameter() {
		pathParamMap = new java.util.HashMap<>();
	}

	public Parameter(Map pathParamMap) {
		pathParamMap.putAll(pathParamMap);
	}

	public Map<String, Object> getPathParamMap() {
		return pathParamMap;
	}

	public void setPathParamMap(Map<String, Object> pathParamMap) {
		this.pathParamMap = pathParamMap;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public void put(String key, Object value) {
		this.pathParamMap.put(key,  value);
	}
	
}
