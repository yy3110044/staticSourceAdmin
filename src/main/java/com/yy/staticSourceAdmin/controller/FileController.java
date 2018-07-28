package com.yy.staticSourceAdmin.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.yy.staticSourceAdmin.component.FileDownloadComponent;
import com.yy.staticSourceAdmin.component.FileDownloadComponent.DownloadResult;
import com.yy.staticSourceAdmin.util.ResponseObject;
import com.yy.staticSourceAdmin.util.Util;

@CrossOrigin
@RestController
@RequestMapping(method=RequestMethod.POST)
public class FileController {
	@Autowired
	private FileDownloadComponent fd;

	/**
	 * 上传一个文件
	 * @param file
	 * @param req
	 * @return
	 */
	@RequestMapping("/fileUpload")
	public ResponseObject fileUpload(@RequestParam MultipartFile file, HttpServletRequest req) {
		return Util.saveFile(file, req);
	}

	/**
	 * 下载一个文件
	 * @return
	 */
	@RequestMapping("/fileDownload")
	public ResponseObject fileDownload(@RequestParam String sourceUrl, String notifyUrl, HttpServletRequest req) {
		return fd.addDownloadQueue(sourceUrl.trim(), notifyUrl == null ? null : notifyUrl.trim(), req);
	}
	
	/**
	 * 返回下载状态
	 * @param sourceUrl
	 * @return
	 */
	@RequestMapping("/getDownloadResult")
	public ResponseObject getDownloadResult(@RequestParam String sourceUrl) {
		DownloadResult dr = fd.getDownloadStatus(sourceUrl);
		if(dr == null) {
			return new ResponseObject(101, "下载结果为空，请查看是否已添加到下载队列");
		} else {
			return new ResponseObject(100, "返回成功", dr);
		}
	}
	
	/**
	 * 删除一个下载记录(只是删除map中的数据，不会取消已经提交的下载任务)
	 * @return
	 */
	@RequestMapping("/removeDownloadRecord")
	public ResponseObject removeDownloadRecord(@RequestParam String sourceUrl) {
		return new ResponseObject(100, "删除成功", fd.removeDownloadRecord(sourceUrl));
	}
	
	/**
	 * 返回所有下载
	 * @return
	 */
	@RequestMapping("/getAllDownload")
	public ResponseObject getAllDownload() {
		return new ResponseObject(100, "返回成功", fd.getMap().values());
	}
}