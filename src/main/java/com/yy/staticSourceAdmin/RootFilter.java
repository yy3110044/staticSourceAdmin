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

		String fileName = request.getParameter("fileName");
		if(!Util.empty(fileName)) {
			System.out.println(fileName);
			httpResp.setHeader("Content-Disposition", "attachment;filename=" + Util.urlEncode(fileName));
		}
		
		httpResp.setHeader("Access-Control-Allow-Origin", "*");//允许跨域访问
		chain.doFilter(request, response);
	}
}