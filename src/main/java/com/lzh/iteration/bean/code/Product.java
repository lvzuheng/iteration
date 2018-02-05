package com.lzh.iteration.bean.code;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.sql.Update;

@Entity
@Table(name="product")
@DynamicInsert(true)  
@DynamicUpdate(true) 
@SelectBeforeUpdate(true)
public class Product {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="product_name")
	private String productname;
	@Column(name="condition_key")
	private String condition;
	@Column(name="product_authority")
	private int authority;
	@Column(name="packname")
	private String packname;
	@Column(name="uploadtime")
	private Date uploadtime;
	@Column(name="packagesize")
	private String packagesize;
	@Column(name="version_name")
	private String versionname;
	@Column(name="version_code")
	private String versioncode;
	@Column(name="projectId")
	private int projectId;
	
	public Product(String productname,int projectId,String condition,int authority,String packname,Date uploadtime,String packagesize,String versionname,String versioncode){
		this.productname = productname;
		this.condition = condition;
		this.setAuthority(authority);
		this.packname = packname;
		this.packagesize = packagesize;
		this.versionname = versionname;
		this.versioncode = versioncode;
		this.projectId = projectId;
		this.uploadtime = uploadtime;
	}
	public Product(){}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProductname() {
		return productname;
	}
	public void setProductname(String productname) {
		this.productname = productname;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getPackname() {
		return packname;
	}
	public void setPackname(String packname) {
		this.packname = packname;
	}
	public String getPackagesize() {
		return packagesize;
	}
	public void setPackagesize(String packagesize) {
		this.packagesize = packagesize;
	}
	public String getVersionname() {
		return versionname;
	}
	public void setVersionname(String versionname) {
		this.versionname = versionname;
	}
	public String getVersioncode() {
		return versioncode;
	}
	public void setVersioncode(String versioncode) {
		this.versioncode = versioncode;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public Date getUploadtime() {
		return uploadtime;
	}
	public void setUploadtime(Date uploadtime) {
		this.uploadtime = uploadtime;
	}
	public int getAuthority() {
		return authority;
	}
	public void setAuthority(int authority) {
		this.authority = authority;
	}
}
