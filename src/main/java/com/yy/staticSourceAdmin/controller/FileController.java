package com.yy.staticSourceAdmin.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.yy.staticSourceAdmin.util.ResponseObject;
import com.yy.staticSourceAdmin.util.Util;

@RestController
@RequestMapping(method=RequestMethod.POST)
public class FileController {

	@RequestMapping("/fileUpload")
	public ResponseObject fileUpload(@RequestParam MultipartFile file, HttpServletRequest req) {
		return Util.saveFile(file, req);
	}
}