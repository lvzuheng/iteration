package com.lzh.iteration.bean.http;

public class ProjectCreate extends HttpRequestCode{

	private String projectName;
	private int authority;
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public int getAuthority() {
		return authority;
	}
	public void setAuthority(int authority) {
		this.authority = authority;
	}
}
