package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BbsDAO {
	
	private Connection conn;
	private ResultSet rs;
	
	public BbsDAO() {   //�����ͺ��̽� �����ϱ�
		try {
			String dbURL = "jdbc:mysql://localhost:3306/BBS?serverTimezone=UTC";
			String dbID = "root";
			String dbPassword = "asd008154";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public String getDate() {  //���� �ð��� �������� �Լ�
		
		String SQL = "SELECT NOW()";  //���� �ð��� �������� mySQL ����
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  // �����ͺ��̽� �� �浹����
			rs = pstmt.executeQuery(); //���� ���
			
			if(rs.next()) {
				return rs.getString(1);  
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ""; // �����ͺ��̽� ����
	}
	
	
	public int getNext() {  //bbsID ���� �������� (��ȣ)
		
		String SQL = "SELECT bbsID FROM BBS ORDER BY bbsID DESC";  //������������ ���� �������� ���� ��
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  // �����ͺ��̽� �� �浹����
			rs = pstmt.executeQuery(); //���� ���
			
			if(rs.next()) {
				return rs.getInt(1)+1;
			}
			
			return 1;  // ù��° �Խñ��� ���
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // �����ͺ��̽� ����
	}
	
	public int write(String bbsTitle, String userID, String bbsContent) {  //�Խñ��� ����
		String SQL = "INSERT INTO BBS VALUES (?, ?, ?, ?, ?, ?)"; //�� �߰�
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  // �����ͺ��̽� �� �浹����
			pstmt.setInt(1, getNext());
			pstmt.setString(2, bbsTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsContent);
			pstmt.setInt(6, 1);   //available ����(���� ����)
					
			return pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // �����ͺ��̽� ����
	}

}























