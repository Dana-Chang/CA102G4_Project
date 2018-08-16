package com.productReport.model;

import java.sql.Timestamp;
import java.util.List;

public class ProductReportService {
	private ProductReportDAO_interface dao;

	public ProductReportService() {
		dao = new ProductReportDAO();
	}

	public ProductReportVO addProductReport(Integer prod_report_product_id,String prod_report_mem_id,String prod_report_reason,
			Timestamp prod_report_time,Integer prod_report_status) {

		ProductReportVO productReportVO = new ProductReportVO();
		
		productReportVO.setProd_report_product_id(prod_report_product_id);
		productReportVO.setProd_report_mem_id(prod_report_mem_id);
		productReportVO.setProd_report_reason(prod_report_reason);
		productReportVO.setProd_report_time(prod_report_time);
		productReportVO.setProd_report_status(prod_report_status);
		

		dao.insert(productReportVO);

		return productReportVO;
	}

	public ProductReportVO updateProductReport(Integer prod_report_product_id,String prod_report_mem_id,String prod_report_reason,
			Timestamp prod_report_time,Integer prod_report_status) {

		ProductReportVO productReportVO = new ProductReportVO();
		
		productReportVO.setProd_report_product_id(prod_report_product_id);
		productReportVO.setProd_report_mem_id(prod_report_mem_id);
		productReportVO.setProd_report_reason(prod_report_reason);
		productReportVO.setProd_report_time(prod_report_time);
		productReportVO.setProd_report_status(prod_report_status);

		dao.update(productReportVO);

		return productReportVO;
	}
	
	public ProductReportVO updateProductReport(ProductReportVO productReportVO) {

		dao.update(productReportVO);

		return productReportVO;
	}

	public void deleteProductReport(Integer prod_report_product_id, String prod_report_mem_id) {
		dao.delete(prod_report_product_id,prod_report_mem_id);
	}

	public ProductReportVO getOneProductReport(Integer prod_report_product_id, String prod_report_mem_id) {
		return dao.findByPK(prod_report_product_id,prod_report_mem_id);
	}

	public List<ProductReportVO> getAll() {
		return dao.getAll();
	}
}
