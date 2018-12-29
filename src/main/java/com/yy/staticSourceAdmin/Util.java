package com.yy.staticSourceAdmin;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Util {
	private Util() {}
	
	public static boolean empty(String str) {
		return str == null || str.trim().isEmpty();
	}
	
	public static String getBasePath(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		String scheme = request.getScheme();
		sb.append(scheme).append("://").append(request.getServerName());
		int port = request.getServerPort();
		if(("https".equals(scheme) && port != 443) || ("http".equals(scheme) && port != 80)) {
			sb.append(':').append(port);
		}
		sb.append(request.getContextPath()).append('/');
		return sb.toString();
	}
	
	//保存文件
	public static ResponseObject saveFile(FileItem fileItem, HttpServletRequest req) {
		if(fileItem != null && fileItem.getSize() > 0) {
			CreateFolderResult cfr = getBaseFolder(req.getServletContext());
			
			String originalFileName = fileItem.getName(); //文件原始名称
			int index = originalFileName.lastIndexOf(".");
			
			String suffix = "";
			if(index >= 0) {
				suffix = originalFileName.substring(index);
				if(".jsp".equals(suffix)) {//把jsp文件替换成.txt
					suffix = ".txt";
				}
			}

			String newFileName = UUID.randomUUID().toString() + suffix;
			File newFile = new File(cfr.getBaseFolder(), newFileName);
			
			try {
				fileItem.write(newFile);
				return new ResponseObject(100, "文件上传成功", new JsonResultMap().set("serverUrl", getBasePath(req) + cfr.getBaseUrl() + newFileName));
			} catch (Exception e) {
				return new ResponseObject(102, "文件上传失败");
			}
		} else {
			return new ResponseObject(101, "上传文件为空");
		}
	}
	
	public static CreateFolderResult getBaseFolder(ServletContext sc) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateStr = sdf.format(new Date());
		
		File baseFolder = new File(sc.getRealPath("/"), dateStr);
		if(!baseFolder.exists()) {
			baseFolder.mkdirs();
		}
		
		String baseUrl = dateStr + "/";
		return new CreateFolderResult(baseFolder, baseUrl);
	}
	
	/**
	 * 得到url文件的后缀
	 * @param urlStr
	 * @return
	 */
	public static String getSuffix(String str) {
		int i = str.lastIndexOf('/');
		if(i >= 0) {
			str = str.substring(i);
		}

		int paramStrIndex = str.indexOf('?'); //参数字符串首字索引
		if(paramStrIndex >= 0) { //没有参数字符串
			str = str.substring(0, paramStrIndex);
		} 

		int index = str.lastIndexOf('.');
		if(index < 0) {
			return "";
		} else {
			return str.substring(index);
		}
	}
	
	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	public static String objectToJson(Object o) {
		return gson.toJson(o);
	}
}