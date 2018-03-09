package com.lzh.iteration.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lzh.iteration.bean.code.User;

@Service
public class SqlServices {


	@Resource
	protected SqlDataManager sqlDataManager;

	public void saveOrUpdate(Object obj){
		sqlDataManager.saveOrUpdate(obj);
	}
	public void save(Object obj){
		sqlDataManager.save(obj);
	}
	public void merge(Object obj){
		sqlDataManager.merge(obj);
	}
	public void update(Object obj){
		sqlDataManager.update(obj);
	}
	public void remove(Object obj){
		sqlDataManager.remove(obj);
	}
	



	public  boolean checkUsername(String username){
		if(username!= null ){
			List<Object> list = sqlDataManager.search("SELECT username FROM User WHERE userName = '"+username+"'");
			try {
				if(list !=null && username.equals(list.get(0))){
					return true;
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
			List<Object> list = sqlDataManager.search("SELECT password FROM User WHERE userName = '"+username+"'");
			try {
				if(list!=null && password.equals(list.get(0))){
					return true;
				}		
			} catch (Exception e) {
				// TODO: handle exception
				return false;
			}
		}
		return false;
	}
	
	public <T> int count(String key,String value,Class<T> t){
		return sqlDataManager.searchCount(key, value, t);
	}
}
