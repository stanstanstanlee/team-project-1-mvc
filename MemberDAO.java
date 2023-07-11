package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;


public class MemberDAO {
	// 회원가입
	static final String sql_insert="INSERT INTO MEMBER (ID,PW,NAME) VALUES(?,?,?)";
	// 비밀번호 변경
	static final String sql_update="UPDATE MEMBER SET PW=? WHERE ID=? AND PW <> ?";
	// 회원 탈퇴
	static final String sql_delete="DELETE FROM MEMBER WHERE ID=?";
	// 로그인
	static final String sql_selectOne="SELECT ID,PW,NAME,IS_ADMIN FROM MEMBER WHERE ID=? AND PW=?";
	// 회원 목록 출력(관리자)
	static final String sql_selectAll="SELECT ID,PW,NAME FROM MEMBER WHERE IS_ADMIN <> 1";

	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;

	// 회원가입
	public boolean insert(MemberVO mVO) {
		try {
			conn=JDBCUtil.connect();
			try {
				pstmt=conn.prepareStatement(sql_insert);
				pstmt.setString(1, mVO.getMid());
				pstmt.setString(2, mVO.getMpw());
				pstmt.setString(3, mVO.getName());
				int res = pstmt.executeUpdate();
				if(res <= 0) {
					return false;
				}
			}
			catch (SQLIntegrityConstraintViolationException e) {
//				e.printStackTrace();
				return false;
			}
			catch (SQLException e){
				e.printStackTrace();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
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
		return true;
	}


	// 비밀번호 변경
	public boolean update(MemberVO mVO) {
		try {
			conn=JDBCUtil.connect();
			try {
				pstmt=conn.prepareStatement(sql_update);	
				pstmt.setString(1, mVO.getTmpMpw());
				pstmt.setString(2, mVO.getMid());
				pstmt.setString(3, mVO.getTmpMpw());

				int res=pstmt.executeUpdate();
				if(res<=0) {
					return false;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
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
		return true;
	}


	// 회원 탈퇴
	public boolean delete(MemberVO mVO) {
		try {
			conn=JDBCUtil.connect();
			try {
				pstmt=conn.prepareStatement(sql_delete);
				pstmt.setString(1, mVO.getMid());
				int res=pstmt.executeUpdate();
				if(res<=0) {
					return false;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
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
		return true;
	}


	// 회원 목록
	public ArrayList<MemberVO> selectAll(MemberVO mVO){
		ArrayList<MemberVO> mdatas = new ArrayList<MemberVO>();
		try {
			conn=JDBCUtil.connect();
			try {
				pstmt=conn.prepareStatement(sql_selectAll);
				rs=pstmt.executeQuery();

				while(rs.next()) {
					mdatas.add(new MemberVO(rs.getString("ID"),rs.getString("PW"),rs.getString("NAME")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
					return null;
				}
				return null;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			JDBCUtil.disconnect(pstmt, conn);
		}
		return mdatas;
	}




	// 로그인
	public MemberVO selectOne(MemberVO mVO){
		MemberVO mdata=new MemberVO();
		try {
			conn=JDBCUtil.connect();
			try {
				pstmt=conn.prepareStatement(sql_selectOne);
				pstmt.setString(1, mVO.getMid());
				pstmt.setString(2, mVO.getMpw());
				rs=pstmt.executeQuery();

				if(rs.next()) {
					mdata=new MemberVO(rs.getString("ID"),rs.getString("PW"),rs.getString("NAME"),rs.getInt("IS_ADMIN"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
					return null;
				}
				return null;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			JDBCUtil.disconnect(pstmt, conn);
		}
		return mdata;
	}
}
