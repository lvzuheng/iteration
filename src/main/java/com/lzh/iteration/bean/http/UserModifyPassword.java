package com.lzh.iteration.bean.http;

public class UserModifyPassword extends HttpRequestCode{

	private String newPassword;

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
