package zgbzhyjy.sampsystem.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class HttpSessionFilter implements Filter {
	public static final ThreadLocal<HttpSession> session = new ThreadLocal<HttpSession>();

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		session.set(((HttpServletRequest) req).getSession());
		chain.doFilter(req, resp);
	}

	public void destroy() {
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}
	
	public static Object getAttribute(String name){
		return session.get().getAttribute(name);
	}
}
