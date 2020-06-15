package cn.bulaomeng.fragment.util.page;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 请求父类
 * 
 * @author tjy
 *
 */
public class RequestBase implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(hidden = true)
	private Long requestTime;
	/**
	 * 请求时间
	 * @return
	 */
	public Long getRequestTime() {
		return requestTime==null? (new Date()).getTime():requestTime;
	}
	/**
	 *  请求时间
	 * @param requestTime
	 */
	public void setRequestTime(Long requestTime) {
		this.requestTime = requestTime;
	}
}
