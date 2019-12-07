package cn.bulaomeng.fragment.web;


import cn.bulaomeng.fragment.service.FragmentService;
import cn.bulaomeng.fragment.util.PdfUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能：pdf预览、下载
 *
 * @author qust
 * @version 1.0 2018/2/23 9:35
 */
@Controller
@RequestMapping(value = "/pdf")
@Api(description = "不老梦碎片内容打印")
public class PdfController {

    @Autowired
    private FreeMarkerConfigurer configurer;
    @Autowired
    private FragmentService fragmentService;

    protected static Logger logger = LoggerFactory.getLogger(PdfController.class);

    /**
     * pdf预览
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/preview", method = RequestMethod.GET)
    @ApiOperation(value = "浏览PDF", notes = "")
    public void preview(HttpServletRequest request, HttpServletResponse response) {
        logger.info("访问 preview");
        // 构造freemarker模板引擎参数,listVars.size()个数对应pdf页数
        List<Map<String,Object>> listVars = new ArrayList<>();
        Map<String,Object> variables = new HashMap<>();
        variables.put("title","测试预览ASGX!");
        variables.put("data","测试预览ASGX!");
        listVars.add(variables);

        PdfUtils.preview(configurer,"pdfPage.ftl",listVars,response);
    }

    /**
     * pdf下载
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    @ApiOperation(value = "打印PDF", notes = "")
    public void download(HttpServletRequest request, HttpServletResponse response) {
        logger.info("访问 download");
        List<Map<String,Object>> listVars = new ArrayList<>();
        Map<String,Object> variables = new HashMap<>();
        variables.put("title","碎片列表");
        variables.put("listAll",fragmentService.getListAll());
        listVars.add(variables);
        PdfUtils.download(configurer,"pdfPage.ftl",listVars,response,"不老梦碎片记录.pdf");
    }
}
