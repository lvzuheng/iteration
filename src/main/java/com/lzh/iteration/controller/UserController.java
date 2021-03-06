package com.lzh.iteration.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.lzh.iteration.bean.code.User;
import com.lzh.iteration.bean.http.HttpRequestCode;
import com.lzh.iteration.bean.http.UserModifyPassword;
import com.lzh.iteration.bean.http.UserPersonInfo;
import com.lzh.iteration.bean.http.UserRegister;
import com.lzh.iteration.service.ProductServices;
import com.lzh.iteration.service.ProjectServices;
import com.lzh.iteration.service.UserServices;
import com.lzh.iteration.utils.ConfigCode;
import com.lzh.iteration.utils.Configure;

@RequestMapping(value = "/user")
@Controller
public class UserController {

	@Autowired
	private UserServices userServices;
	@Autowired
	private ProductServices productServices;
	@Autowired
	private ProjectServices projectServices;
	@Autowired
	private Configure.Config config;

	public UserController(){
		System.out.println("扫描了UserController");
	}

	@RequestMapping(value = "/username",method = RequestMethod.POST,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String username(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		System.out.println("进来了");
		HttpRequestCode httpRequestCode = JSON.parseObject(request.getParameter(ConfigCode.REQUEST),HttpRequestCode.class);
		if(userServices.checkUsername(httpRequestCode.getUserName())){
			return "1";
		}
		return "0";
	}

	@RequestMapping(value = "/login",method = RequestMethod.POST,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String login(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		HttpRequestCode httpRequestCode = JSON.parseObject(request.getParameter(ConfigCode.REQUEST),HttpRequestCode.class);
		if(httpRequestCode != null  && userServices.checkPassword(httpRequestCode.getUserName(), httpRequestCode.getPassWord())){
			return "1";
		}
		return "0";
	}


	@RequestMapping(value = "/info",method = RequestMethod.POST,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String info(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		HttpRequestCode httpRequestCode = JSON.parseObject(request.getParameter(ConfigCode.REQUEST),HttpRequestCode.class);
		if(httpRequestCode != null ){
			User user = userServices.getUserInfo(httpRequestCode.getUserName(), httpRequestCode.getPassWord());
			if(user != null){
				return JSON.toJSONString(user);
			}
		}
		return "0";
	}

	@RequestMapping(value = "/person",method = RequestMethod.POST,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String person(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		HttpRequestCode httpRequestCode = JSON.parseObject(request.getParameter(ConfigCode.REQUEST),HttpRequestCode.class);
		if(httpRequestCode != null ){
			User user = userServices.getUserInfo(httpRequestCode.getUserName(), httpRequestCode.getPassWord());
			if(user != null){
				List<Object> projectIds = projectServices.getProjectId(httpRequestCode.getUserName());
				if(projectIds!=null && projectIds.size()>0){
					int productCounts = productServices.getProductCount(projectIds.toArray());
					UserPersonInfo userPersonInfo = new UserPersonInfo(user.getUsername(), user.getNickname(), projectIds.size(),productCounts,user.getRegisterTime(),user.getAuthority());
					return JSON.toJSONString(userPersonInfo);
				}
			}
		}
		return "0";
	}


	@RequestMapping(value = "/register",method = RequestMethod.POST,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String register(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		UserRegister userRegister = JSON.parseObject(request.getParameter(ConfigCode.REQUEST),UserRegister.class);
		System.err.println(userRegister == null );
		System.err.println(request.getParameter(ConfigCode.REQUEST));
		if(userRegister != null ){
			System.err.println((userRegister.getUserName())+","+(userRegister.getPassword())+","+(userRegister.getNickname() ));
			if(userRegister.getUsername()!= null && userRegister.getPassword() != null && userRegister.getNickname() != null){
				System.err.println(userRegister.getUsername());
				User user = new User(userRegister.getUsername(),userRegister.getPassword(),
						userRegister.getNickname(),0,
						new Date(),null);
				userServices.save(user);
				return JSON.toJSONString(user);
			}
		}
		return "0";
	}



	@RequestMapping(value = "/modifyPassword",method = RequestMethod.POST,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String modifyPassword (HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		UserModifyPassword userModifyPassword = JSON.parseObject(request.getParameter(ConfigCode.REQUEST),UserModifyPassword.class);
		System.out.println(request.getParameter(ConfigCode.REQUEST));
		if(userModifyPassword != null ){
			System.out.println(request.getParameter(ConfigCode.REQUEST));
			User user = userServices.getUserInfo(userModifyPassword.getUserName(), userModifyPassword.getPassWord());
			if(user != null){
				user.setPassword(userModifyPassword.getNewPassword());
				userServices.save(user);
				return "1";
			}
			return "2";
		}
		return "0";
	}

	@RequestMapping(value = "/test",method = RequestMethod.GET,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String test(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		System.out.println("进来了");
		try {
//			return config.getApkAddress();
			return JSON.toJSONString(userServices.getUserInfo("zdm","123456"));
		} catch (Exception e) {
			// TODO: handle exception
			return e.getMessage();
		}
	}

}
