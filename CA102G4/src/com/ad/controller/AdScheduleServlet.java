package com.ad.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import com.ad.model.*;

public class AdScheduleServlet extends HttpServlet {
	
	Timer timer ; 
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req,res);
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
	}
	
	//伺服器載入Servlet後，建立實體完，進行初始化工作
	public void init() {
		//建立排程器物件
		timer = new Timer();
		SimpleDateFormat time_format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		
		//設定任務內容--
		//去資料庫撈廣告的上下架時間，若時間符合將修改上下架狀況
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				//取得現在時間
				long nowTime = System.currentTimeMillis();
//				System.out.println(System.currentTimeMillis());
				AdService adSvc = new AdService();
				List<AdVO> list = adSvc.getAllAD();

				long onTime = 0;
				long offTime = 0 ;
				System.out.println("***************start****************************");
				for(AdVO advo : list) {
					//如果預計上架時間及下架時間都不為空值時
					if(advo.getAd_PreAddTime() != null && advo.getAd_PreOffTime() != null) {
						onTime = advo.getAd_PreAddTime().getTime();
						offTime= advo.getAd_PreOffTime().getTime();
						
//						System.out.println(advo.getAd_ID()+"=執行時間："+nowTime+"("+time_format.format(nowTime)+")；預計上架時間："
//						+onTime+"("+time_format.format(onTime)+")；預計下架時間："+offTime+"("+time_format.format(offTime)+")");
						
						if( ((nowTime-1000) <= onTime && onTime <= nowTime) && advo.getAd_Stat() == 0) {
							adSvc.updateAD(advo.getAd_ID(), 1,advo);
							System.out.println("已順利將"+advo.getAd_ID()+"上架了");
						}else if(((nowTime-1000) <= offTime && offTime <= nowTime) && advo.getAd_Stat() == 1){
							adSvc.updateAD(advo.getAd_ID(), 0,advo);
							System.out.println("已順利將"+advo.getAd_ID()+"下架了");
						}
						
						
					}else if(advo.getAd_PreAddTime() != null && advo.getAd_PreOffTime() == null) {
						//如果預計上架時間有值，但下架時間未定時
						onTime = advo.getAd_PreAddTime().getTime();
//						System.out.println(advo.getAd_ID()+"=執行時間："+nowTime+"("+time_format.format(nowTime)+")；預計上架時間："
//						+onTime+"("+time_format.format(onTime)+")");
						
						if(((nowTime-1000) <= onTime && onTime <= nowTime) && advo.getAd_Stat() == 0) {
							adSvc.updateAD(advo.getAd_ID(), 1,advo);
							System.out.println("已順利將"+advo.getAd_ID()+"上架了");
						}
					}
				}
				System.out.println("***************end****************************");
			}	
		};
		Date start_time = new Date();
		
//		//宣告開始時間
		Calendar cal = new GregorianCalendar(2018,start_time.getMonth(),start_time.getDate(),start_time.getHours(),start_time.getMinutes(),0);
			

		
		
		//幫排程器設定開始時間後，間隔多久做一次(1min檢查一次)
		timer.scheduleAtFixedRate(task,cal.getTime(),1*60*1000);
		System.out.println("開啟伺服器時間："+time_format.format(start_time)+";排程器時間:"+cal.getTime());
		
	}
	
	//container關閉時，會呼叫的
	public void destroy() {
		timer.cancel();
		System.out.println("排程器結束");
	}
}
