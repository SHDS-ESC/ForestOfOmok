package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/* 
 * 오목 게임방 서블릿 
 * /room?gameId=value
 */
@WebServlet("/game")
public class OmokGameRoomServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		
		// 방 처리 로직..
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");
		String gameId = (String) session.getAttribute("gameId");
//		response.sendRedirect(request.getContextPath() + "/html/game.html?gameId=" + gameId);
		
		request.setAttribute("userId", userId);
		request.setAttribute("gameId", gameId);
		request.getRequestDispatcher("/game/room.jsp").forward(request, response);
		
	}
	
}
