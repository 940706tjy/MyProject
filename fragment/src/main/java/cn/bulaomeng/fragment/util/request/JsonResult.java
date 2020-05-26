package cn.bulaomeng.fragment.util.request;

import cn.bulaomeng.fragment.common.HttpReturnRnums;
import cn.bulaomeng.fragment.common.IEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 默认code为成功
 * 
 * @author caoheyang
 *
 */
@ApiModel
public class JsonResult<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "状态码", notes = "0是成功,其他都是失败")
	private int code = HttpReturnRnums.Success.value();

	@ApiModelProperty(value = "状态码描述信息", notes = "例如失败，参数错误")
	private String msg = HttpReturnRnums.Success.desc();

	@ApiModelProperty(value = "具体的业务数据", notes = "")
	private T data;

	public JsonResult(int code, String msg, T data) {
		setCode(code);
		setMsg(msg);
		setData(data);
	}

	public JsonResult(IEnum e, T data) {
		setCode(e.value());
		setMsg(e.desc());
		setData(data);
	}

	public JsonResult(IEnum data) {
		setCode(data.value());
		setMsg(data.desc());
	}

	public JsonResult(int code, String msg) {
		setCode(code);
		setMsg(msg);
	}

	public JsonResult(T data) {
		this.data = data;
	}

	public JsonResult() {
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return this.msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public static <T> JsonResult<T> get(int code, String msg, T data) {
		return new JsonResult<T>(code, msg, data);

	}

	public static <T> JsonResult<T> get(int code, String msg) {
		return JsonResult.get(code, msg, null);
	}
	
	public static <T> JsonResult<T> ok() {
		return JsonResult.get(0, "ok", null);
	}
	
	public static <T> JsonResult<T> get(T data) {
		return JsonResult.get(0, "ok", data);
	}

	public static <T> JsonResult<T> get(IEnum e) {
		return JsonResult.get(e.value(), e.desc(), null);
	}

	public static <T> JsonResult<T> get(IEnum e, T data) {
		return JsonResult.get(e.value(), e.desc(), data);
	}
}
