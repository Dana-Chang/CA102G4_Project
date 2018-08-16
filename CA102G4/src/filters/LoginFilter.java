package filters;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.mem.model.*;

public class LoginFilter implements Filter {

	private FilterConfig config;

	@Override
	public void destroy() {
		config = null;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		res.setContentType("text/html; charset=UTF-8");
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		HttpSession session = request.getSession();

		MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");

		if (memberVO == null) {
			session.setAttribute("location", request.getRequestURI()+"?action=myBlog");
			response.sendRedirect(request.getContextPath() + "/front_end/member/mem_login.jsp");
			return;
		} else {
			chain.doFilter(req, res);
		}

	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		this.config = config;
	}

}
