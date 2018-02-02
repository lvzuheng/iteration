package com.lzh.iteration.utils;

import java.io.File;
import java.util.List;

public class FileUtils {

	public static File[] findFiles(String fileParentPath){
		File file = new File(fileParentPath);
		if(file.exists() && file.isDirectory()){
			return file.listFiles();
		}
		return null;
	}
	
}
