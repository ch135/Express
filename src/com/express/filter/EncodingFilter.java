package com.express.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 编码过滤器
 * @author LK
 *
 */
public class EncodingFilter implements Filter {

	private String charEncoding = null;

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
		charEncoding = arg0.getInitParameter("encoding");
		if(charEncoding == null){
			throw new ServletException("EncodingFilter中的编码设置为空");
		}
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		System.out.println(((HttpServletRequest) request).getRequestURL());
		if(!charEncoding.equals(request.getCharacterEncoding())){
			request.setCharacterEncoding(charEncoding);
		}
		response.setCharacterEncoding(charEncoding);
		chain.doFilter(request, response);
	}



	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
}
