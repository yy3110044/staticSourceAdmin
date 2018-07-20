package com.yy.staticSourceAdmin.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {
	private Util() {}
	
	private static final Logger logger = LogManager.getLogger(Util.class);
	
	public static boolean empty(String str) {
		return str == null || str.trim().isEmpty();
	}
	public static boolean empty(String ... strs) {
		for(String str : strs) {
			if(empty(str)) {
				return true;
			}
		}
		return false;
	}
	
	public static String getBasePath(HttpServletRequest request) {
		int port = request.getServerPort();
		String portStr = port == 80 ? "" : ":" + port;
		String basePath = request.getScheme() + "://" + request.getServerName() + portStr + request.getContextPath() + "/";
		return basePath;
	}
	
	//url编码
	public static String urlEncode(String str) {
		String retVal = "";
		if(str != null) {
			try {
				retVal = URLEncoder.encode(str, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.error(e.toString());
				throw new RuntimeException(e);
			}
		}
		return retVal;
	}
	
	//url解码
	public static String urlDecode(String str) {
		String retVal = "";
		if(str != null) {
			try {
				retVal = URLDecoder.decode(str, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.error(e.toString());
				throw new RuntimeException(e);
			}
		}
		return retVal;
	}
	
	/**
	 * 将对象转换成json字符串
	 * @param o
	 * @return
	 */
	private static ObjectMapper objectMapper = new ObjectMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	public static String toJsonStr(Object o) {
		try {
			return objectMapper.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			logger.error(o.toString() + "：转换json错误：" + e.toString());
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 返回spring的bean
	 * @param name
	 * @param sc
	 * @return
	 */
	public static Object getBean(String name, ServletContext sc) {
		WebApplicationContext context = (WebApplicationContext)sc.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		return context.getBean(name);
	}
	public static <T> T getBean(Class<T> requiredType, ServletContext sc) {
		WebApplicationContext context = (WebApplicationContext)sc.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		return context.getBean(requiredType);
	}
	
	/**
	 * 得到url文件的后缀
	 * @param urlStr
	 * @return
	 */
	public static String getSuffix(String str) {
		str = str.substring(str.lastIndexOf('/'));

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
	
	/**
	 * 将对象转为map
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> ObjectToMap(Object obj) {
		Field[] fields = obj.getClass().getFields();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			for(Field field : fields) {
				field.setAccessible(true);
				map.put(field.getName(), field.get(obj));
			}
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
		return map;
	}

	/**
	 * 保存上传文件
	 * @param file
	 * @return
	 */
	public static ResponseObject saveFile(MultipartFile file, HttpServletRequest req) {
		if(file != null && file.getSize() > 0) {
			CreateFolderResult cfr = getBaseFolder(req.getServletContext());
			
			String originalFileName = file.getOriginalFilename(); //文件原始名称
			int index = originalFileName.lastIndexOf(".");
			String newFileName = UUID.randomUUID().toString() + (index < 0 ? "" : originalFileName.substring(index));
			File newFile = new File(cfr.getBaseFolder(), newFileName);

			try {
				file.transferTo(newFile);
				logger.debug(newFile.getPath());
				return new ResponseObject(100, "文件上传成功", new MyMap().set("serverUrl", getBasePath(req) + cfr.getBaseUrl() + newFileName));
			} catch (IllegalStateException | IOException e) {
				logger.error(e.toString());
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
}