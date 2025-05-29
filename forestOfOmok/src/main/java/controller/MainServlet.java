package controller;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import dao.UserDAO2;
import dao.UserDAO2Impl;
@WebServlet("/main/*")
public class MainServlet extends HttpServlet {
	public MainServlet() {
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
		PrintWriter out = response.getWriter();
		String prefix = "/forestOfOmok/";
		String cmd = request.getRequestURI().substring(prefix.length());
		
		System.out.println(prefix);
		System.out.println(cmd);
		
		String page = "";
		
		if("main/profile".equals(cmd)) {
			System.out.println("main 도착");
			UserDAO2 dao = new UserDAO2Impl();
			// 세션에 저장된 아이디를 매개변수로 주어야함
			JSONObject userProfile = new JSONObject(dao.getProfile("1"));
			System.out.println(userProfile);
			out.print(userProfile.toString());
		} else {
			System.out.println("프로필 조회");
			request.getRequestDispatcher("/html/main.html").forward(request, response);
		}
		
	
	}
}
