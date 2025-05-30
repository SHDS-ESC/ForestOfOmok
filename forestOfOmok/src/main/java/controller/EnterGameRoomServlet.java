package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import webSocket.WebSocketServer;

@WebServlet("/enter")
public class EnterGameRoomServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int id = WebSocketServer.cnt;
		request.getSession().setAttribute("userId", Integer.toString(id));
		
		// db에서 방 테이블보고, 새로 방 만들고 id 받아내고...
		// 배정하고... 일련의 과정
		
		String userId = (String)request.getSession().getAttribute("userId");
		String gameId = Integer.toString(id/2+1);
//		request.setAttribute("userId", userId);
		request.getSession().setAttribute("gameId", gameId);
		request.getSession();
		request.getRequestDispatcher("/game").forward(request, response);
		
		System.out.println("[EnterServlet] userId : " + userId);
	}
	
}
