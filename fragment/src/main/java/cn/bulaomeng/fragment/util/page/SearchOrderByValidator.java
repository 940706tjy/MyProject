package cn.bulaomeng.fragment.util.page;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 排序字段验证
 *
 * @author tjy
 */
public class SearchOrderByValidator implements
		ConstraintValidator<SearchOrderByValid, PageSearch<? extends OrderBy>> {
	@Override
	public void initialize(SearchOrderByValid constraintAnnotation) {

	}

	@Override
	public boolean isValid(PageSearch<? extends OrderBy> value,
			ConstraintValidatorContext context) {
		// 为空
		if (value.getSearch() == null) {
			return true;
		}
		// 可排序字段
		if (value.getOrderBy() == null || value.getOrderBy().trim().isEmpty()) {
			return true;
		}
		if (value.getSearch().getOrderByFieldMap() == null) {
			throw new OrderByException("order排序字段与数据库字段对应map未初始化");
		}
		String orderByStr = "";
		for (String temp : value.getOrderBy().split(",")) {
			String[] column = temp.split(" ");
			if (column.length != 2) {
				return false;
			}
			if (column[0] == null
					|| column[0].trim().isEmpty()
					|| !value.getSearch().getOrderByFieldMap()
							.containsKey(column[0].toLowerCase())
					|| column[1] == null || column[1].trim().isEmpty()
					|| "asc,desc".indexOf(column[1]) < 0) {
				return false;
			} else {
				orderByStr += value.getSearch().getOrderByFieldMap()
						.get(column[0].toLowerCase())
						+ " " + column[1] + ",";
			}
		}
		value.setOrderBy(orderByStr.substring(0, orderByStr.length() - 1));
		return true;
	}
}