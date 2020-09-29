package cn.bulaomeng.fragment.service;

import cn.bulaomeng.fragment.base.BaseClass;
import cn.bulaomeng.fragment.entity.Fragment;
import cn.bulaomeng.fragment.util.PageUtil;
import cn.bulaomeng.fragment.util.RedisUtil;
import cn.bulaomeng.fragment.util.request.JsonResult;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisTestService extends BaseClass {


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Autowired
    private RedisUtil redisUtil;

    public JsonResult<Integer> getKeyCount(String keyName) {
        return succMsgData(getScanKeys(keyName, -1).size());
    }

    public JsonResult<Integer> getListByName(String keyName) {
        // 先根据key名称模糊匹配出所有key
        Set<String> scanKeys = getScanKeys(keyName, -1);
        return succMsgData(batchGetList(scanKeys).size());
    }

    @Async(value = "tjyTaskExecutor")
    public JsonResult<Integer> add() {
        List<Fragment> userLs = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            Fragment obj = new Fragment();
            obj.setName("张三" + i);
            obj.setUserNo("" + i);
            userLs.add(obj);
        }
        // 总人数
        Integer count = userLs.size();
        Integer pageSize = 10000;
        Integer resultCount = count % pageSize == 0 ? (count / pageSize) : ((count / pageSize) + 1);
        // 查询key的数量 然后将key 按照1w/份的数量平均分到多个线程中

        for (int i = 1; i <= resultCount; i++) {

            List<Fragment> objects = (List<Fragment>) PageUtil.startPage(userLs, i, pageSize);
            // 多线程处理
            log.info("第" + i + "次");
            List<Map<String, Object>> ls = new ArrayList<>();
            for (int j = 0; j < objects.size(); j++) {
                Map<String, Object> map = new TreeMap<>();
                map.put("key", "DEV:SERIAL:" + objects.get(j).getUserNo());
                map.put("value", JSON.toJSONString(objects.get(j)));
                ls.add(map);
            }
            batchInsert(ls, TimeUnit.MINUTES, 60);
        }

        return null;
    }

    public void batchInsert(List<Map<String, Object>> saveList, TimeUnit unit, int timeout) {
        long beging = System.currentTimeMillis();
        /* 插入多条数据 */
        redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
                for (Map<String, Object> needSave : saveList) {
                    redisOperations.opsForList().rightPush((K) needSave.get("key"), (V) needSave.get("value"));
                    redisOperations.expire((K) needSave.get("key"), timeout, unit);
                    // redisUtil.lSet(needSave.get("key").toString(),needSave.get("value"),timeout);
                }
                log.info("结束下传");
                long end = System.currentTimeMillis();
                System.out.println(end - beging);
                return null;
            }
        });
    }

    public List<Object> batchGetList(Set<String> set) {
        long start = System.currentTimeMillis();
        List<Object> ls = redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
                for (String s : set) {
                    redisOperations.opsForList().range((K) s,0,-1);
                }
                long end = System.currentTimeMillis();
                System.out.println(end - start);
                return null;
            }
        });
        return ls;
    }

    public Set<String> getScanKeys(String pattern, int count) {

        log.info("pattern:{}, count:{}", pattern, count);
        return redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> tmpKeys = new HashSet<>();
            ScanOptions options;
            if (count <= 0) {
                options = ScanOptions.scanOptions().match(pattern).count(10000).build();
            } else {
                options = ScanOptions.scanOptions().match(pattern).count(count).build();
            }
            // 迭代一直查找，直到找到redis中所有满足条件的key为止(cursor变为0为止)
            Cursor<byte[]> cursor = connection.scan(options);
            while (cursor.hasNext()) {
                tmpKeys.add(new String(cursor.next()));
            }
            return tmpKeys;
        });
    }

    public Set<String> getKeys(String pattern, int count) {
        return redisTemplate.keys(pattern);
    }
}
