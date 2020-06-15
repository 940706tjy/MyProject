package cn.bulaomeng.fragment.util.page;

import java.util.Map;

/**
 * 分页排序抽象类
 * 
 * @author tjy
 *
 */
public abstract class OrderBy {
	/**
	 * orderby map
	 * <p>
	 * key：调用方orderby字符串包含的字段
	 * </p>
	 * 
	 * <p>
	 * value：数据库中的字段名，如a.id,b.insert_time
	 * </p>
	 * 
	 * @return
	 */
	public abstract Map<String, String> getOrderByFieldMap();
}
