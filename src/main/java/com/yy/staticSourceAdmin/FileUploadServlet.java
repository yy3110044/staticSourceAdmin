package com.yy.staticSourceAdmin;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@WebServlet("/fileUpload")
public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 2423733173012648925L;
	
	private ServletFileUpload upload;
	public FileUploadServlet() {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));//设置临时目录

		upload = new ServletFileUpload(factory);
		upload.setFileSizeMax(536870912L); //设置文件最大上传大小：512MB
		upload.setSizeMax(536870912L); //设置最大请求值 (包含文件和表单数据)
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
		//是否是文件上传
		if(ServletFileUpload.isMultipartContent(req)) {
			List<FileItem> fileItems = null;
			try {
				fileItems = upload.parseRequest(req);
			} catch (FileUploadException e) {
				System.err.println(e.toString());
			}
			if(fileItems != null) {
				List<ResponseObject> resultList = new ArrayList<ResponseObject>();
				for(FileItem fileItem : fileItems) {
					if(!fileItem.isFormField()) {
						resultList.add(Util.saveFile(fileItem, req));
					}
				}
				
			}
		}
	}
}