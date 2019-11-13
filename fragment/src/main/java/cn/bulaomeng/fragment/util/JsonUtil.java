package cn.bulaomeng.fragment.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JsonUtil {
	
	//响应体封装
	public static Map<String,Object> resultMap(Integer code, String msg, Object data){
		Map<String,Object> res = new HashMap<String,Object>();
		res.put("code",code);
        res.put("msg",msg);
        if(data!=null)
        	res.put("data",data);
		return res;
	}


    /**
     * 返回为JSONObject
     * @return
     */
    public static JSONObject resultJSONObject(Integer code, String msg, Object data) {
        JSONObject res= new JSONObject();
        res.put("comm",code);
        res.put("msg",msg);
        if(data!=null)
            res.put("data",data);
        return res;
    }

    //map转字符串
    public static String map2Json(Map<String,Object> params){
        return JSONObject.toJSONString(params);
    }

    //字符串转map
    public static Map<String,Object> json2Map(String param){
        return  JSON.parseObject(param);
    }

    //字符串转JSONObject
    public static JSONObject str2JSONObject(String param){
	    return JSONObject.parseObject(param);
    }


    /***
     * 下划线命名转为驼峰命名
     *
     * @param para
     *        下划线命名的字符串
     */

    public static String UnderlineToHump(String para){
        StringBuilder result=new StringBuilder();
        String a[]=para.split("_");
        for(String s:a){
            if(result.length()==0){
                result.append(s.toLowerCase());
            }else{
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /***
     * 驼峰命名转为下划线命名
     *
     * @param para
     *        驼峰命名的字符串
     */

    public static String HumpToUnderline(String para){
        StringBuilder sb=new StringBuilder(para);
        int temp=0;//定位
        for(int i=0;i<para.length();i++){
            if(Character.isUpperCase(para.charAt(i))){
                sb.insert(i+temp, "_");
                temp+=1;
            }
        }
        return sb.toString().toLowerCase();
    }

    /**
     * @Author: Yi
     * @param paramStr
     * @Description 路径参数转map
     * @Date: 10:36 2019/1/24
     */
    public static Map<String, String> paramToMap(String paramStr) {
        String[] params = paramStr.split("&");
        Map<String, String> resMap = new HashMap<String, String>();
        for (int i = 0; i < params.length; i++) {
            String[] param = params[i].split("=");
            if (param.length >= 2) {
                String key = param[0];
                String value = param[1];
                for (int j = 2; j < param.length; j++) {
                    value += "=" + param[j];
                }
                resMap.put(key, value);
            }else if(param.length == 1){
                resMap.put(param[0], "");
            }
        }
        return resMap;
    }

//    public static void main(String[] args) {
//        System.out.println(HumpToUnderline("userId"));
//    }

}
