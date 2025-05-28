package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.OmokBoard;

/* 
 * 오목 게임방 서블릿 
 * /room?gameId=value
 */
@WebServlet("/room")
public class OmokGameServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		
		// 방 처리 로직..
		String gameId = request.getParameter("gameId");
		
		
		
		// 방으로 이동 시키기
		request.getRequestDispatcher("/html/game.html").forward(request, response);
		
		
		
		
		
	}
	
}
