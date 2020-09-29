package cn.bulaomeng.fragment.web;

import cn.bulaomeng.fragment.service.RedisTestService;
import cn.bulaomeng.fragment.util.request.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
@Api(description = "redis操作测试")
public class RedisTestController {

    @Autowired
    private RedisTestService redisTestService;


    @ApiOperation("添加")
    @GetMapping("add")
    public JsonResult<Integer> add(){
        return redisTestService.add();
    }


    @ApiOperation("获取key的数量")
    @GetMapping("getKeyCount")
    public JsonResult<Integer> getKeyCount(@RequestParam("keyName")String keyName){
        return redisTestService.getKeyCount(keyName);
    }


    @ApiOperation("获取key的数量")
    @GetMapping("getKeyCount1")
    public JsonResult<Integer> getKeyCount1(@RequestParam("keyName")String keyName){
        return redisTestService.getListByName(keyName);
    }
}
