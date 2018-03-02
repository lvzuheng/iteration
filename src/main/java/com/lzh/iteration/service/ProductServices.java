package com.lzh.iteration.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lzh.iteration.bean.code.Product;

@Service
public class ProductServices extends SqlServices{

	public List<Product> getProduct(int projectId,String name){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("projectId", projectId);
		map.put("product_name", name);
		return sqlDataManager.search(map,Product.class);
	}
	public List<Product> getProductByProjectId(int projectId){
		return sqlDataManager.search("projectId",projectId,Product.class);
	}
	public List<Product> getProductByProjectId(Collection<Integer> projectIds){
		return sqlDataManager.searchSqlList("projectId",projectIds,Product.class);
	}
	public Product getProduct(int ProductId){
		List<Product> pList = sqlDataManager.search("id",ProductId,Product.class);
		if(pList != null && pList.size()>0){
			return pList.get(0);
		}
		return null;
	}
	public List<Product> getProduct(Collection<Integer> ProductId){
		return sqlDataManager.searchSqlList("id",ProductId,Product.class);
	}
	public Product getProduct(String conditionKey){
		List<Product> pList = sqlDataManager.search("condition",conditionKey,Product.class);
		if(pList!= null && pList.size()>0){
			return pList.get(0);
		}
		return null;
	}
}
