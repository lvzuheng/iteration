package com.lzh.iteration.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzh.iteration.bean.code.Product;
import com.lzh.iteration.bean.code.Project;
import com.lzh.iteration.bean.http.ProductDelete;
import com.lzh.iteration.bean.http.IterationInfo;
import com.lzh.iteration.bean.http.IterationRequest;
import com.lzh.iteration.bean.http.ProductCreate;
import com.lzh.iteration.bean.http.ProductInfo;
import com.lzh.iteration.bean.http.ProductUpdate;
import com.lzh.iteration.service.ProductServices;
import com.lzh.iteration.service.ProjectServices;
import com.lzh.iteration.utils.ApkUtils;
import com.lzh.iteration.utils.ConfigCode;
import com.lzh.iteration.utils.Configure;
import com.lzh.iteration.utils.FileUtils;
import com.lzh.iteration.utils.MD5Utils;
import com.lzh.iteration.utils.StreamWriter;

@Controller
@RequestMapping(value = "/product")
public class ProductController {
	private String address = Configure.getConfig().getApkAddress();
	@Resource
	private ProductServices productServices;
	@Resource
	private ProjectServices projectServices;
	
	/**
	 * 应用创建
	 * 可以进行连表查询优化
	 * */
	@RequestMapping(value = "/create",method = RequestMethod.POST,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String createProduct(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		ProductCreate productCreate = JSON.parseObject(request.getParameter(ConfigCode.REQUEST),ProductCreate.class);
		System.err.println(request.getParameter(ConfigCode.REQUEST));
		if(productCreate != null && productServices.checkPassword(productCreate.getUserName(), productCreate.getPassWord())){
			if(productCreate.getProductName() == null){
				return "2";
			}
			try {
				Project project  = projectServices.getProjectByProjectId(productCreate.getProjectId());
				if(project!=null){
					List<Product> pList = productServices.getProduct(productCreate.getProjectId(),productCreate.getProductName());
					if(pList == null || pList.size() == 0){
						String conditionKey = MD5Utils.getMD5(productCreate.getProductName()+new Date().getTime());
						Product newProduct = new Product(productCreate.getProductName(),null,productCreate.getProjectId(),conditionKey,productCreate.getAuthority(),
								null,null,new Date(),null,null,null);
						productServices.save(newProduct);
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
	@RequestMapping(value = "/info",method = RequestMethod.POST,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String Productinfo(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		System.out.println(request.getParameter(ConfigCode.REQUEST));
		ProductInfo ProductInfo = JSON.parseObject(request.getParameter(ConfigCode.REQUEST),ProductInfo.class);
		if(ProductInfo != null && productServices.checkPassword(ProductInfo.getUserName(), ProductInfo.getPassWord())){
			List<Product> pList = productServices.getProductByProjectId(ProductInfo.getProjectId());
			if(pList != null){
				System.out.println(ProductInfo.getProjectId() +","+JSON.toJSONString(pList));
				return JSON.toJSONString(pList);
			}
		}
		return "0";
	}

	/**项目更新 */
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	@ResponseBody
	public String updateProduct(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8020");
		ProductUpdate productUpdate = JSON.parseObject(request.getParameter(ConfigCode.REQUEST),ProductUpdate.class);
		if(productServices.checkPassword(productUpdate.getUserName(), productUpdate.getPassWord())){
			System.out.println("fd：上传中");
			if(productUpdate.getProductName().substring(productUpdate.getProductName().lastIndexOf("."), productUpdate.getProductName().length()).equals(".apk")){
				try {
					Product product = productServices.getProduct(productUpdate.getProductId());
					if(product!=null){
						product.setAuthority(productUpdate.getAuthority());
						product.setPackname(productUpdate.getProductName());
						productServices.update(product);
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
		System.out.println(fileName);
		if(productServices.checkPassword(userName, passWord)){
			Product product = productServices.getProduct(Integer.valueOf(productId));
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
				File file = new File(address+"/"+projcetId+"/"+product.getProductname()+"/"+fileName);
				System.out.println("file:"+","+file.getParentFile().exists());
				if(!file.getParentFile().exists()){
					file.getParentFile().mkdirs();
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
					productServices.update(product);
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
		ProductDelete fileDelete = JSONObject.parseObject(request.getParameter("request"), ProductDelete.class);
		System.err.println("delete:"+request.getParameter("request"));
		boolean check = productServices.checkPassword(fileDelete.getUserName(), fileDelete.getPassWord());
		File[] files ;
		if(check){
			List<Product> products = productServices.getProduct(fileDelete.getProductId());
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
				productServices.remove(product);
			}

			return "1";
		}
		return "0";
	}



}
