package com.photo_report.model;

import java.sql.Timestamp;

public class Photo_reportVO implements java.io.Serializable {
	private String mem_Id;
	private String photo_No;
	private String report_Reason;
	private Timestamp report_Time;
	private Integer pho_Rep_Stats;
	
	public Photo_reportVO() {
		
	}

	public String getMem_Id() {
		return mem_Id;
	}

	public void setMem_Id(String mem_Id) {
		this.mem_Id = mem_Id;
	}

	public String getPhoto_No() {
		return photo_No;
	}

	public void setPhoto_No(String photo_No) {
		this.photo_No = photo_No;
	}

	public String getReport_Reason() {
		return report_Reason;
	}

	public void setReport_Reason(String report_Reason) {
		this.report_Reason = report_Reason;
	}

	public Timestamp getReport_Time() {
		return report_Time;
	}

	public void setReport_Time(Timestamp report_Time) {
		this.report_Time = report_Time;
	}

	public Integer getPho_Rep_Stats() {
		return pho_Rep_Stats;
	}

	public void setPho_Rep_Stats(Integer pho_Rep_Stats) {
		this.pho_Rep_Stats = pho_Rep_Stats;
	}

	

}
