package ch.frickler.jass.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.frickler.jass.UserBean;

/**
 * filters all requests to the /restricted/ folder if the session has a UserBean
 * and is logged in, the page is displayed. otherwise the login page is shown.
 */
public class AuthenticationFilter implements Filter {

	@Override
	public void destroy() {
		//TODO
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest) req).getSession();
		UserBean userBean = (UserBean) (session.getAttribute("userBean"));
		if (userBean != null && userBean.isLoggedIn()) {
			chain.doFilter(req, resp);
		} else {
			((HttpServletResponse) resp).sendRedirect("../login.jsf");
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		//TODO
	}

}
