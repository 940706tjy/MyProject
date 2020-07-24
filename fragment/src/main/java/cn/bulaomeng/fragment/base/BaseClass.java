package cn.bulaomeng.fragment.base;

import cn.bulaomeng.fragment.util.page.PageSearch;
import cn.bulaomeng.fragment.util.request.JsonResult;
import com.github.pagehelper.PageHelper;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import static cn.bulaomeng.fragment.constant.CommConstant.*;

public class BaseClass {

    public BaseClass() {
    }

    protected void setPageHelper(PageSearch<?> pageSearch) {
        PageHelper.startPage(pageSearch.getPageIndex(), pageSearch.getPageSize(), pageSearch.getCount());
        PageHelper.orderBy(pageSearch.getOrderBy() != null ? pageSearch.getOrderBy().trim() : pageSearch.getOrderBy());
    }

    protected <T> JsonResult<T> message(Integer code, String msg, T data) {
        JsonResult<T> jsonResult = new JsonResult();
        jsonResult.setCode(code);
        jsonResult.setMsg(msg);
        jsonResult.setData(data);
        return jsonResult;
    }

    protected <T> JsonResult<T> succMsg(String msg, T data) {
        return this.message(SUCCESSED_FLAG, msg, data);
    }

    protected <T> JsonResult<T> succMsg(String msg) {
        return this.succMsg(msg, null);
    }

    protected <T> JsonResult<T> succMsgData(T data) {
        return this.succMsg(SUCCESSED_MSG, data);
    }

    protected <T> JsonResult<T> succMsg() {
        return this.succMsg(SUCCESSED_MSG);
    }

    protected <T> JsonResult<T> failMsg(Integer code, String message, T data) {
        return this.message(code, message, data);
    }

    protected <T> JsonResult<T> failMsg(String message) {
        return this.failMsg(FAILED_FLAG,message, null);
    }
    protected <T> JsonResult<T> failMsg(String message, T data) {
        return this.failMsg(FAILED_FLAG, message, data);
    }

    protected <T> JsonResult<T> failMsg() {
        return this.failMsg(FAILED_MSG);
    }

    protected <T> JsonResult<T> failMsgData(T data) {
        return this.message(FAILED_FLAG, FAILED_MSG, data);
    }

    protected <T> JsonResult<T> failParamMsg() {
        return this.failMsg("参数错误");
    }

    protected String objToStr(Object res) {
        return res.toString();
    }


    /**
     * 使用Validator + BindResult进行校验
     *
     * @author tjy
     * @date 2020/7/24
     **/
    protected <T> JsonResult<T> checkResult(BindingResult bindingResult, T data) {

        for (ObjectError error : bindingResult.getAllErrors()) {
            return failMsg(error.getDefaultMessage(), data);
        }
        return succMsgData(data);
    }

    protected <T> JsonResult<T> checkResult(BindingResult bindingResult) {

        for (ObjectError error : bindingResult.getAllErrors()) {
            return failMsg(error.getDefaultMessage());
        }
        return succMsg();
    }

}
