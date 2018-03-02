package com.lzh.iteration.bean.http;

import java.io.File;
import java.util.Set;

public class ProductDelete extends HttpRequestCode{

	private Set<Integer> productId;
	private boolean isDelete;
	
	public ProductDelete() {
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
