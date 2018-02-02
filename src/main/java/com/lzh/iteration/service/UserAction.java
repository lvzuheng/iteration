package com.lzh.iteration.service;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lzh.iteration.bean.code.Product;
import com.lzh.iteration.bean.code.Project;
import com.lzh.iteration.bean.code.User;
import com.lzh.iteration.bean.http.ProductInfo;


@Service
public class UserAction {

	@Resource
	private   SqlDataManager sqlDataManager;
	
	public  boolean checkUsername(String username){
		if(username!= null ){
			List<Object> list = sqlDataManager.search("FROM User WHERE userName = '"+username+"'");
			try {
				if(list!=null){
					if(list !=null && username.equals(((User)list.get(0)).getUsername())){
						return true;
					}		
				}
			} catch (Exception e) {
				// TODO: handle exception
				return false;
			}
		}
		return false;
	}

	public  boolean checkPassword(String username,String password){
		if(username!= null && password != null){
			List<Object> list = sqlDataManager.search("FROM User WHERE userName = '"+username+"'");
			try {
				if(list!=null){
					if(password.equals(((User)list.get(0)).getPassword())){
						return true;
					}		
				}
			} catch (Exception e) {
				// TODO: handle exception
				return false;
			}
		}
		return false;
	}
	
	public List<Project> getProject(String username){
		System.out.println("username:"+username);
		return sqlDataManager.search("owner",username,Project.class);
	}
	public List<Product> getProduct(int ProductId){
		return sqlDataManager.search("id",ProductId,Product.class);
	}
	public List<Product> getProduct(Collection<Integer> ProductId){
		return sqlDataManager.searchSqlList("id",ProductId,Product.class);
	}
	
	public void save(Object obj){
		sqlDataManager.save(obj);
	}
	public void remove(Object obj){
		sqlDataManager.remove(obj);
	}
}
