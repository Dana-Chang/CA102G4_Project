package com.productReport.model;

import java.util.List;



public interface ProductReportDAO_interface {
	public void insert(ProductReportVO productReportVO);
    public void update(ProductReportVO productReportVO);
    public void delete(Integer prod_report_product_id,String prod_report_mem_id);
	public List<ProductReportVO> getAll();
	public ProductReportVO findByPK(Integer prod_report_product_id,String prod_report_mem_id);
}
