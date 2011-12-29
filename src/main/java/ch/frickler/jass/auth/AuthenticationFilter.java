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
 * this class check in all files of the folder /restricted/ if the user is logged in.
 */
public class AuthenticationFilter implements Filter {

	@Override
	public void destroy() {
		//TODO
	}

	/**
	 * check if the user is logged in, if not redirect it to the login page
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
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
	public void init(FilterConfig arg0) throws ServletException {
		// no init required	
	}
}
