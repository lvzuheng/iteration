package com.lzh.iteration.bean.http;

import java.util.Date;

public class IterationInfo {

	private String packName;
	private String fileName;
	private String versionName;
	private String versionCode;
	private Date updateTime;
	private String packSize;
	private String Url;
	private boolean status;
	
	public IterationInfo(){
		
	}
	public IterationInfo(String packName,String fileName,String versionName,String versionCode,Date updateTime,String packSize,String Url,boolean status){
		this.packName = packName;
		this.fileName = fileName;
		this.versionCode = versionCode;
		this.versionName= versionName;
		this.setUpdateTime(updateTime);
		this.packSize = packSize;
		this.setUrl(Url);
		this.status = status;
	}
	
	public String getPackName() {
		return packName;
	}
	public void setPackName(String packName) {
		this.packName = packName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public String getPackSize() {
		return packSize;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}
}
