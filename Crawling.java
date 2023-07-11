package model;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class Crawling {

	public static ArrayList<AnnounceVO> sample() {
		//		public static void main(String[] args) {

		final String url = "https://www.jobkorea.co.kr/top100/";
		Connection conn = Jsoup.connect(url);
		Document doc = null;

		try {
			doc = conn.get();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//		System.out.println(doc);

//		Elements elems1 = doc.select("b");
//		Elements elems2 = doc.select("div.info > div.tit > a >span");
//		Elements elems3 = doc.select("span.day");

		Elements elems1 = doc.select("div.rankListArea.devSarterTab > ol > li > div.co > div > a > b");
		Elements elems2 = doc.select("div.rankListArea.devSarterTab > ol > li > div.info > div.tit > a >span");
		Elements elems3 = doc.select("div.rankListArea.devSarterTab > ol > li > div.side > span.day");

		Iterator<Element> itr1 = elems1.iterator();
		Iterator<Element> itr2 = elems2.iterator();
		Iterator<Element> itr3 = elems3.iterator();


		ArrayList<AnnounceVO> mdatas=new ArrayList<AnnounceVO>();


		while(itr1.hasNext() && itr2.hasNext() && itr3.hasNext() ) {
			String str1 = itr1.next().text();
			String str2 = itr2.next().text();
			String str3 = itr3.next().text();
			try {
				if (str3.contains("오늘")) {
					LocalDateTime todayDateTime = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(),
																	LocalDateTime.now().getDayOfMonth(), 0, 0);
					Timestamp timestamp3 = Timestamp.valueOf(todayDateTime);
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					str3 = dateFormat.format(timestamp3);
				}
				else if(str3.contains("내일")) {
					LocalDateTime todayDateTime = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(),
																	LocalDateTime.now().getDayOfMonth()+1, 0, 0);
					Timestamp timestamp3 = Timestamp.valueOf(todayDateTime);
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					str3 = dateFormat.format(timestamp3);
				}
				else if(str3.contains("상시채용")){
					str3 = "2023/12/31 00:00:00";
				}
				else {
					str3 = LocalDateTime.now().getYear()+"/"+str3.substring(1,6)+" 00:00:00";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			mdatas.add(new AnnounceVO(0,str1,str2,str3,0));
		}
		return mdatas;
	}
}
