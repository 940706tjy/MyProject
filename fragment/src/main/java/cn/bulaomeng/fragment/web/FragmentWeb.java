package cn.bulaomeng.fragment.web;

import cn.bulaomeng.fragment.entity.Fragment;
import cn.bulaomeng.fragment.mapper.FragmentMapper;
import cn.bulaomeng.fragment.service.FragmentService;
import cn.bulaomeng.fragment.util.QrCodeUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "不老梦碎片接口")
@RequestMapping("/fragment")
public class FragmentWeb {

    @Autowired
    private FragmentService fragmentService;
    @Autowired
    private FragmentMapper fragmentMapper;

    protected static Logger logger = LoggerFactory.getLogger(FragmentWeb.class);

    /**
    * @Description:  
    * @Param: [] 
    * @return: java.util.List<cn.bulaomeng.fragment.entity.Fragment> 
    * @Author: tjy
    * @Date: 2019/5/21 
    */ 
    @PostMapping("/go")
    @ApiOperation(value = "查询全部", notes = "")
    public List<Fragment> getListAll(String name){
        logger.info("访问 go 参数:" + name);
        System.out.println(name);
        return fragmentService.getListAll();
    }
    /** 
    * @Description:  
    * @Param: [fragment] 
    * @return: java.util.List<cn.bulaomeng.fragment.entity.Fragment> 
    * @Author: tjy
    * @Date: 2019/5/21 
    */ 
    @PostMapping("/selectByPrimaryKey")
    @ApiOperation(value = "根据条件查询参数", notes = "")
    public  PageInfo<Fragment> selectByPrimaryKey(@RequestBody Fragment fragment){
        logger.info("访问 selectByPrimaryKey 参数:" + fragment);
        PageHelper.startPage(fragment.getPageNum(),fragment.getPageSize(),true);
            PageHelper.orderBy("date desc");
            PageInfo<Fragment> pageInfo=new PageInfo<>(fragmentService.selectByPrimaryKey(fragment));
        return pageInfo;
    }
    /** 
    * @Description:
    * @Param: [fragment] 
    * @return: java.lang.String 
    * @Author: tjy
    * @Date: 2019/5/21 
    */ 
    @ApiOperation(value = "增加信息",hidden = true)
    @PostMapping("/insert")
    public String insert(Fragment fragment){
        logger.info("访问 insert 参数:" + fragment);
        Integer flag=fragmentService.insert(fragment);
        if(flag==0){
            return "失败";
        }
        return "成功";
    }
    @PostMapping("/user")
    @ApiOperation(value = "数组")
    public List<Map<String,Object>> getArray(){
        logger.info("访问 user 参数:" );

        return  fragmentMapper.selectByPr();
    }
    @PostMapping("/restTemplate")
    @ApiOperation(value = "数组")
    public List<String> restTemplate(@RequestBody Fragment fragment){
        logger.info("访问 restTemplate 参数:" );

        return  fragmentService.getListByArray(fragment.getName());
    }

    @PostMapping("/email")
    @ApiOperation(value = "发送邮件")
    public String goEmail(String email){
        logger.info("访问 email 参数:" + email);
        return  fragmentService.goEmail(email);
    }


    @PostMapping("/code")
    @ApiOperation(value = "生成二维码")
    public void code(HttpServletRequest request, HttpServletResponse response){
        StringBuffer url = request.getRequestURL();
        // 域名
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append("/").toString();

        // 再加上请求链接
        String requestUrl = tempContextUrl + "/index";
        try {
            OutputStream os = response.getOutputStream();
            QrCodeUtils.encode(requestUrl, "d:/2019001.jpg", os, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
