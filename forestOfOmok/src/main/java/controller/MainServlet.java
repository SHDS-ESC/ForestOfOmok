package controller;
import java.io.IOException;
import java.io.PrintWriter;
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
import model.Game;
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
			HttpSession session = request.getSession();
			String userId = (String) session.getAttribute("userId"); // 세션에서 가져오는 부분 유지

			JSONObject userProfile = new JSONObject(dao.getProfile(userId));
			System.out.println(userProfile);
			out.print(userProfile.toString());
		} else if("main/createRoom".equals(cmd)) {
			System.out.println("방 만들기 서블릿");
		    HttpSession session = request.getSession();
		    String gameId = request.getParameter("gameId"); // 클라이언트에서 안 넘김
		    String ownerId = (String) session.getAttribute("userId");
		    String title = request.getParameter("title");

		    Game game = new Game();
		    game.setOwnerId(Integer.parseInt(ownerId)); // 세션에서 가져온 값
		    game.setTitle(title);

		    UserDAO2 dao = new UserDAO2Impl();
		    Game createdGame = dao.createRoom(game); // 시퀀스 적용된 Game 객체 반환

		    // JSON 응답
		    JSONObject obj = new JSONObject();
		    obj.put("gameId", createdGame.getGameId());
		    obj.put("title", createdGame.getTitle());
		    obj.put("ownerId", createdGame.getOwnerId());
		    obj.put("guestId", createdGame.getGuestId());

		    response.setContentType("application/json;charset=utf-8");
		    out.print(obj.toString()); // ★ 응답!
		} else if("main/getRoomList".equals(cmd)) {
			UserDAO2 dao = new UserDAO2Impl();
			List<Game> gameList = dao.getGameList();
			
			JSONArray arr = new JSONArray();
			for(Game g : gameList) {
				JSONObject obj = new JSONObject();
				obj.put("gameId",g.getGameId());
		        obj.put("ownerId", g.getOwnerId());
		        obj.put("guestId", g.getGuestId());
		        obj.put("title", g.getTitle());
		        arr.put(obj);
			}
			response.setContentType("application/json;charset=utf-8");
		    out.print(arr.toString());
		}
		else {
			System.out.println("프로필 조회");
			request.getRequestDispatcher("/html/main.html").forward(request, response);
		}
		
	
	}
}
