package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import service.OmokService;

/*
 * 오목 턴 마다 호출되는 서블릿
 * json으로 데이터 받음
 * 
 */

@WebServlet("/omokTurn")
public class OmokTurnServlet extends HttpServlet {
	
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");

		// 1. JSON 데이터 읽기
		StringBuilder sb = new StringBuilder();
		String line;
		BufferedReader reader = request.getReader();
		while ((line = reader.readLine()) != null) {
		    sb.append(line);
		}
		String jsonData = sb.toString();
		
		// 2. JSON 파싱 (org.json 사용 예시)
		JSONObject json = new JSONObject(jsonData);
		String gameId = json.getString("gameId");
		int row = json.getInt("row");
		int col = json.getInt("col");
		
		// 3. 로직 처리
//		OmokService.omokTurn(gameId, row, col);
		
		
		// 4. 응답
		response.setContentType("text/plain; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.write("서버에서 받은 row,col: " + row + "," + col);
		out.print("서버에서 받은 row,col: " + row + "," + col);
		out.flush();
		
		System.out.println("서버에서 받은 row,col: " + row + "," + col);
	}
	
	
}
