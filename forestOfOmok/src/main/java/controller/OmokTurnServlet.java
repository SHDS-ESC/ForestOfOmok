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

import service.OmokGameService;

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
		response.setContentType("application/json");

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
		String roomId = json.getString("roomId");
		int row = json.getInt("row");
		int col = json.getInt("col");
		
		// 3. 로직 처리
		// OmokGameService 클래스에서 로직 처리 후, 결과 값 다시 프론트로 보내기 -> json에 담으면 됨
		// result 종류 -> 일반 진행, 둘 수 없는 곳, 승부 결정 등...
		String result = OmokGameService.omokTurn(roomId, row, col);
		JSONObject jResult = new JSONObject();
		jResult.put("result", result);
		jResult.put("roomId", roomId);
		jResult.put("row", row);
		jResult.put("col", col);
		
		// 4. 응답
		PrintWriter out = response.getWriter();
		out.write(jResult.toString());
		out.flush();
		
		System.out.println("RoomID " + roomId);
		System.out.println("서버에서 받은 row,col: " + row + "," + col);
	}
	
	
}
