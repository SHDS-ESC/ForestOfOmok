package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.UserDAO;
import model.User;

@WebServlet("/join")
public class JoinServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doHandle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("UTF-8");

		System.out.println("result");
	
		String name = request.getParameter("name");
		System.out.println(name + "아이디");
		// 객체 생성 및 값 세팅
		User user = new User();
		user.setUserId(request.getParameter("userId"));
		user.setImgId(request.getParameter("imgId"));
		user.setPwd(request.getParameter("pwd"));
		user.setName(request.getParameter("name"));
		user.setEmail(request.getParameter("email"));
		
		//아이디 중복체크
		if ("1".equals(request.getParameter("check"))) {
			UserDAO dao = new UserDAO();
			boolean exists = dao.isUserIdExists(name);

			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write("{\"exists\":" + exists + "}");
			return;
		}
		
		// DAO 호출
		UserDAO dao = new UserDAO();
		int result = dao.insertUser(user);
		
		System.out.println("결과" + result);

		if (result > 0) {
			// 회원가입 성공 시 login.html로 포워드
			HttpSession session = request.getSession();
			session.setAttribute("userId", user.getUserId());
			response.sendRedirect(request.getContextPath() + "/login");


		} else {
			// 실패 시 다시 join.html로 포워드
			request.setAttribute("error", "회원가입 실패. 다시 시도해주세요.");
			System.out.println(name);
			request.getRequestDispatcher("/html/join.html").forward(request, response);
		}
	}
}
