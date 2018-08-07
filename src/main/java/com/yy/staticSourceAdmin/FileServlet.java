package com.yy.staticSourceAdmin;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/fileServlet")
public class FileServlet extends HttpServlet {
	private static final long serialVersionUID = -6208697900512843433L;

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
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

	private void fileDownload(HttpServletRequest req, HttpServletResponse resp) {
		
	}
	
	private void getDownloadResult(HttpServletRequest req, HttpServletResponse resp) {
		
	}
	
	private void removeDownloadRecord(HttpServletRequest req, HttpServletResponse resp) {
		
	}
	
	private void getAllDownload(HttpServletRequest req, HttpServletResponse resp) {
		
	}
}