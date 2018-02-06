package com.lzh.iteration.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.lzh.iteration.bean.http.FileList;
import com.lzh.iteration.bean.http.HttpRequestCode;
import com.lzh.iteration.bean.http.ProductInfo;
import com.lzh.iteration.service.FileAction;
import com.lzh.iteration.utils.ApkUtils;
import com.lzh.iteration.utils.ConfigCode;

@Controller
@RequestMapping(value = "/user")
public class UserController {

	@Resource
	private FileAction userAction;

	@RequestMapping(value = "/username",method = RequestMethod.POST,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String username(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		System.out.println("进来了");
		HttpRequestCode httpRequestCode = JSON.parseObject(request.getParameter(ConfigCode.REQUEST),HttpRequestCode.class);
		if(userAction.checkUsername(httpRequestCode.getUserName())){
			return "1";
		}
		return "0";
	}

	@RequestMapping(value = "/login",method = RequestMethod.POST,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String login(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		HttpRequestCode httpRequestCode = JSON.parseObject(request.getParameter(ConfigCode.REQUEST),HttpRequestCode.class);
		if(httpRequestCode != null && userAction.checkPassword(httpRequestCode.getUserName(), httpRequestCode.getPassWord())){
			return "1";
		}
		return "0";
	}
	
}
