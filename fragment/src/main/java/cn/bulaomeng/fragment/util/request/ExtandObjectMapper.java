package cn.bulaomeng.fragment.util.request;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * 序列化器
 * @author CaoHeYang
 * @date 20150915
 */
public class ExtandObjectMapper extends ObjectMapper{
	private static final long serialVersionUID = 4402127997078513582L;

	@SuppressWarnings("deprecation")
	public ExtandObjectMapper() {
		super();
        //设置null值不参与序列化(字段不被显示)  
        //this.setSerializationInclusion(Include.NON_NULL);
        // 禁用空对象转换json校验
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.setDateFormat(df);
        //驼峰命名法转换为小写加下划线
       this.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

}
