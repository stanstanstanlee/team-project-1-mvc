package model;

public class MemberVO {
	private String mid; // PK
	private String mpw;
	private String name;
	private int is_admin;
	
	// -- DB_테이블에는 없지만 개발 편의성때문에 JAVA_VO에서 구현한 멤버변수
	private String tmpMpw;
	private String searchCondition;
	// ------------------------------------------------------
		
	
	public MemberVO() {
		this(null,null,null,0);
	}
	
	public MemberVO(String mid,String mpw,String name) {
		this(mid,mpw,name,0);
	}
	
	public MemberVO(String mid,String mpw,String name,int is_admin) {
		this.mid=mid;
		this.mpw=mpw;
		this.name=name;
		this.is_admin=is_admin;
		
	}
	

	public int getIs_admin() {
		return is_admin;
	}

	public void setIs_admin(int is_admin) {
		this.is_admin = is_admin;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getMpw() {
		return mpw;
	}

	public void setMpw(String mpw) {
		this.mpw = mpw;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getTmpMpw() {
		return tmpMpw;
	}

	public void setTmpMpw(String tmpMpw) {
		this.tmpMpw = tmpMpw;
	}

	public String getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}
	@Override
	public String toString() {
		String mid = String.format("%-10s", this.mid);
		String mpw = String.format("%-10s", this.mpw);
		String name = String.format("%-6s", this.name);
		return mid+" | "+mpw+" | "+name;
	}

	@Override
	public boolean equals(Object obj) {		
		MemberVO mVO=(MemberVO)obj;
		if(this.mid.equals(mVO.mid)) {
			return true;
		}
		return false;
	}
}
