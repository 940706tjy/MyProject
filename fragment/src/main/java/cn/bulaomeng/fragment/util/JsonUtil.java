package cn.bulaomeng.fragment.util;

import cn.bulaomeng.fragment.util.request.ExtandObjectMapper;
import com.fasterxml.jackson.databind.JavaType;

import java.io.StringWriter;
import java.util.List;

public class JsonUtil {
	private final static ExtandObjectMapper mapper = new ExtandObjectMapper();

	/**
	 * 将对象转换为json字符串
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static String obj2string(Object obj) {
		if (obj == null) {
			return "";
		}
		StringWriter sw = new StringWriter();
		try {
			mapper.writeValue(sw, obj);
		} catch (Exception e) {
			throw new RuntimeException("序列化时出错:", e);
		}
		return sw.toString();
	}

	/**
	 * 将字符串转list对象
	 * 
	 * @param <T>
	 * @param jsonStr
	 * @param cls
	 * @return
	 */
	public static <T> List<T> str2list(String jsonStr, Class<T> cls) {
		List<T> objList = null;
		try {
			JavaType t = mapper.getTypeFactory().constructParametricType(
					List.class, cls);
			objList = mapper.readValue(jsonStr, t);
		} catch (Exception e) {
			throw new RuntimeException("反序列化为List对象时出错:", e);
		}
		return objList;
	}

	/**
	 * 字符串转泛型
	 * 
	 * @param jsonStr
	 *            json字符串
	 * @param genTyClass
	 *            泛型类 比如 List
	 * @param cls
	 *            泛的具体类型 比如 User.class
	 * @return
	 */
	public static <T> T str2Obj(String jsonStr, Class<?> genTyClass,
			Class<?> cls) {

		return str2obj(jsonStr,genTyClass,cls);
	}
	public static <T> T str2obj(String jsonStr, Class<?> genTyClass,
			Class<?> cls) {
		T objList = null;
		try {
			JavaType t = mapper.getTypeFactory().constructParametricType(
					genTyClass, cls);
			objList = mapper.readValue(jsonStr, t);
		} catch (Exception e) {
			throw new RuntimeException("反序列化为List对象时出错:", e);
		}
		return objList;
	}

	/**
	 * 将字符串转为对象
	 * 
	 * @param <T>
	 * @param jsonStr
	 * @param cls
	 * @return
	 */
	public static <T> T str2obj(String jsonStr, Class<T> cls) {
		T obj = null;
		if (jsonStr != null && jsonStr.length() > 1) {
			try {
				obj = mapper.readValue(jsonStr, cls);
			} catch (Exception e) {
				throw new RuntimeException("反序列化时出错:", e);
			}
		}
		return obj;
	}
	
	/**
	 * 将字符串转为对象(safe)
	 * 
	 * @param <T>
	 * @param jsonStr
	 * @param cls
	 * @return
	 */
	public static <T> T str2obj_safe(String jsonStr, Class<T> cls) {
		T obj = null;
		if (jsonStr != null && jsonStr.length() > 1) {
			try {
				obj = mapper.readValue(jsonStr, cls);
			} catch (Exception e) {
				//throw new RuntimeException("反序列化时出错:", e);
			}
		}
		if(obj==null){
			try {
				obj = cls.newInstance();
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
		}
		return obj;
	}

}
