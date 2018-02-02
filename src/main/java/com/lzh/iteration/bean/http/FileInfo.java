package com.lzh.iteration.bean.http;

public class FileInfo {
	private String fileName;
	private String packageName;
	private String createTime;
	private String fileSize;
	private String version;
	
	public FileInfo(){}
	
	public FileInfo(String fileName,String packagename,String version,String createTime,String fileSize){
		this.fileName = fileName;
		this.packageName = packagename;
		this.version = version;
		this.createTime = createTime;
		this.fileSize = fileSize;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
