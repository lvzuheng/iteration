package com.lzh.iteration.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.lzh.iteration.bean.code.Project;
import com.lzh.iteration.bean.http.HttpRequestCode;
import com.lzh.iteration.bean.http.ProjectCreate;
import com.lzh.iteration.bean.http.ProjectRemove;
import com.lzh.iteration.bean.http.ProjectUpdate;
import com.lzh.iteration.service.ProjectServices;
import com.lzh.iteration.service.UserServices;
import com.lzh.iteration.utils.ConfigCode;
import com.lzh.iteration.utils.Configure;

@Controller
@RequestMapping(value = "/project")
public class ProjectController {

	@Autowired
	private Configure.Config config;

	/**项目信息更新 */
	@Autowired
	private ProjectServices projectService;
	@Autowired
	private UserServices userServices;
	
	
	@RequestMapping(value = "/update",method = RequestMethod.POST,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String updateProject(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		ProjectUpdate projectUpdate = JSON.parseObject(request.getParameter(ConfigCode.REQUEST),ProjectUpdate.class);
		System.out.println(request.getParameter(ConfigCode.REQUEST));
		if(projectUpdate != null && userServices.checkPassword(projectUpdate.getUserName(), projectUpdate.getPassWord())){
			try {
				Project project= projectService.getProjectByProjectId(projectUpdate.getProjectId());
				if(project != null){
					project.setAuthority(projectUpdate.getAuthority());
					project.setProjectname(projectUpdate.getProjectName());
					projectService.save(project);
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
		if(projectRemove != null && userServices.checkPassword(projectRemove.getUserName(), projectRemove.getPassWord())){
			try {
				Project project= projectService.getProjectByProjectId(projectRemove.getProjectId());
				if(project != null){
					File dir = new File(config.getApkAddress()+"/"+project.getId());
					if(dir!=null && dir.isDirectory()){
						for(File file: dir.listFiles()){
							file.delete();
						}
						dir.delete();
					}
					
					projectService.remove(project);
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
		if(projectCreate != null && userServices.checkPassword(projectCreate.getUserName(), projectCreate.getPassWord())){
			try {
				List<Project> pList = projectService.getProject(projectCreate.getUserName());
				if(pList != null && pList.size()>0){
					for(Project project: pList){
						if(project.getProjectname().equals(projectCreate.getProjectName())){
							return "0";
						}
					}
				}
				Project insertProject = new Project(projectCreate.getProjectName(),projectCreate.getUserName(),projectCreate.getAuthority());
				projectService.save(insertProject);
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
		if(httpRequestCode != null && userServices.checkPassword(httpRequestCode.getUserName(), httpRequestCode.getPassWord())){
			List<Project> pList = projectService.getProject(httpRequestCode.getUserName());
			if(pList != null){
				System.out.println( JSON.toJSONString(pList));
				return JSON.toJSONString(pList);
			}
		}
		return "0";
	}

}
