package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MFunctionDAO {	
	final String sql_INSERT_APPLY="INSERT INTO APPLY (MID,ANUM) VALUES(?,?) ON DUPLICATE KEY UPDATE APPLY_TIME = VALUES(APPLY_TIME)";
	final String sql_INSERT_SCRAP="INSERT INTO SCRAP (MID,ANUM) VALUES(?,?) ON DUPLICATE KEY UPDATE SCRAP_TIME = VALUES(SCRAP_TIME)";
	
	final String sql_SELECTALL_APPLY="SELECT ANNOUNCE.NUM,COMPANY,TITLE,APPLY_TIME AS TIME FROM APPLY INNER JOIN ANNOUNCE ON APPLY.ANUM = ANNOUNCE.NUM WHERE MID=? ORDER BY TIME";
	final String sql_SELECTALL_SCRAP="SELECT ANNOUNCE.NUM,COMPANY,TITLE,SCRAP_TIME AS TIME FROM SCRAP INNER JOIN ANNOUNCE ON SCRAP.ANUM = ANNOUNCE.NUM WHERE MID=? ORDER BY TIME";	
	
	final String sql_DELETE_APPLY="DELETE FROM APPLY WHERE ANUM=? AND MID=?";
	final String sql_DELETE_SCRAP="DELETE FROM SCRAP WHERE ANUM=? AND MID=?";
	
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;	
	
	// INSERT
	public boolean insert(MFunctionVO mfVO) {			
		try {
			conn=JDBCUtil.connect();
			if(mfVO.getSerchCondition().equals("APPLY")) {
				pstmt=conn.prepareStatement(sql_INSERT_APPLY);
				pstmt.setString(1, mfVO.getMid());
				pstmt.setInt(2, mfVO.getAnnounceNum());
			}
			else if(mfVO.getSerchCondition().equals("SCRAP")) {
				pstmt=conn.prepareStatement(sql_INSERT_SCRAP);
				pstmt.setString(1, mfVO.getMid());
				pstmt.setInt(2, mfVO.getAnnounceNum());
			}
			try {
				int res = pstmt.executeUpdate();	
				if(res==2) {					
					return false;
				}				 
				return true;
			}
			catch(SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}			
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			JDBCUtil.disconnect(pstmt, conn);
		}			
	}

	static boolean update(MFunctionVO mVO) {	
		return false;
	}
	
	// DELETE
	public boolean delete(MFunctionVO mfVO) {			

		try {
			conn=JDBCUtil.connect();

			if(mfVO.getSerchCondition().equals("APPLY")) {
				pstmt=conn.prepareStatement(sql_DELETE_APPLY);
				pstmt.setInt(1,mfVO.getNum());
				pstmt.setString(2, mfVO.getMid());
			}
			else if(mfVO.getSerchCondition().equals("SCRAP")) {
				pstmt=conn.prepareStatement(sql_DELETE_SCRAP);
				pstmt.setInt(1,mfVO.getNum());
				pstmt.setString(2, mfVO.getMid());
			}
			try {				
				int res = pstmt.executeUpdate();
				if(res <= 0) {
					return false;
				}
				return true;
			}
			catch(SQLException e) {
				e.printStackTrace();
				return false;
			}			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {		
			JDBCUtil.disconnect(pstmt, conn);
		}
		return false;
	}
	//==============================================================================	
	public ArrayList<MFunctionVO> selectAll(MFunctionVO mfVO){
		ArrayList<MFunctionVO> mfdatas=new ArrayList<MFunctionVO>();
		try {
			conn=JDBCUtil.connect();			
			try {
				
				if(mfVO.getSerchCondition().equals("APPLY")) {
					pstmt=conn.prepareStatement(sql_SELECTALL_APPLY);	
					pstmt.setString(1, mfVO.getMid());
					rs=pstmt.executeQuery(); 
				}
				else if(mfVO.getSerchCondition().equals("SCRAP")) {
					pstmt=conn.prepareStatement(sql_SELECTALL_SCRAP);
					pstmt.setString(1, mfVO.getMid());
					rs=pstmt.executeQuery(); 
				}					
				while(rs.next()) {
					int num=rs.getInt("NUM");
					String company=rs.getString("COMPANY");
					String title=rs.getString("TITLE");
					Date date=rs.getDate("TIME");
					MFunctionVO mdata = new MFunctionVO();
					mdata.setaNum(num);
					mdata.setaCompany(company);
					mdata.setaTitle(title);
					mdata.setDate(date);
					mfdatas.add(mdata);    
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
		return mfdatas;
		
	}
	
	//==============================================================================
	
	static MFunctionVO selectOne(MFunctionVO mfVO) {			
		return null;
	}
	//==============================================================================
}





