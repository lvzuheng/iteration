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
import com.lzh.iteration.bean.http.FileDelete;
import com.lzh.iteration.bean.http.FileInfo;
import com.lzh.iteration.bean.http.FileList;
import com.lzh.iteration.bean.http.HttpRequestCode;
import com.lzh.iteration.bean.http.IterationInfo;
import com.lzh.iteration.bean.http.IterationRequest;
import com.lzh.iteration.bean.http.ProductCreate;
import com.lzh.iteration.bean.http.ProductInfo;
import com.lzh.iteration.bean.http.ProductUpdate;
import com.lzh.iteration.bean.http.ProjectCreate;
import com.lzh.iteration.bean.http.ProjectRemove;
import com.lzh.iteration.bean.http.ProjectUpdate;
import com.lzh.iteration.service.FileAction;
import com.lzh.iteration.utils.ApkUtils;
import com.lzh.iteration.utils.ConfigCode;
import com.lzh.iteration.utils.Configure;
import com.lzh.iteration.utils.FileUtils;
import com.lzh.iteration.utils.MD5Utils;
import com.lzh.iteration.utils.StreamWriter;

@Controller
@RequestMapping(value = "/file")
public class FileController {

	private String address = Configure.getConfig().getApkAddress();

	@Resource
	private FileAction fileAction;
	@RequestMapping(value = "/updateProject",method = RequestMethod.POST,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String updateProject(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		ProjectUpdate projectUpdate = JSON.parseObject(request.getParameter(ConfigCode.REQUEST),ProjectUpdate.class);
		System.out.println(request.getParameter(ConfigCode.REQUEST));
		if(projectUpdate != null && fileAction.checkPassword(projectUpdate.getUserName(), projectUpdate.getPassWord())){
			try {
				Project project= fileAction.getProjectByProjectId(projectUpdate.getProjectId());
				if(project != null){
					project.setAuthority(projectUpdate.getAuthority());
					project.setProjectname(projectUpdate.getProjectName());
					fileAction.update(project);
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
	@RequestMapping(value = "/removeProject",method = RequestMethod.POST,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String removeProject(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		ProjectRemove projectRemove = JSON.parseObject(request.getParameter(ConfigCode.REQUEST),ProjectRemove.class);
		if(projectRemove != null && fileAction.checkPassword(projectRemove.getUserName(), projectRemove.getPassWord())){
			try {
				Project project= fileAction.getProjectByProjectId(projectRemove.getProjectId());
				if(project != null){
					fileAction.remove(project);
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


	@RequestMapping(value = "/createProject",method = RequestMethod.POST,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String createProject(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		ProjectCreate projectCreate = JSON.parseObject(request.getParameter(ConfigCode.REQUEST),ProjectCreate.class);
		if(projectCreate != null && fileAction.checkPassword(projectCreate.getUserName(), projectCreate.getPassWord())){
			try {
				List<Project> pList = fileAction.getProject(projectCreate.getUserName());
				if(pList != null && pList.size()>0){
					for(Project project: pList){
						
						if(project.getProjectname().equals(projectCreate.getProjectName())){
							return "0";
						}
					}
				}
				Project insertProject = new Project(projectCreate.getProjectName(),projectCreate.getUserName(),projectCreate.getAuthority());
				fileAction.save(insertProject);
				return "1";
			} catch (Exception e) {
				// TODO: handle exception
				return "0";
			}
		}
		return "0";
	}

	@RequestMapping(value = "/Projectinfo",method = RequestMethod.POST,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String Projectinfo(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		HttpRequestCode httpRequestCode = JSON.parseObject(request.getParameter(ConfigCode.REQUEST),HttpRequestCode.class);
		if(httpRequestCode != null && fileAction.checkPassword(httpRequestCode.getUserName(), httpRequestCode.getPassWord())){
			List<Project> pList = fileAction.getProject(httpRequestCode.getUserName());
			if(pList != null){
				System.out.println( JSON.toJSONString(pList));
				return JSON.toJSONString(pList);
			}
		}
		return "0";
	}

	@RequestMapping(value = "/createProduct",method = RequestMethod.POST,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String createProduct(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		ProductCreate productCreate = JSON.parseObject(request.getParameter(ConfigCode.REQUEST),ProductCreate.class);
		if(productCreate != null && fileAction.checkPassword(productCreate.getUserName(), productCreate.getPassWord())){
			try {
				Project project  = fileAction.getProjectByProjectId(productCreate.getProjectId());
				if(project!=null){
					String conditionKey = MD5Utils.getMD5(productCreate.getProductName()+new Date().getTime());
					Product product = new Product(productCreate.getProductName(),null,productCreate.getProjectId(),conditionKey,0,
							null,new Date(),null,null,null);
					fileAction.save(product);
				}
				return "1";
			} catch (Exception e) {
				// TODO: handle exception
				return "0";
			}
		}
		return "0";
	}

	@RequestMapping(value = "/Productinfo",method = RequestMethod.POST,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String Productinfo(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		System.out.println(request.getParameter(ConfigCode.REQUEST));
		ProductInfo ProductInfo = JSON.parseObject(request.getParameter(ConfigCode.REQUEST),ProductInfo.class);
		if(ProductInfo != null && fileAction.checkPassword(ProductInfo.getUserName(), ProductInfo.getPassWord())){
			List<Product> pList = fileAction.getProductByProjectId(ProductInfo.getProjectId());
			if(pList != null){
				System.out.println(ProductInfo.getProjectId() +","+JSON.toJSONString(pList));
				return JSON.toJSONString(pList);
			}
		}
		return "0";
	}


	@RequestMapping(value = "/updateProduct",method = RequestMethod.POST)
	@ResponseBody
	public String updateProduct(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		ProductUpdate productUpdate = JSON.parseObject(request.getParameter(ConfigCode.REQUEST),ProductUpdate.class);
		if(check(productUpdate.getUserName(), productUpdate.getPassWord())){
			System.out.println("fd：上传中");
			if(productUpdate.getProductName().substring(productUpdate.getProductName().lastIndexOf("."), productUpdate.getProductName().length()).equals(".apk")){
				try {
					List<Product> products = fileAction.getProduct(productUpdate.getProductId());
					if(products.get(0)!=null){
						products.get(0).setAuthority(productUpdate.getAuthority());
						products.get(0).setPackname(productUpdate.getProductName());
						fileAction.update(products);
						return "1";
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("error:IOException");
					return "0";
				}
			}
		}
		return "0";
	}
	@RequestMapping(value = "/upload",method = RequestMethod.POST)
	@ResponseBody
	public String uploadApk(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
		String userName = multipartHttpServletRequest.getParameter(ConfigCode.USERNAME);
		String passWord =  multipartHttpServletRequest.getParameter(ConfigCode.PASSWORD);
		String projcetId =  multipartHttpServletRequest.getParameter(ConfigCode.UPLOADPROJECTID);
		String productName =  multipartHttpServletRequest.getParameter(ConfigCode.UPLOADFILENAME);
		String size  =  multipartHttpServletRequest.getParameter(ConfigCode.UPLOADSIZE);
		if(check(userName, passWord)){
			System.out.println("fd：上传中");
			if(productName.substring(productName.lastIndexOf("."), productName.length()).equals(".apk")){
				ApkUtils apkUtils  = null;
				try {
					System.out.println("fd：上传中。。。。。"+projcetId);
					MultipartFile multipartFile = multipartHttpServletRequest.getFile(ConfigCode.UPLOADDATA);
					System.out.println("fd："+multipartFile.getBytes().length);
					if(String.valueOf(multipartFile.getBytes().length).equals(size)){
						File file = new File(address+"/"+projcetId+"/"+productName);
						System.out.println("file:"+","+file.getParentFile().exists());
						if(!file.getParentFile().exists()){
							file.getParentFile().mkdir();
						}
						if(file.exists()){
							file.delete();
						}
						System.out.println("address:"+address+"/"+projcetId+"/"+multipartHttpServletRequest.getParameter(ConfigCode.UPLOADFILENAME));
						StreamWriter.PrintStreamWrite(multipartFile.getBytes(),file,true);
						apkUtils = ApkUtils.ApkParse(file.getAbsolutePath());
						if(apkUtils != null){
							List<Product> products = fileAction.getProductByProjectId(Integer.valueOf(projcetId));
							if(products!=null && products.size()>0){
								for(Product p:products){
									System.err.println(p.getProductname()+","+productName);
									if(p.getProductname().equals(productName)){
										p.setPackname(apkUtils.parseAttrbute("package").get(0));
										p.setPackagesize(size);
										p.setProjectId(Integer.valueOf(projcetId));
										p.setVersionname(apkUtils.parseAttrbute("versionName").get(0));
										p.setVersioncode(apkUtils.parseAttrbute("versionCode").get(0));
										p.setUploadtime(new Date());
										fileAction.update(p);
										return "1";
									}
								}
							}
							String conditionKey = MD5Utils.getMD5(productName+new Date().getTime());
							Product product = new Product(productName,productName,Integer.valueOf(projcetId),conditionKey,0,
									apkUtils.parseAttrbute("package").get(0),new Date(),size,apkUtils.parseAttrbute("versionName").get(0),apkUtils.parseAttrbute("versionCode").get(0));
							fileAction.save(product);
							return "1";
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("error:IOException");
					return "0";
				}finally {	
					if(apkUtils != null){
						apkUtils.release();
					}
				}
			}
		}
		return "0";
	}

	@RequestMapping(value = "/delete" ,method = RequestMethod.POST)
	@ResponseBody
	public String removeApk(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");

		FileDelete fileDelete = JSONObject.parseObject(request.getParameter("request"), FileDelete.class);
		System.err.println("delete:"+request.getParameter("request"));
		boolean check = fileAction.checkPassword(fileDelete.getUserName(), fileDelete.getPassWord());
		File[] files ;
		if(check){
			List<Product> products = fileAction.getProduct(fileDelete.getProductId());
			System.err.println("delete:"+products.size());
			for(Product product :products){
				files = FileUtils.findFiles(address+"/"+product.getProjectId());
				System.err.println("delete:"+product.getProductname()+","+address+"/"+product.getProjectId()+","+files);
				if(files!=null){
					for(File file:files){
						System.err.println("delete:"+file.getName());
						if(file.getName().equals(product.getProductname())){
							System.err.println("delete:"+file.getName());
							file.delete();
						}
					}
				}
				fileAction.remove(product);
			}

			return "1";
		}
		return "0";
	}

	@RequestMapping("/iterationInfo")
	@ResponseBody
	public String iterationInfo(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		IterationRequest iterationRequest = JSONObject.parseObject(request.getParameter("request"), IterationRequest.class);
		System.out.println("进来了");
		System.out.println(request.getParameter("request")+","+(iterationRequest!=null));
		if(iterationRequest!=null){
			Product product = fileAction.getProduct(iterationRequest.getConditionKey());
			IterationInfo iterationInfo;
			if(product != null){
				String url ="http://"+request.getLocalAddr()+":"+request.getLocalPort()+request.getContextPath()+"/file/iterationDownload";
				iterationInfo = new IterationInfo(product.getPackname(),product.getProductname(),
						product.getVersionname(),product.getVersioncode(),product.getUploadtime(),product.getPackagesize(),url,true);
			}
			else {
				iterationInfo =  new IterationInfo(null,null,null,null,null,null,null,false);
			}
			return JSON.toJSONString(iterationInfo);
		}
		return "";
	}
	@RequestMapping(value = "/iterationDownload",produces="text/plain;charset=UTF-8")
	@ResponseBody
	public void download(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("来访者："+request.getRemoteAddr());
		IterationRequest iterationRequest = JSONObject.parseObject(request.getParameter("request"), IterationRequest.class);
		if(iterationRequest!= null){
			Product product = fileAction.getProduct(iterationRequest.getConditionKey());
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


	private boolean check(String userName,String passWord){
		return fileAction.checkPassword(userName, passWord);
	}


}
