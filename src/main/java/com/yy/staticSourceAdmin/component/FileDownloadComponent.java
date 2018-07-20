package com.yy.staticSourceAdmin.component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.yy.staticSourceAdmin.util.CreateFolderResult;
import com.yy.staticSourceAdmin.util.MyMap;
import com.yy.staticSourceAdmin.util.ResponseObject;
import com.yy.staticSourceAdmin.util.Util;

/**
 * 文件下载组件
 * @author yy
 *
 */
@Component("fileDownloadComponent")
public class FileDownloadComponent {
	private static final Logger logger = LogManager.getLogger(FileDownloadComponent.class);
	
	private ExecutorService service = Executors.newFixedThreadPool(5);
	private Map<String, DownloadResult> taskMap = new ConcurrentHashMap<String, DownloadResult>();
	
	@Value("${download.connectionTimeout}")
	private int connectionTimeout;
	@Value("${download.readTimeout}")
	private int readTimeout;

	/**
	 * 把一个url添加进下载队列
	 * @param url
	 */
	public ResponseObject addDownloadQueue(String sourceUrl, String notifyUrl, HttpServletRequest req) {
		DownloadResult dr = taskMap.get(sourceUrl);
		if(dr == null) {
			dr = new DownloadResult();
			dr.setSourceUrl(sourceUrl);
			dr.setDownloadStatus(DownloadStatus.准备中);
			dr.setNotifyUrl(notifyUrl);
			service.execute(new DownloadRunnable(dr, Util.getBaseFolder(req.getServletContext()), Util.getBasePath(req)));
			taskMap.put(sourceUrl, dr);
			return new ResponseObject(100, "已添加到下载队列中");
		} else {
			return new ResponseObject(101, "已在下载队列中，不要重复添加", dr);
		}
	}
	
	/**
	 * 返回下载结果
	 * @param sourceUrl
	 * @return
	 */
	public DownloadResult getDownloadStatus(String sourceUrl) {
		return taskMap.get(sourceUrl);
	}
	
	/**
	 * 删除一个下载记录
	 * @param sourceUrl
	 * @return
	 */
	public DownloadResult removeDownloadRecord(String sourceUrl) {
		return taskMap.remove(sourceUrl);
	}
	
	/**
	 * 返回map
	 * @return
	 */
	public Map<String, DownloadResult> getMap() {
		return taskMap;
	}
	public void setMap(Map<String, DownloadResult> taskMap) {
		this.taskMap = taskMap;
	}
	
	public void shutdown() {
		service.shutdown();
	}
	
	
	//下载线程执行类
	private class DownloadRunnable implements Runnable {
		private DownloadResult dr;
		private CreateFolderResult cfr;
		private String basePath;
		DownloadRunnable(DownloadResult dr, CreateFolderResult cfr, String basePath) {
			this.dr = dr;
			this.cfr = cfr;
			this.basePath = basePath;
		}
		@Override
		public void run() {
			String newFileName = UUID.randomUUID().toString() + Util.getSuffix(dr.getSourceUrl());
			File newFile = new File(cfr.getBaseFolder(), newFileName);
			dr.setDownloadStatus(DownloadStatus.下载中);
			try {
				FileUtils.copyURLToFile(new URL(dr.getSourceUrl()), newFile, connectionTimeout, readTimeout);
				dr.setDownloadStatus(DownloadStatus.已完成);
				dr.setFilePath(newFile.getPath());
				dr.setServerUrl(basePath + cfr.getBaseUrl() + newFileName);
			} catch (IOException e) {
				logger.error(e.toString());
				dr.setDownloadStatus(DownloadStatus.未完成);
			}
			if(!Util.empty(dr.getNotifyUrl())) { //通知
				Util.requestPost(dr.getNotifyUrl(), new MyMap().set("downloadStatus", dr.getDownloadStatus()).set("filePath", dr.getFilePath()).set("serverUrl", dr.getServerUrl()));
			}
		}
	}
	
	//下载结果
	public class DownloadResult {
		private String sourceUrl;
		private DownloadStatus downloadStatus;
		private String filePath;
		private String serverUrl;
		private String notifyUrl;
		public String getSourceUrl() {
			return sourceUrl;
		}
		public void setSourceUrl(String sourceUrl) {
			this.sourceUrl = sourceUrl;
		}
		public DownloadStatus getDownloadStatus() {
			return downloadStatus;
		}
		public String getFilePath() {
			return filePath;
		}
		public String getServerUrl() {
			return serverUrl;
		}
		public void setDownloadStatus(DownloadStatus downloadStatus) {
			this.downloadStatus = downloadStatus;
		}
		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}
		public void setServerUrl(String serverUrl) {
			this.serverUrl = serverUrl;
		}
		public String getNotifyUrl() {
			return notifyUrl;
		}
		public void setNotifyUrl(String notifyUrl) {
			this.notifyUrl = notifyUrl;
		}
	}
	
	public enum DownloadStatus {
		准备中, 下载中, 已完成, 未完成
	}
}