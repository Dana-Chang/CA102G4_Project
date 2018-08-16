package com.productReport.model;

import java.sql.Timestamp;

public class ProductReportVO {
	private Integer prod_report_product_id;
	private String prod_report_mem_id;
	private String prod_report_reason;
	private Timestamp prod_report_time;
	private Integer prod_report_status;
	
	public Integer getProd_report_product_id() {
		return prod_report_product_id;
	}
	public void setProd_report_product_id(Integer prod_report_product_id) {
		this.prod_report_product_id = prod_report_product_id;
	}
	public String getProd_report_mem_id() {
		return prod_report_mem_id;
	}
	public void setProd_report_mem_id(String prod_report_mem_id) {
		this.prod_report_mem_id = prod_report_mem_id;
	}
	public String getProd_report_reason() {
		return prod_report_reason;
	}
	public void setProd_report_reason(String prod_report_reason) {
		this.prod_report_reason = prod_report_reason;
	}
	public Integer getProd_report_status() {
		return prod_report_status;
	}
	public void setProd_report_status(Integer prod_report_status) {
		this.prod_report_status = prod_report_status;
	}
	public Timestamp getProd_report_time() {
		return prod_report_time;
	}
	public void setProd_report_time(Timestamp prod_report_time) {
		this.prod_report_time = prod_report_time;
	}
	
}
