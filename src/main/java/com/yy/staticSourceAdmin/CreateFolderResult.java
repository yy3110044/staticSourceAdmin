package com.yy.staticSourceAdmin;

import java.io.File;

public class CreateFolderResult {
	private File baseFolder;
	private String baseUrl;
	public CreateFolderResult(File baseFolder, String baseUrl) {
		this.baseFolder = baseFolder;
		this.baseUrl = baseUrl;
	}
	public File getBaseFolder() {
		return baseFolder;
	}
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseFolder(File baseFolder) {
		this.baseFolder = baseFolder;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
}