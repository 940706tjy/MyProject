package cn.bulaomeng.fragment.common;
/**
 * api-http 返回值公用 enum
 * @author CaoHeYang
 * @date 2015-0909
 */
public enum HttpReturnRnums implements IEnum{
	/**
	 * 成功
	 */
	Success(0,"成功"),
	/**
	 * 系统错误
	 */
	SystemError(-1,"系统错误"),
	/**
	 * 参数错误
	 */
	ParaError(-2,"参数错误"),
	/**
	 * 参数加解密异常
	 */
	EncodeError(-3,"参数加解密异常"),
	/**
	 *签名认证失败
	 */
	SignError(-4,"签名认证失败"),
	/**
	 *排序字段不合法
	 */
	OrderbyError(-5,"排序字段不合法");
	
	private int value = 0;
	private String desc;
	private HttpReturnRnums(int value, String desc) { // 必须是private的，否则编译错误
		this.value = value;
		this.desc = desc;
	}

	@Override
	public int value() {
		return this.value;
	}
	@Override

	public String desc() {
		return this.desc;
	}

	public static HttpReturnRnums getEnum(int index) {
		for (HttpReturnRnums c : HttpReturnRnums.values()) {
			if (c.value() == index) {
				return c;
			}
		}
		return null;
	}
}
