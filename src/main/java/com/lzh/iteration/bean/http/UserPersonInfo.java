package com.lzh.iteration.bean.http;

import java.util.Date;

public class UserPersonInfo {
	
	private String username;
	private String nickname;
	private Date registetime;
	private int authority;
	private int projectCount;
	private int productCount;
	
	public UserPersonInfo(String username,String nickname,int projectCount,int productCount,Date registetime,int authority){
		this.username = username;
		this.nickname = nickname;
		this.projectCount = projectCount;
		this.productCount = productCount;
		this.registetime  = registetime;
		this.authority = authority;
	}
	
	public UserPersonInfo(){}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getProjectCount() {
		return projectCount;
	}

	public void setProjectCount(int projectCount) {
		this.projectCount = projectCount;
	}

	public int getProductCount() {
		return productCount;
	}

	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}

	public Date getRegistetime() {
		return registetime;
	}

	public void setRegistetime(Date registetime) {
		this.registetime = registetime;
	}

	public int getAuthority() {
		return authority;
	}

	public void setAuthority(int authority) {
		this.authority = authority;
	}
}
