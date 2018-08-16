package com.tools;
import java.util.*;

public class jdbcUtil_CompositeQuery_Grp {


	public static String get_aCondition_For_Oracle(String columnName, String value) {

		String aCondition = null;
		if ("GRP_ID".equals(columnName))
			aCondition = columnName + "=" + value;
		else if ("TRIP_LOCALE".equals(columnName) || "TRIP_DETAILS".equals(columnName) || "GRP_PRICE".equals(columnName)) // 用於varchar
			aCondition = columnName + " like '%" + value + "%'";
		else if ("GRP_START".equals(columnName) || "GRP_END".equals(columnName))      // 用於Oracle的date
			aCondition = "to_char(" + columnName + ",'yyyy-mm-dd')='" + value + "'";

		return aCondition + " ";
	}

	public static String get_WhereCondition(Map<String, String[]> map) {
		Set<String> keys = map.keySet();
		StringBuffer whereCondition = new StringBuffer();
		int count = 0;
		for (String key : keys) {
			String value = map.get(key)[0];
			if (value != null && value.trim().length() != 0	&& !"action".equals(key)) {
				count++;
				String aCondition = get_aCondition_For_Oracle(key, value.trim());

				if (count == 1)
					whereCondition.append(" where " + aCondition);
				else
					whereCondition.append(" and " + aCondition);

				System.out.println("有送出查詢資料的欄位數count = " + count);
			}
		}
		
		return whereCondition.toString();
	}

	public static void main(String argv[]) {

		// 配合 req.getParameterMap()方法 回傳 java.util.Map<java.lang.String,java.lang.String[]> 之測試
		Map<String, String[]> map = new TreeMap<String, String[]>();
		map.put("GRP_ID", new String[] { "GRP000001" });
		map.put("TRIP_LOCALE", new String[] { "台灣" });
		map.put("TRIP_DETAILS", new String[] { "台灣" });
		map.put("GRP_PRICE", new String[] { "19999" });
		map.put("GRP_START", new String[] { "2018-07-17" });
		map.put("GRP_END", new String[] { "2018-07-20" });
//		map.put("action", new String[] { "getXXX" }); // 注意Map裡面會含有action的key

		String finalSQL = "SELECT * FROM GRP"
				          + jdbcUtil_CompositeQuery_Grp.get_WhereCondition(map)
				          + "order by GRP_ID";
		System.out.println("●●finalSQL = " + finalSQL);

	}

}
