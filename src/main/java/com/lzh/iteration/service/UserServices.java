package com.lzh.iteration.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lzh.iteration.bean.code.Product;
import com.lzh.iteration.bean.code.User;

@Service
public class UserServices extends SqlServices{

	public User getUserInfo(String username,String password){
		if(username!= null && password != null){
			System.out.println(username);
			List<User> list = sqlDataManager.search("username", username, User.class);
			try {
				if(list !=null && password.equals(list.get(0).getPassword())){
					return list.get(0);
				}		
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}
		}
		return null;
	}
	
	public int getProjectCount(String username){
		return sqlDataManager.searchCount("owner",username,User.class);
	}
	public int getProductCount(Integer[] projectId){
		return sqlDataManager.searchCountbyList("projectId",projectId,Product.class);
	}
}
