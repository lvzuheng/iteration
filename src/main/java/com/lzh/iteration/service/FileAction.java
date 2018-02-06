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
public class FileAction {

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
	
	public Project getProjectByProjectId(int projectId){
		List<Project> projects = sqlDataManager.search("id",projectId,Project.class);
		if(projects!= null && projects.size()>0){
			return projects.get(0);
		}
		return null;
	}
	public List<Product> getProductByProjectId(int projectId){
		return sqlDataManager.search("projectId",projectId,Product.class);
	}
	public List<Product> getProductByProjectId(Collection<Integer> projectIds){
		return sqlDataManager.searchSqlList("projectId",projectIds,Product.class);
	}
	public List<Product> getProduct(int ProductId){
		return sqlDataManager.search("id",ProductId,Product.class);
	}
	public List<Product> getProduct(Collection<Integer> ProductId){
		return sqlDataManager.searchSqlList("id",ProductId,Product.class);
	}
	
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
}
