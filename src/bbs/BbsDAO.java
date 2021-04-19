package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BbsDAO {
	
	private Connection conn;
	private ResultSet rs;
	
	public BbsDAO() {   //데이터베이스 연결하기
		try {
			String dbURL = "jdbc:mysql://localhost:3306/BBS?serverTimezone=UTC";
			String dbID = "root";
			String dbPassword = "asd008154";
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getDate() {  //현재 시간을 가져오는 함수
		
		String SQL = "SELECT NOW()";  //현재 시간을 가져오는 mySQL 문장
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  // 데이터베이스 간 충돌방지
			rs = pstmt.executeQuery(); //실행 결과
			
			if(rs.next()) {
				return rs.getString(1);  
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ""; // 데이터베이스 오류
	}
	
	
	public int getNext() {  //bbsID 값을 가져오기 (번호)
		
		String SQL = "SELECT bbsID FROM BBS ORDER BY bbsID DESC";  //내림차순으로 가장 마지막에 쓰인 값
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  // 데이터베이스 간 충돌방지
			rs = pstmt.executeQuery(); //실행 결과
			
			if(rs.next()) {
				return rs.getInt(1)+1;
			}
			
			return 1;  // 첫번째 게시글인 경우
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
	
	public int write(String bbsTitle, String userID, String bbsContent) {  //게시글을 삽입
		String SQL = "INSERT INTO BBS VALUES (?, ?, ?, ?, ?, ?)"; //값 추가
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  // 데이터베이스 간 충돌방지
			pstmt.setInt(1, getNext());
			pstmt.setString(2, bbsTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsContent);
			pstmt.setInt(6, 1);   //available 여부(삭제 여부)
					
			return pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}

	
	
	public ArrayList<Bbs> getList(int pageNumber){
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10"; 
		ArrayList<Bbs> list = new ArrayList<Bbs>();
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  // 데이터베이스 간 충돌방지
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);  //getNext:다음으로 작성될 번호
			rs = pstmt.executeQuery(); //실행 결과
			
			while (rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				
				
				list.add(bbs);  //리스트에 해당 인스턴트를 담아 반환
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	public boolean nextPage(int pageNumber) {  //10단위로 끊기는 경우 페이징 처리를 위해
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  // 데이터베이스 간 충돌방지
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);  //getNext:다음으로 작성될 번호
			rs = pstmt.executeQuery(); //실행 결과
			
			if (rs.next()) {			
				return true;   //특정 페이지가 존재함
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Bbs getBbs(int bbsID) {    //글 내용을 불러오기 위함
String SQL = "SELECT * FROM BBS WHERE bbsID = ?";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  // 데이터베이스 간 충돌방지
			pstmt.setInt(1,bbsID);  //BBS ID의 값
			rs = pstmt.executeQuery(); //실행 결과
			
			if (rs.next()) {			 //결과가 나왔다면
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				
				return bbs;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public int update(int bbsID, String bbsTitle, String bbsContent) {
		String SQL = "UPDATE BBS SET bbsTitle = ?, bbsContent = ? WHERE bbsID = ?"; //값 추가
		//특정 ID에 해당하는 bbsTItle과 bbsContent의 값을 바꿔주겠다.
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  // 데이터베이스 간 충돌방지
			pstmt.setString(1, bbsTitle);
			pstmt.setString(2, bbsContent);
			pstmt.setInt(3, bbsID);
					
			return pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
	
	public int delete(int bbsID) {
		String SQL = "UPDATE BBS SET bbsAvailable = 0 WHERE bbsID = ?"; //값 추가
		//특정 ID에 해당하는 bbsTItle과 bbsContent의 값을 바꿔주겠다.
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  // 데이터베이스 간 충돌방지
			pstmt.setInt(1, bbsID);
			return pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
}























