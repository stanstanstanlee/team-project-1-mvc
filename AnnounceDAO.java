package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
//import java.util.Collections;

public class AnnounceDAO {


	final String sql_INSERT="INSERT INTO ANNOUNCE (COMPANY,TITLE,LIMITDATE) VALUES(?,?,?)";
	
	final String sql_UPDATE_NAME="UPDATE ANNOUNCE SET TITLE = ? WHERE NUM= ?";	
	final String sql_UPDATE_VIEW="UPDATE ANNOUNCE SET VIEW=VIEW+1 WHERE NUM= ?";	
	
	final String sql_DELETE="DELETE FROM ANNOUNCE WHERE NUM= ?";	
	
	final String sql_SELECTALL="SELECT ANNOUNCE.NUM,COMPANY,TITLE,(SELECT SUBSTRING(LIMITDATE,1,10)) AS LIMITDATE,VIEW,COUNT(APPLY.ANUM) AS APPLYCNT FROM ANNOUNCE"
							+ " LEFT JOIN APPLY ON ANNOUNCE.NUM = APPLY.ANUM GROUP BY ANNOUNCE.NUM";
	
	final String sql_SELECTALL_NAME="SELECT ANNOUNCE.NUM, COMPANY, TITLE, SUBSTRING(LIMITDATE,1,10) AS LIMITDATE, VIEW,COUNT(APPLY.ANUM) AS APPLYCNT FROM ANNOUNCE"
							+ " LEFT JOIN APPLY ON ANNOUNCE.NUM = APPLY.ANUM WHERE ANNOUNCE.TITLE LIKE CONCAT('%' ? '%') GROUP BY ANNOUNCE.NUM;";
	
