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

// 임시 서블릿. controller에는 서블릿 클래스 배치할 것

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("UTF-8");
	
		String name = request.getParameter("name");
		String pwd = request.getParameter("pwd");
		System.out.println("name: [" + name + "]");
		System.out.println("pwd: [" + pwd + "]");
		
		UserDAO dao = new UserDAO();
		User user = dao.login(name, pwd);
		System.out.println(user);
		if(user != null) {
			HttpSession session = request.getSession();
			session.setAttribute("userId",user.getUserId());
			System.out.println("로그인");
			request.getRequestDispatcher("/main").forward(request, response);
		} else {
			// 로그인 실패: 에러 메시지를 함께 전달하고 다시 로그인 페이지로 포워드
			request.setAttribute("errorMsg", "아이디 또는 비밀번호가 잘못되었습니다.");
			System.out.println("로그인실패");
			request.getRequestDispatcher("/html/login.html").forward(request, response);
		}
	}

}
