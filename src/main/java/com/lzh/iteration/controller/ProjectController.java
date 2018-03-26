package com.lzh.iteration.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.wiring.BeanWiringInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzh.iteration.bean.code.Product;
import com.lzh.iteration.bean.code.Project;
import com.lzh.iteration.bean.http.ProductDelete;
import com.lzh.iteration.bean.http.FileInfo;
import com.lzh.iteration.bean.http.productList;
import com.lzh.iteration.bean.http.HttpRequestCode;
import com.lzh.iteration.bean.http.IterationInfo;
import com.lzh.iteration.bean.http.IterationRequest;
import com.lzh.iteration.bean.http.ProductCreate;
import com.lzh.iteration.bean.http.ProductInfo;
import com.lzh.iteration.bean.http.ProductUpdate;
import com.lzh.iteration.bean.http.ProjectCreate;
import com.lzh.iteration.bean.http.ProjectRemove;
import com.lzh.iteration.bean.http.ProjectUpdate;
import com.lzh.iteration.service.ProjectServices;
import com.lzh.iteration.utils.ApkUtils;
import com.lzh.iteration.utils.ConfigCode;
import com.lzh.iteration.utils.Configure;
import com.lzh.iteration.utils.FileUtils;
import com.lzh.iteration.utils.MD5Utils;
import com.lzh.iteration.utils.StreamWriter;

@Controller
@RequestMapping(value = "/project")
public class ProjectController {

	private String address = Configure.getConfig().getApkAddress();

	/**项目信息更新 */
	@Resource
	private ProjectServices projectServices;
	@RequestMapping(value = "/update",method = RequestMethod.POST,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String updateProject(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		ProjectUpdate projectUpdate = JSON.parseObject(request.getParameter(ConfigCode.REQUEST),ProjectUpdate.class);
		System.out.println(request.getParameter(ConfigCode.REQUEST));
		if(projectUpdate != null && projectServices.checkPassword(projectUpdate.getUserName(), projectUpdate.getPassWord())){
			try {
				Project project= projectServices.getProjectByProjectId(projectUpdate.getProjectId());
				if(project != null){
					project.setAuthority(projectUpdate.getAuthority());
					project.setProjectname(projectUpdate.getProjectName());
					projectServices.update(project);
					return JSON.toJSONString(project);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return "0";
			}
		}
		return "0";
	}
	
	
	/**项目移除 */
	@RequestMapping(value = "/remove",method = RequestMethod.POST,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String removeProject(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		ProjectRemove projectRemove = JSON.parseObject(request.getParameter(ConfigCode.REQUEST),ProjectRemove.class);
		if(projectRemove != null && projectServices.checkPassword(projectRemove.getUserName(), projectRemove.getPassWord())){
			try {
				Project project= projectServices.getProjectByProjectId(projectRemove.getProjectId());
				if(project != null){
					File dir = new File(address+"/"+project.getId());
					if(dir!=null && dir.isDirectory()){
						for(File file: dir.listFiles()){
							file.delete();
						}
						dir.delete();
					}
					
					projectServices.remove(project);
					return "1";
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return "0";
			}
		}
		return "0";
	}

	/**项目创建 */
	@RequestMapping(value = "/create",method = RequestMethod.POST,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String createProject(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		ProjectCreate projectCreate = JSON.parseObject(request.getParameter(ConfigCode.REQUEST),ProjectCreate.class);
		if(projectCreate != null && projectServices.checkPassword(projectCreate.getUserName(), projectCreate.getPassWord())){
			try {
				List<Project> pList = projectServices.getProject(projectCreate.getUserName());
				if(pList != null && pList.size()>0){
					for(Project project: pList){
							
						if(project.getProjectname().equals(projectCreate.getProjectName())){
							return "0";
						}
					}
				}
				Project insertProject = new Project(projectCreate.getProjectName(),projectCreate.getUserName(),projectCreate.getAuthority());
				projectServices.save(insertProject);
				return "1";
			} catch (Exception e) {
				// TODO: handle exception
				return "0";
			}
		}
		return "0";
	}
	/**项目信息 */
	@RequestMapping(value = "/info",method = RequestMethod.POST,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String Projectinfo(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		HttpRequestCode httpRequestCode = JSON.parseObject(request.getParameter(ConfigCode.REQUEST),HttpRequestCode.class);
		if(httpRequestCode != null && projectServices.checkPassword(httpRequestCode.getUserName(), httpRequestCode.getPassWord())){
			List<Project> pList = projectServices.getProject(httpRequestCode.getUserName());
			if(pList != null){
				System.out.println( JSON.toJSONString(pList));
				return JSON.toJSONString(pList);
			}
		}
		return "0";
	}


	private boolean check(String userName,String passWord){
		return projectServices.checkPassword(userName, passWord);
	}

}
