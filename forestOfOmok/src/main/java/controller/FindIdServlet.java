package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDAO;

@WebServlet("/findId")
public class FindIdServlet extends HttpServlet {

	public FindIdServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
		
	}

	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		
		
		String method = request.getMethod();

		if (method.equalsIgnoreCase("GET")) {
			// 사용자가 직접 브라우저에서 /findId로 접근한 경우
			request.getRequestDispatcher("/html/findId.html").forward(request, response);
			return;
		}
		String email = request.getParameter("email");
		
		UserDAO userDAO = new UserDAO();
		
		
		String name = userDAO.findNameByEmail(email);
		
		if (name != null) {
			response.getWriter().write("🐰아이디 : " + name);
		} else {
			response.getWriter().write("일치하는 닉네임이 없습니다.");
		}	
	
	}


}
