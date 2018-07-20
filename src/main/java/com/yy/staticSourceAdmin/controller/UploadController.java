package com.yy.staticSourceAdmin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.yy.staticSourceAdmin.util.ResponseObject;

@RestController
@RequestMapping(method=RequestMethod.POST)
public class UploadController {

	@RequestMapping("/upload")
	public ResponseObject upload(@RequestParam MultipartFile file) {
	}
}