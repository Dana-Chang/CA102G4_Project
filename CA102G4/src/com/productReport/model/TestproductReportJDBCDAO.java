package com.productReport.model;

import java.sql.Timestamp;
import java.util.List;


import com.productWishlist.model.ProductWishlistVO;

public class TestproductReportJDBCDAO {

	public static void main(String[] args) {
		ProductReportJDBCDAO dao = new ProductReportJDBCDAO();
		
		// Insert
		ProductReportVO productReport1 = new ProductReportVO();
		productReport1.setProd_report_product_id(1002);
		productReport1.setProd_report_mem_id("M000003");
		productReport1.setProd_report_reason("�Ϥ夣��");
		productReport1.setProd_report_time(new Timestamp(System.currentTimeMillis()));
		productReport1.setProd_report_status(1);
		dao.insert(productReport1);
		System.out.println("---------------------");
		
		// �R��
		dao.delete(1002,"M000003");
		
		// �ק�
		ProductReportVO productReport2 = new ProductReportVO();
		productReport2.setProd_report_product_id(1001);
		productReport2.setProd_report_mem_id("M000006");
		productReport2.setProd_report_reason("�D�s�~");
		productReport2.setProd_report_time(new Timestamp(System.currentTimeMillis()));
		productReport2.setProd_report_status(1);
		
		dao.update(productReport2);

		// �d��
		ProductReportVO productReport3 = dao.findByPK(1001,"M000006");
		System.out.print(productReport3.getProd_report_product_id() + ",");
		System.out.print(productReport3.getProd_report_mem_id() + ",");
		System.out.print(productReport3.getProd_report_reason() + ",");
		System.out.print(productReport3.getProd_report_status());
		System.out.println();
		System.out.println("---------------------");
		
		// �d��
		List<ProductReportVO> list = dao.getAll();
		for (ProductReportVO productReport : list) {
			System.out.print(productReport.getProd_report_product_id() + ",");
			System.out.print(productReport.getProd_report_mem_id() + ",");
			System.out.print(productReport.getProd_report_reason() + ",");
			System.out.print(productReport.getProd_report_status());
			System.out.println();
		}


	}

}
