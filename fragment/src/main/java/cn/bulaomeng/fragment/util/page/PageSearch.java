package cn.bulaomeng.fragment.util.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * @author tjy
 */
@ApiModel
@SearchOrderByValid
public  class PageSearch<T extends OrderBy> extends RequestBase {
	private static final long serialVersionUID = -2816565786374080978L;
	
	@ApiModelProperty(required = false, value = "页码,默认1", example = "1", dataType = "int")
	@JsonProperty("page_index")
	private Integer pageIndex;
	
	@ApiModelProperty(required = false, value = "每页条数,默认20", example = "20", dataType = "int")
	@JsonProperty("page_size")
	private Integer pageSize;
	
	@ApiModelProperty(required = false, value = "是否查询数据总条数,默认false", example = "true", dataType = "bool")
	private boolean count = true;
	
	@ApiModelProperty(required = false, value = "排序字段  ", example = "XXX desc,XXX asc", dataType = "string")
	@JsonProperty("order_by")
	private String orderBy;
	
	@ApiModelProperty(required = false, value = "查询条件主体")
	@NotNull
	private T search;
	
	/**
	 * 页码 从1开始
	 * 
	 * @return
	 */
	public Integer getPageIndex() {
		if (pageIndex == null || pageIndex <= 0) {
			return 1;
		}
		return pageIndex;
	}

	/**
	 * 页码 从1开始
	 *
	 */
	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	/**
	 * 每页条数
	 * 
	 * @return
	 */
	public Integer getPageSize() {
		if (pageSize == null || pageSize <= 0) {
			return 20;
		}
		return pageSize;
	}

	/**
	 * 每页条数
	 * 
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 是否查询数据总条数,默认false
	 * 
	 * @return
	 */
	public Boolean getCount() {
		return this.count;
	}

	/**
	 * 是否查询数据总条数,默认false
	 * 
	 * @param count
	 */
	public void setCount(boolean count) {
		this.count = count;
	}

	/**
	 * 排序字段
	 * 
	 * @return
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * 排序字段
	 * 
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public T getSearch() {
		return search;
	}

	public void setSearch(T search) {
		this.search = search;
	}
}
