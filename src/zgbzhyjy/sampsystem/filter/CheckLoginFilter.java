package zgbzhyjy.sampsystem.filter;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CheckLoginFilter implements Filter {

	FilterConfig fc = null;

	// 不处理列表
	String noFilterList = null;

	// 用于web中配置session名
	String session_user = null;

	// 校验失败后页面
	String failureAction = null;

	public void destroy() {
		this.fc = null;
		this.noFilterList = null;
		this.session_user = null;
	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {

		if (req instanceof HttpServletRequest) {
			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) resp;
			HttpSession session = request.getSession();

			if (request.getServletPath().indexOf("showLicenceInvalid.do") != -1) {
				chain.doFilter(req, resp);
				return;
			}

			if (session != null) {
				Object us = new Object();
				us = (Object) session.getAttribute(session_user);
				if (us == null) {
					// session_user为空，则检查当前路径或请求是否在不处理列表中
					if (noFilterList != null && !noFilterList.equals("")) {
						String path = request.getServletPath();
						if (isNoFilterPath(path)) {
							chain.doFilter(request, response);
							return;
						} else {
							// session_user为空，当前路径不在不过虑列表中，则提示用户重新登录
							response.sendRedirect(failureAction);
						}
					} else {
						// session_user为空，不过虑路径列表为空，则提示用户重新登录
						response.sendRedirect(failureAction);
					}
				} else{
					// session_user不为空，则通过.
					chain.doFilter(req, resp);
				}
			} else {
				response.sendRedirect(failureAction);
			}
		} else {
			chain.doFilter(req, resp);
		}

	}

	// 判断是否存在设置不过滤的路径
	protected boolean isNoFilterPath(String path) {
		boolean NoFilterflag = false;
		String temp = null;
		StringTokenizer tokens = new StringTokenizer(noFilterList, ",");
		int countPath = tokens.countTokens();
		for (int i = 0; i < countPath; i++) {
			temp = (String) tokens.nextElement();
			if (temp.equalsIgnoreCase(path)) {
				NoFilterflag = true;
				break;
			}
		}

		return NoFilterflag;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.fc = filterConfig;
		this.session_user = fc.getInitParameter("session_user");
		this.noFilterList = fc.getInitParameter("noFilterList");
		this.failureAction = fc.getInitParameter("failureAction");
	}

}
