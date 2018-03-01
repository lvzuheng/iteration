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

	/**项目信息更新 */
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
	/**项目移除 */
	@RequestMapping(value = "/removeProject",method = RequestMethod.POST,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String removeProject(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		ProjectRemove projectRemove = JSON.parseObject(request.getParameter(ConfigCode.REQUEST),ProjectRemove.class);
		if(projectRemove != null && fileAction.checkPassword(projectRemove.getUserName(), projectRemove.getPassWord())){
			try {
				Project project= fileAction.getProjectByProjectId(projectRemove.getProjectId());
				if(project != null){
					File dir = new File(address+"/"+project.getId());
					if(dir!=null && dir.isDirectory()){
						for(File file: dir.listFiles()){
							file.delete();
						}
						dir.delete();
					}
					
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

	/**项目创建 */
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
	/**项目信息 */
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

	/**
	 * 应用创建
	 * 可以进行连表查询优化
	 * */
	@RequestMapping(value = "/createProduct",method = RequestMethod.POST,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String createProduct(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		ProductCreate productCreate = JSON.parseObject(request.getParameter(ConfigCode.REQUEST),ProductCreate.class);
		System.err.println(request.getParameter(ConfigCode.REQUEST));
		if(productCreate != null && fileAction.checkPassword(productCreate.getUserName(), productCreate.getPassWord())){
			if(productCreate.getProductName() == null){
				return "2";
			}
			try {
				Project project  = fileAction.getProjectByProjectId(productCreate.getProjectId());
				if(project!=null){
					List<Product> pList = fileAction.getProduct(productCreate.getProjectId(),productCreate.getProductName());
					if(pList == null || pList.size() == 0){
						String conditionKey = MD5Utils.getMD5(productCreate.getProductName()+new Date().getTime());
						Product newProduct = new Product(productCreate.getProductName(),null,productCreate.getProjectId(),conditionKey,productCreate.getAuthority(),
								null,null,new Date(),null,null,null);
						fileAction.save(newProduct);
						return "1";
					}
				}
				return "0";
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return "0";
			}
		}
		return "0";
	}
	/**应用信息 */
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

	/**项目更新 */
	@RequestMapping(value = "/updateProduct",method = RequestMethod.POST)
	@ResponseBody
	public String updateProduct(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		ProductUpdate productUpdate = JSON.parseObject(request.getParameter(ConfigCode.REQUEST),ProductUpdate.class);
		if(check(productUpdate.getUserName(), productUpdate.getPassWord())){
			System.out.println("fd：上传中");
			if(productUpdate.getProductName().substring(productUpdate.getProductName().lastIndexOf("."), productUpdate.getProductName().length()).equals(".apk")){
				try {
					Product product = fileAction.getProduct(productUpdate.getProductId());
					if(product!=null){
						product.setAuthority(productUpdate.getAuthority());
						product.setPackname(productUpdate.getProductName());
						fileAction.update(product);
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
	
	/**文件上传 */
	@RequestMapping(value = "/upload",method = RequestMethod.POST)
	@ResponseBody
	public String uploadApk(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
		String userName = multipartHttpServletRequest.getParameter(ConfigCode.USERNAME);
		String passWord =  multipartHttpServletRequest.getParameter(ConfigCode.PASSWORD);
		String projcetId =  multipartHttpServletRequest.getParameter(ConfigCode.UPLOADPROJECTID);
		String productId =  multipartHttpServletRequest.getParameter(ConfigCode.UPLOADPRODUCTID);
		String size  =  multipartHttpServletRequest.getParameter(ConfigCode.UPLOADSIZE);
		String fileName = multipartHttpServletRequest.getParameter(ConfigCode.UPLOADFILENAME);
		if(check(userName, passWord)){
			Product product = fileAction.getProduct(Integer.valueOf(productId));
			if(product == null){
				return "233";
			}
			ApkUtils apkUtils  = null;
			try {
				System.out.println("fd：上传中。。。。。"+projcetId);
				MultipartFile multipartFile = multipartHttpServletRequest.getFile(ConfigCode.UPLOADDATA);
				System.out.println("fd："+multipartFile.getBytes().length);
				if(!String.valueOf(multipartFile.getBytes().length).equals(size)){
					System.out.println("fd："+multipartFile.getBytes().length+","+size);
					return "23333";
				}
				File file = new File(address+"/"+projcetId+"/"+product.getProductname());
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
					product.setPackname(apkUtils.parseAttrbute("package").get(0));
					product.setPackagesize(size);
					product.setProjectId(Integer.valueOf(projcetId));
					product.setVersionname(apkUtils.parseAttrbute("versionName").get(0));
					product.setVersioncode(apkUtils.parseAttrbute("versionCode").get(0));
					product.setUploadtime(new Date());
					product.setFileName(fileName);
					fileAction.update(product);
					return "1";
				}
			} catch (Exception e) {
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
		return "0";
	}

	/**文件删除 */
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

	/**迭代信息查询 */
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
