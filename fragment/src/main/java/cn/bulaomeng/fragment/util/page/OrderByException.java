package cn.bulaomeng.fragment.util.page;

/**
 * 加解密异常
 * @author tjy
 *
 */
public class OrderByException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OrderByException() {
		super();
	}

	public OrderByException(String message) {
		super(message);
	}

	public OrderByException(Throwable cause) {
		super(cause);
	}

	public OrderByException(String message, Throwable cause) {
		super(message, cause);
	}
}
