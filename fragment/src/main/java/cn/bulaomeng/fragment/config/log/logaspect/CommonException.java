package cn.bulaomeng.fragment.config.log.logaspect;

import lombok.Data;

/**
 * Created by  on 2018/12/22.
 */
@Data
public class CommonException extends Exception {

    private Integer code;

    private String msg;

    public CommonException(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public CommonException(){
        this.code = 400;
        this.msg = "异常";
    }
}
