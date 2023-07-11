package model;

import java.util.Comparator;






class TitleComparator implements Comparator<AnnounceVO> {
	@Override
	public int compare(AnnounceVO aVO1, AnnounceVO aVO2) {
		return aVO1.getTitle().compareTo(aVO2.getTitle()); 
	}
}




public class AnnounceVO {
	private int num;
	private String company;
	private String title;
	private String date;
	private int view;	
	private int applyCnt;	
	private String serchCondition; // 임시변수
	
	
	
	public AnnounceVO() {
		this(0,"","","",0,0);
	}
	public AnnounceVO(int num,String company,String title,String date,int view) {
		this(num,company,title,date,view,0);
	}
	public AnnounceVO(int num,String company,String title,String date,int view,int applyCnt) {
		this.num=num;      // Pk
		this.company=company;  // 회사명
		this.title=title;		//공고명
		this.date=date;    // 기간
		this.view=view;         // 조회수
		this.applyCnt=applyCnt;	// 지원수
		this.serchCondition="";
	}
	

	public String getSerchCondition() {
		return serchCondition;
	}

	public void setSerchCondition(String serchCondition) {
		this.serchCondition = serchCondition;
	}

	public int getapplycnt() {
		return applyCnt;
	}
	public void setapplycnt(int applyCnt) {
		this.applyCnt = applyCnt;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getview() {
		return view;
	}

	public void setview(int view) {
		this.view = view;
	}

	@Override
	public String toString() {
		String announce = String.format("%-12s", this.company) +" | "
						+ String.format("%-30s", this.title) +" | "
						+ String.format("%-12s", this.date+" 까지") +" | "
						+ String.format("%-1s", this.view) +" | "
						+ String.format("%-3s", this.applyCnt+" 명");
		return announce;
	}
}


