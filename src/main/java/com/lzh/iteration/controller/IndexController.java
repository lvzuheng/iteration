package com.lzh.iteration.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lzh.iteration.utils.Configure;

@Controller
public class IndexController {
	
	@Autowired
	private Configure.Config config;
	
	@RequestMapping(value = "/iteration",produces="text/plain;charset=UTF-8")
	public ModelAndView username(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		
		ModelAndView modelAndView = new ModelAndView("redirect:"+config.getIndexPath().trim());
		return modelAndView;
	}
}
