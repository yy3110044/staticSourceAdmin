package com.yy.staticSourceAdmin;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.yy.staticSourceAdmin.FileDownloadUtil.DownloadResult;

@WebServlet("/fileServlet")
public class FileServlet extends HttpServlet {
	private static final long serialVersionUID = -6208697900512843433L;

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String action = req.getParameter("action");
		if(!Util.empty(action)) {
			switch(action) {
			case "fileDownload":
				this.fileDownload(req, resp);
				break;
			case "getDownloadResult":
				this.getDownloadResult(req, resp);
				break;
			case "removeDownloadRecord":
				this.removeDownloadRecord(req, resp);
				break;
			case "getAllDownload":
				this.getAllDownload(req, resp);
				break;
			}
		}
	}

	//下载一个文件
	private void fileDownload(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String sourceUrl = req.getParameter("sourceUrl");
		String notifyUrl = req.getParameter("notifyUrl");
		if(!Util.empty(sourceUrl)) {
			ResponseObject ro = FileDownloadUtil.getInstance().addDownloadQueue(sourceUrl, notifyUrl, req);
			resp.setContentType("application/json;charset=utf-8");
			resp.getWriter().write(Util.objectToJson(ro));
		}
	}
	
	//返回下载状态
	private void getDownloadResult(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String sourceUrl = req.getParameter("sourceUrl");
		if(!Util.empty(sourceUrl)) {
			DownloadResult dr = FileDownloadUtil.getInstance().getDownloadStatus(sourceUrl);
			resp.setContentType("application/json;charset=utf-8");
			if(dr == null) {
				resp.getWriter().write(Util.objectToJson(new ResponseObject(101, "下载结果为空，请查看是否已添加到下载队列")));
			} else {
				resp.getWriter().write(Util.objectToJson(new ResponseObject(100, "返回成功", dr)));
			}
		}
	}
	
	//删除一个下载记录(只是删除map中的数据，不会取消已经提交的下载任务)
	private void removeDownloadRecord(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String sourceUrl = req.getParameter("sourceUrl");
		if(!Util.empty(sourceUrl)) {
			resp.setContentType("application/json;charset=utf-8");
			resp.getWriter().write(Util.objectToJson(new ResponseObject(100, "删除成功", FileDownloadUtil.getInstance().removeDownloadRecord(sourceUrl))));
		}
	}
	
	//返回所有下载
	private void getAllDownload(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json;charset=utf-8");
		resp.getWriter().write(Util.objectToJson(new ResponseObject(100, "返回成功", FileDownloadUtil.getInstance().getMap().values())));
	}
}