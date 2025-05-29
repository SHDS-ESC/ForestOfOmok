package controller;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import dao.UserDAO2;
import dao.UserDAO2Impl;
import model.History;
import model.Rank;
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
		}else if("main/rank".equals(cmd)) {
			System.out.println("랭크 조회");
			UserDAO2 dao = new UserDAO2Impl();
			List<Rank> rankList = dao.getRankList(); // rank 리스트
			JSONObject totalObj = new JSONObject();
			JSONArray rankJsonArray = new JSONArray(); // json배열 담을 객체
			for(Rank rank : rankList) {
				JSONObject userObj = new JSONObject(rank); // Rank 객체 꺼내서 넣을 오브젝트
				rankJsonArray.put(userObj); // 오브젝트 JSONArray에 넣기
			}
			totalObj.put("rank", rankJsonArray);
			String jsonInfo = totalObj.toString();
			out.write(jsonInfo);
		} else if("main/history".equals(cmd)){
			UserDAO2 dao = new UserDAO2Impl();
			HttpSession session = request.getSession();
			String ownerId = (String) session.getAttribute("userId");
			List<History> historyList = dao.getHistoryList("1"); // 세션에서 가져오는 곳
			JSONObject totalObj = new JSONObject();
			JSONArray historyJsonArray = new JSONArray();
			for(History history : historyList) {
				JSONObject historyObj = new JSONObject(history);
				historyJsonArray.put(historyObj);
			}
			totalObj.put("history", historyJsonArray);
			String jsonInfo = totalObj.toString();
			out.write(jsonInfo);
		}else {
			System.out.println("프로필 조회");
			request.getRequestDispatcher("/html/main.html").forward(request, response);
		}
		
	
	}
}
