/*
 *  1. 萬用複合查詢-可由客戶端隨意增減任何想查詢的欄位
 *  2. 為了避免影響效能:
 *        所以動態產生萬用SQL的部份,本範例無意採用MetaData的方式,也只針對個別的Table自行視需要而個別製作之
 * */

package com.attractions.model;

import java.util.*;

public class CompositeQuery_Att {

	public static String get_aCondition_For_Oracle(String columnName, String value) {

		String aCondition = null;

		if ("att_name".equals(columnName) || "country".equals(columnName) || "att_address".equals(columnName) || "administrative_area".equals(columnName)) // 用於varchar
			aCondition = "upper(" + columnName + ")" + " like upper('%" + value + "%')";
			
		return aCondition + " ";
	}

	public static String get_WhereCondition(Map<String, String[]> map) {
		Set<String> keys = map.keySet();
		StringBuffer whereCondition = new StringBuffer();
		int count = 0;
		for (String key : keys) {
			String value = map.get(key)[0];
			System.out.println("key:" + key);
			if (value != null && value.trim().length() != 0 && !"action".equals(key) && !"whichPage".equals(key)
					&& !"keyword".equals(key) && !"changePage".equals(key)) {
				count++;
				String aCondition = get_aCondition_For_Oracle(key, value.trim());

				if (count == 1)
					whereCondition.append(" where " + aCondition);
				else
					whereCondition.append(" or " + aCondition);

				System.out.println("有送出查詢資料的欄位數count = " + count);
			}
		}

		return whereCondition.toString();
	}

}
