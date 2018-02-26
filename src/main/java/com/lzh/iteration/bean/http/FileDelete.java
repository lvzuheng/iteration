package com.lzh.iteration.bean.http;

import java.io.File;
import java.util.Set;

public class FileDelete extends HttpRequestCode{

	private Set<Integer> productId;
	private boolean isDelete;
	
	public FileDelete() {
		// TODO Auto-generated constructor stub
	}
	


	public boolean isDelete() {
		return isDelete;
	}


	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	public Set<Integer> getProductId() {
		return productId;
	}
	public void setProductId(Set<Integer> productId) {
		this.productId = productId;
	}


}
