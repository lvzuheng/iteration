package com.lzh.iteration.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzh.iteration.bean.code.Product;
import com.lzh.iteration.bean.http.IterationInfo;
import com.lzh.iteration.bean.http.IterationRequest;
import com.lzh.iteration.service.ProductServices;
import com.lzh.iteration.service.ProjectServices;
import com.lzh.iteration.utils.Configure;


@Controller
@RequestMapping(value = "/iteration")
public class IterationController {
	
	private String address = Configure.getConfig().getApkAddress();
	@Resource
	private ProductServices productServices;
	
	/**迭代信息查询 */
	@RequestMapping("/iterationInfo")
	@ResponseBody
	public String iterationInfo(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		IterationRequest iterationRequest = JSONObject.parseObject(request.getParameter("request"), IterationRequest.class);
		System.out.println("进来了");
		System.out.println(request.getParameter("request")+","+(iterationRequest!=null));
		if(iterationRequest!=null){
			Product product = productServices.getProduct(iterationRequest.getConditionKey());
			IterationInfo iterationInfo;
			if(product != null){
				String url ="http://"+request.getLocalAddr()+":"+request.getLocalPort()+request.getContextPath()+"/file/iterationDownload";
				iterationInfo = new IterationInfo(product.getPackname(),product.getFileName(),
						product.getVersionname(),product.getVersioncode(),product.getUploadtime(),product.getPackagesize(),url,true);
			}
			else {
				iterationInfo =  new IterationInfo(null,null,null,null,null,null,null,false);
			}
			return JSON.toJSONString(iterationInfo);
		}
		return "";
	}
	
	/**文件下载接口 */
	@RequestMapping(value = "/iterationDownload",produces="text/plain;charset=UTF-8")
	@ResponseBody
	public void download(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("来访者："+request.getRemoteAddr());
		IterationRequest iterationRequest = JSONObject.parseObject(request.getParameter("request"), IterationRequest.class);
		if(iterationRequest!= null){
			Product product = productServices.getProduct(iterationRequest.getConditionKey());
			File file = new File(address+"/"+product.getProjectId()+"/"+product.getProductname());
			String filenames = file.getName();
			InputStream inputStream;
			try {
				inputStream = new BufferedInputStream(new FileInputStream(file));
				byte[] buffer = new byte[inputStream.available()];
				inputStream.read(buffer);
				inputStream.close();
				response.reset();
				// 先去掉文件名称中的空格,然后转换编码格式为utf-8,保证不出现乱码,这个文件名称用于浏览器的下载框中自动显示的文件名
				response.addHeader("Content-Disposition", "attachment;filename=" + new String(filenames.replaceAll(" ", "").getBytes("utf-8"), "iso8859-1"));
				response.addHeader("Content-Length", "" + file.length());
				OutputStream os = new BufferedOutputStream(response.getOutputStream());
				response.setContentType("application/octet-stream");
				os.write(buffer);// 输出文件
				os.flush();
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
