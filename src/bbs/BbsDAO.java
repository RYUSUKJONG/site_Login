package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BbsDAO {
	
	private Connection conn;
	private ResultSet rs;
	
	public BbsDAO() {   //�����ͺ��̽� �����ϱ�
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

	
	
	public ArrayList<Bbs> getList(int pageNumber){
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10"; 
		ArrayList<Bbs> list = new ArrayList<Bbs>();
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  // �����ͺ��̽� �� �浹����
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);  //getNext:�������� �ۼ��� ��ȣ
			rs = pstmt.executeQuery(); //���� ���
			
			while (rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				
				
				list.add(bbs);  //����Ʈ�� �ش� �ν���Ʈ�� ��� ��ȯ
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	public boolean nextPage(int pageNumber) {  //10������ ����� ��� ����¡ ó���� ����
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  // �����ͺ��̽� �� �浹����
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);  //getNext:�������� �ۼ��� ��ȣ
			rs = pstmt.executeQuery(); //���� ���
			
			if (rs.next()) {			
				return true;   //Ư�� �������� ������
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Bbs getBbs(int bbsID) {    //�� ������ �ҷ����� ����
String SQL = "SELECT * FROM BBS WHERE bbsID = ?";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  // �����ͺ��̽� �� �浹����
			pstmt.setInt(1,bbsID);  //BBS ID�� ��
			rs = pstmt.executeQuery(); //���� ���
			
			if (rs.next()) {			 //����� ���Դٸ�
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
		String SQL = "UPDATE BBS SET bbsTitle = ?, bbsContent = ? WHERE bbsID = ?"; //�� �߰�
		//Ư�� ID�� �ش��ϴ� bbsTItle�� bbsContent�� ���� �ٲ��ְڴ�.
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  // �����ͺ��̽� �� �浹����
			pstmt.setString(1, bbsTitle);
			pstmt.setString(2, bbsContent);
			pstmt.setInt(3, bbsID);
					
			return pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // �����ͺ��̽� ����
	}
	
	public int delete(int bbsID) {
		String SQL = "UPDATE BBS SET bbsAvailable = 0 WHERE bbsID = ?"; //�� �߰�
		//Ư�� ID�� �ش��ϴ� bbsTItle�� bbsContent�� ���� �ٲ��ְڴ�.
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  // �����ͺ��̽� �� �浹����
			pstmt.setInt(1, bbsID);
			return pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // �����ͺ��̽� ����
	}
}























