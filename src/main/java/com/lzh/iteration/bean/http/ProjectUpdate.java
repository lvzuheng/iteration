package com.lzh.iteration.bean.http;

public class ProjectUpdate extends HttpRequestCode{

	private int ProjectId;
	private int authority;
	private String projectName;

	public int getProjectId() {
		return ProjectId;
	}

	public void setProjectId(int projectId) {
		ProjectId = projectId;
	}

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
