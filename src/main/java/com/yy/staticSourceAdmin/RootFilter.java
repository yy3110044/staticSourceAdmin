package com.yy.staticSourceAdmin;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/*")
public class RootFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse httpResp = (HttpServletResponse)response;
		httpResp.setHeader("Access-Control-Allow-Origin", "*");
		httpResp.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
		httpResp.setHeader("Access-Control-Allow-Headers", "x-requested-with, Content-Type");
		httpResp.setHeader("Access-Control-Allow-Credentials", "true");
		chain.doFilter(request, response);
	}
}