	final String sql_SELECTONE="SELECT ANNOUNCE.NUM,COMPANY,TITLE,SUBSTRING(LIMITDATE,1,10) AS LIMITDATE,VIEW,COUNT(APPLY.ANUM) AS APPLYCNT FROM ANNOUNCE LEFT JOIN APPLY ON ANNOUNCE.NUM = APPLY.ANUM WHERE ANNOUNCE.NUM=?";

	
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	
	public boolean insert(AnnounceVO aVO) {
		try {
			conn=JDBCUtil.connect();			
			if(aVO.getSerchCondition().equals("크롤링")) {
				ArrayList<AnnounceVO> datas=Crawling.sample();
				pstmt=conn.prepareStatement(sql_INSERT);
				for(int i=0;i<datas.size();i++) {
					try {
						pstmt.setString(1,datas.get(i).getCompany());
						pstmt.setString(2,datas.get(i).getTitle());
						pstmt.setString(3,datas.get(i).getDate());
						int res = pstmt.executeUpdate();
						if(res <= 0) {
							return false;
						}
					} 
					// SQL문 오류시
					catch (SQLException e) {
						e.printStackTrace();
						return false;
					}
				}
				return true;
			}
			if(aVO.getSerchCondition().equals("공고추가")) {
				try {
					pstmt=conn.prepareStatement(sql_INSERT);
					pstmt.setString(1,aVO.getCompany());
					pstmt.setString(2,aVO.getTitle());
					pstmt.setString(3,aVO.getDate());
					int res = pstmt.executeUpdate();
					if(res <= 0) {
						return false;
					}
					return true;
				}
				// SQL문 오류시
				catch(SQLException e) {
					e.printStackTrace();
					return false;
				}	
			}
		}
		// conn 연결 실패
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			JDBCUtil.disconnect(pstmt, conn);
		}		
		// 크롤링, 공고추가 두가지 모두 해당사항 없을때
		System.out.println("로그 : AnnounceDAO() INSERT 전체실패 ");
		return false;
	}

	
	
	
	public boolean update(AnnounceVO aVO) {	
		try {
			conn=JDBCUtil.connect();	
			if(aVO.getSerchCondition().equals("공고명변경")) {				
				try {
					pstmt=conn.prepareStatement(sql_UPDATE_NAME);
					pstmt.setString(1,aVO.getTitle());
					pstmt.setInt(2,aVO.getNum());
					int res = pstmt.executeUpdate();
					if(res <= 0) {
						return false;
					}
					return true;
				}
				// SQL문 오류시 
				catch(SQLException e) {
					e.printStackTrace();
					return false;
				}
			}
			else if (aVO.getSerchCondition().equals("조회수")) {
				try {
					pstmt=conn.prepareStatement(sql_UPDATE_VIEW);
					pstmt.setInt(1,aVO.getNum());
					int res = pstmt.executeUpdate();
					if(res <= 0) {
						return false;
					}
					return true;
				}
				// SQL문 오류시 
				catch(SQLException e) {
					e.printStackTrace();
					return false;
				}
			}
			
		}
		// conn 연결 실패
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {		
			JDBCUtil.disconnect(pstmt, conn);
		}	
		return false;
	}
	
	
	public boolean delete(AnnounceVO aVO) {
		try {
			conn=JDBCUtil.connect();					
			try {
				pstmt=conn.prepareStatement(sql_DELETE);
				pstmt.setInt(1,aVO.getNum());
				int res = pstmt.executeUpdate();
				if(res <= 0) {
					return false;
				}
				return true;
			}
			// SQL문 오류시
			catch(SQLException e) {
				e.printStackTrace();
				return false;
			}			
		}
		// conn 연결 실패
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {		
			JDBCUtil.disconnect(pstmt, conn);
		}				
	}

	
	
	public ArrayList<AnnounceVO> selectAll(AnnounceVO aVO){
		// 선택한 데이터 저장하는 mdatas 배열
		ArrayList<AnnounceVO> mdatas=new ArrayList<AnnounceVO>();
		try {
			conn=JDBCUtil.connect();			
			try {
				if(aVO.getSerchCondition()=="전체출력") {
					pstmt=conn.prepareStatement(sql_SELECTALL);						
					rs=pstmt.executeQuery();
				}				
				else if(aVO.getSerchCondition()=="이름검색") {							
					pstmt=conn.prepareStatement(sql_SELECTALL_NAME);
					pstmt.setString(1,aVO.getTitle());					
					rs=pstmt.executeQuery();
				}
				while(rs.next()) {
					int num=rs.getInt("NUM");	// 번호 넣기
					String company=rs.getString("COMPANY");	// 회사명 넣기
					String title=rs.getString("TITLE");		// 공고명 넣기
					String date=rs.getString("LIMITDATE");	// 날짜 넣기
					int view=rs.getInt("VIEW");	// 날짜 넣기
					int applyCnt = rs.getInt("APPLYCNT");
					// mdatas에 조건을 만족하는 데이터 넣기
					mdatas.add(new AnnounceVO(num,company,title,date,view,applyCnt));		
				}						
			}
			// SQL문 오류시
			catch(SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {		
			JDBCUtil.disconnect(rs,pstmt, conn);
		}
		return mdatas;
	}
	
	
	
	public AnnounceVO selectOne(AnnounceVO aVO) {
		AnnounceVO adata = new AnnounceVO();
		try {
			conn=JDBCUtil.connect();	
			try {
				pstmt=conn.prepareStatement(sql_SELECTONE);
				pstmt.setInt(1,aVO.getNum());
				rs=pstmt.executeQuery(); 
				if(rs.next()) {
					int num=rs.getInt("NUM");
					String company=rs.getString("COMPANY");	// 회사명 넣기
					String title=rs.getString("TITLE");		// 공고명 넣기
					String date=rs.getString("LIMITDATE");// 날짜 넣기
					int view=rs.getInt("VIEW");
					int applyCnt = rs.getInt("APPLYCNT");
					// mVO에 조건을 만족하는 데이터 넣기
					adata=new AnnounceVO(num,company,title,date,view,applyCnt); 						
				}						
			}
			// SQL문 오류시
			catch(SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		// conn 연결 실패
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {		
			JDBCUtil.disconnect(rs,pstmt, conn);
		}
		return adata;
	}
}
