package jdbc.util.CompositeQuery;

import java.util.*;

public class jdbcUtil_CompositeQuery_Product {

	public static String get_aCondition_For_Oracle(String columnName, String value) {

		String aCondition = null;

		if ("PRODUCT_ID".equals(columnName) || "PRODUCT_CATEGORY_ID".equals(columnName) || "PRODUCT_STOCK".equals(columnName)|| "PRODUCT_STATUS".equals(columnName)) // 用於其他
			aCondition = columnName + "=" + value;
		else if ( "minPrice".equals(columnName))
			aCondition = "PRODUCT_PRICE" + ">" + value;
		else if ( "maxPrice".equals(columnName))
		aCondition = "PRODUCT_PRICE" + "<=" + value;
		else if ("PRODUCT_MEM_ID".equals(columnName) || "PRODUCT_NAME".equals(columnName)|| "PRODUCT_DESCR".equals(columnName)) // 用於varchar
			aCondition = "upper("+columnName +")"+ " like upper('%" + value + "%')";
		else if ("PRODUCT_DATE".equals(columnName))                          // 用於Oracle的date
			aCondition = "to_char(" + columnName + ",'yyyy-mm-dd')='" + value + "'";
		else if ( "orderby".equals(columnName))
			aCondition = "order by " + value;
		return aCondition + " ";
	}

	public static String get_WhereCondition(Map<String, String[]> map) {
		Set<String> keys = map.keySet();
		StringBuffer whereCondition = new StringBuffer();
		int count = 0;
		int i =0;
		for (String key : keys) {
			String value = map.get(key)[0];
		
			if (value != null && value.trim().length() != 0	&& !"action".equals(key)) {
				count++;
				String aCondition = get_aCondition_For_Oracle(key, value.trim());
				System.out.println(key);
				System.out.println(value.trim());
				int lastIndex = whereCondition.lastIndexOf("order");
					if(key.equals("orderby")) {
						whereCondition.append(aCondition);
					}else if(!key.equals("orderby")){
						if (i == 0) {
							if(lastIndex != -1) {
								whereCondition.insert(lastIndex," where " + aCondition);
							}else if(lastIndex == -1) {
								whereCondition.append(" where " + aCondition);
							}
							i++;
						}else {
							if(lastIndex != -1) {
								whereCondition.insert(lastIndex," and " + aCondition);
							}else if(lastIndex == -1) {
								whereCondition.append(" and " + aCondition);
							}
							
						}
					}

				System.out.println("有送出查詢資料的欄位數count =  " + count);
			}
		}
		
		return whereCondition.toString();
	}

}
