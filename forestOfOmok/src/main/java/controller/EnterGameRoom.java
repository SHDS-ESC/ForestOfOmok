package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/enter")
public class EnterGameRoom extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// db에서 방 테이블보고, 새로 방 만들고 id 받아내고...
		// 배정하고... 일련의 과정
		
		String roomId = "0";
		request.setAttribute("roomId", roomId);
		request.getRequestDispatcher("/room").forward(request, response);
	}
}
