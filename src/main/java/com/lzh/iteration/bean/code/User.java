package com.lzh.iteration.bean.code;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="user")
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="username")
	private String username;

	@Column(name="password")
	private String password;

	@Column(name="register_time")
	@Temporal(TemporalType.DATE)
	private Date registerTime;
	
	@Column(name="last_login")
	private Date lastLogin;
	
	@Column(name="nickname")
	private String nickname;

	@Column(name="authority")
	private int authority;
	
	public User(String username,String password,String nickname,int authority,Date registerTime,Date lastLogin){
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.authority = authority;
		this.registerTime = registerTime;
		this.lastLogin = lastLogin;
	}
	
	public User(){}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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
