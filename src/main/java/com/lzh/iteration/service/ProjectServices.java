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
public class ProjectServices extends SqlServices{

	
	
	
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
	public List<Object> getProjectId(String username){
		List<Object> projectIds = sqlDataManager.search("SELECT id FROM Project WHERE owner = '"+username+"'");
		if(projectIds!= null && projectIds.size()>0){
			return projectIds;
		}
		return null;
	}

	public int getProjectctCount(String userName){
		return sqlDataManager.searchCount("owner",userName,Project.class);
	}
	
}
