package com.lzh.iteration.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.lzh.iteration.bean.http.HttpRequestCode;
import com.lzh.iteration.utils.ConfigCode;

@Controller
public class IndexController {
	
	@RequestMapping(value = "*",produces="text/plain;charset=UTF-8")
	public ModelAndView username(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		ModelAndView modelAndView = new ModelAndView("redirect:http://127.0.0.1:8020/IterationSystem/Login.html");
		return modelAndView;
	}
}
