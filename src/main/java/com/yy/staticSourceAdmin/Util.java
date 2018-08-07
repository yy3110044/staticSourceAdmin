package com.yy.staticSourceAdmin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
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
		int port = request.getServerPort();
		String portStr = port == 80 ? "" : ":" + port;
		String basePath = request.getScheme() + "://" + request.getServerName() + portStr + request.getContextPath() + "/";
		return basePath;
	}
	
	//保存文件
	public static ResponseObject saveFile(FileItem fileItem, HttpServletRequest req) {
		if(fileItem != null && fileItem.getSize() > 0) {
			CreateFolderResult cfr = getBaseFolder(req.getServletContext());
			
			String originalFileName = fileItem.getName(); //文件原始名称
			int index = originalFileName.lastIndexOf(".");
			String newFileName = UUID.randomUUID().toString() + (index < 0 ? "" : originalFileName.substring(index));
			File newFile = new File(cfr.getBaseFolder(), newFileName);
			
			try {
				fileItem.write(newFile);
				return new ResponseObject(100, "文件上传成功", new MyMap().set("serverUrl", getBasePath(req) + cfr.getBaseUrl() + newFileName));
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
	
	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd  HH:mm:ss").create();
	public static String objectToJson(Object o) {
		return gson.toJson(o);
	}
	
	public static String requestPost(String urlStr, Map<String, Object> params) {
		BufferedWriter bw = null;
		BufferedReader br = null;
		StringBuilder result = new StringBuilder();

		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(10000);
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("contentType", "application/x-www-form-urlencoded");
			
			if(params != null && params.size() > 0) {
				bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
				StringBuilder sb = new StringBuilder();
				int i = 0;
				for(Entry<String, Object> entry : params.entrySet()) {
					sb.append(entry.getKey()).append('=').append(entry.getValue());
					if(++i < params.size()) {
						sb.append('&');
					}
				}
				bw.write(sb.toString());
				bw.flush();
			}
			
			if(conn.getResponseCode() == 200) {
				br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				String str = null;
				while((str = br.readLine()) != null) {
					result.append(str);
				}
			} else {
				System.err.println("请求错误：" + url + "，代码：" + conn.getResponseCode());
			}
		} catch (IOException e) {
			System.err.println(e.toString());
		} finally {
			try {
				if(bw != null) bw.close();
				if(br != null) br.close();
			} catch (IOException e) {
				System.err.println(e.toString());
			}
		}
		return result.toString();
	}
}