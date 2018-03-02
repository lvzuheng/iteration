package com.lzh.iteration.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzh.iteration.bean.code.Product;
import com.lzh.iteration.bean.code.Project;
import com.lzh.iteration.bean.code.User;
import com.lzh.iteration.bean.http.FileInfo;
import com.lzh.iteration.bean.http.productList;
import com.lzh.iteration.bean.http.HttpRequestCode;
import com.lzh.iteration.bean.http.ProductInfo;
import com.lzh.iteration.bean.http.UserRegister;
import com.lzh.iteration.service.ProjectServices;
import com.lzh.iteration.service.UserServices;
import com.lzh.iteration.utils.ApkUtils;
import com.lzh.iteration.utils.ConfigCode;

import io.netty.handler.codec.json.JsonObjectDecoder;

@Controller
@RequestMapping(value = "/user")
public class UserController {

	@Resource
	private UserServices userServices;

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


	@RequestMapping(value = "/regiest",method = RequestMethod.POST,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String regiest(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		UserRegister userRegister = JSON.parseObject(request.getParameter(ConfigCode.REQUEST),UserRegister.class);
		if(userRegister != null ){
			if(userRegister.getUsername()!= null && userRegister.getPassword() != null && userRegister.getNickname() != null){
				User user = new User(userRegister.getUsername(),userRegister.getPassword(),
						userRegister.getNickname(),userRegister.getAuthority(),
						new Date(),null);
				userServices.save(user);
				return JSON.toJSONString(user);
			}
		}
		return "0";
	}

}
