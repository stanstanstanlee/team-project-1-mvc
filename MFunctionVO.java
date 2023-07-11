package model;

import java.sql.Date;

public class MFunctionVO {
	private int num; // PK
	private String Mid; // 멤버 아이디
	private int announceNum; // 공고넘버
	private Date date; // 날짜
	private String serchCondition; // 임시변수

	private int aNum;
	private String aCompany;
	private String aTitle;
	private int aView;

	public MFunctionVO() {
		this(0, null, 0, null);
	}
	public MFunctionVO(int num, String Mid, int announceNum, Date date) {
		this.num = num;
		this.Mid= Mid;
		this.announceNum = announceNum;
		this.date = date;
		this.serchCondition="";
		this.aNum = 0;
		this.aCompany = "";
		this.aTitle = "";
		this.aView = 0;
	}   

	public int getaNum() {
		return aNum;
	}
	public void setaNum(int aNum) {
		this.aNum = aNum;
	}
	public String getaCompany() {
		return aCompany;
	}
	public void setaCompany(String aCompany) {
		this.aCompany = aCompany;
	}
	public String getaTitle() {
		return aTitle;
	}
	public void setaTitle(String aTitle) {
		this.aTitle = aTitle;
	}
	public int getaView() {
		return aView;
	}
	public void setaView(int aView) {
		this.aView = aView;
	}
	public String getSerchCondition() {
		return serchCondition;
	}

	public void setSerchCondition(String serchCondition) {
		this.serchCondition = serchCondition;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	public String getMid() {
		return Mid;
	}
	public void setMid(String mid) {
		Mid = mid;
	}
	public int getAnnounceNum() {
		return announceNum;
	}


	public void setAnnounceNum(int announceNum) {
		this.announceNum = announceNum;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	@Override
	public String toString() { // toString 오버라이딩
		String mfunction = String.format("%-12s", this.aCompany) +" | "
					+ String.format("%-30s", this.aTitle) +" | "
					+ String.format("%-1s", this.aView);
					return mfunction;
	}
}