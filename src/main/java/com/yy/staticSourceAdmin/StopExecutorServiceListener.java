package com.yy.staticSourceAdmin;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class StopExecutorServiceListener implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent event) {
		
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		FileDownloadUtil.getInstance().shutdown();
	}
}