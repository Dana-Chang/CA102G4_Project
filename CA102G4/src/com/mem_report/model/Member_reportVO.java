package com.mem_report.model;

import java.sql.Timestamp;

public class Member_reportVO implements java.io.Serializable {
	
	private String mem_Id_report;
	private String mem_Id_reported;
	private Timestamp report_Time;
	private String report_Reason;
	private Integer mem_Rep_Sta;
	
	public Member_reportVO() {
		
	}

	public String getMem_Id_report() {
		return mem_Id_report;
	}

	public void setMem_Id_report(String mem_Id_report) {
		this.mem_Id_report = mem_Id_report;
	}

	public String getMem_Id_reported() {
		return mem_Id_reported;
	}

	public void setMem_Id_reported(String mem_Id_reported) {
		this.mem_Id_reported = mem_Id_reported;
	}

	public Timestamp getReport_Time() {
		return report_Time;
	}

	public void setReport_Time(Timestamp report_Time) {
		this.report_Time = report_Time;
	}

	public String getReport_Reason() {
		return report_Reason;
	}

	public void setReport_Reason(String report_Reason) {
		this.report_Reason = report_Reason;
	}

	public Integer getMem_Rep_Sta() {
		return mem_Rep_Sta;
	}

	public void setMem_Rep_Sta(Integer mem_Rep_Sta) {
		this.mem_Rep_Sta = mem_Rep_Sta;
	}
	
}
