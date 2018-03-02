package com.lzh.iteration.bean.http;

import java.util.Date;


public class UserRegister extends HttpRequestCode{

	private String username;
	private String password;
	private  Date registerTime;
	private Date lastLogin;
	private String nickname;
	private int authority;
	
	
	public UserRegister(String username,String password,String nickname,int authority,Date registerTime,Date lastLogin){
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.registerTime = registerTime;
		this.lastLogin = lastLogin;
		this.authority = authority;
	}
	
	public UserRegister(){}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}
	public Date getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getAuthority() {
		return authority;
	}

	public void setAuthority(int authority) {
		this.authority = authority;
	}
}
