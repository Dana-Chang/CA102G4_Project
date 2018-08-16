package com.grp_report.model;

import java.sql.Timestamp;

public class Grp_reportVO implements java.io.Serializable{
	private String grp_Id;
	private String mem_Id;
	private String report_Reason;
	private Timestamp report_Time;
	private Integer grp_Rep_Sta;
	
	public Grp_reportVO() {
		
	}

	public String getGrp_Id() {
		return grp_Id;
	}

	public void setGrp_Id(String grp_Id) {
		this.grp_Id = grp_Id;
	}

	public String getMem_Id() {
		return mem_Id;
	}

	public void setMem_Id(String mem_Id) {
		this.mem_Id = mem_Id;
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

	public Integer getGrp_Rep_Sta() {
		return grp_Rep_Sta;
	}

	public void setGrp_Rep_Sta(Integer grp_Rep_Sta) {
		this.grp_Rep_Sta = grp_Rep_Sta;
	}

	
	
}
