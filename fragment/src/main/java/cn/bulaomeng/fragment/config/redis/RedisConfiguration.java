/*
package cn.bulaomeng.fragment.config.redis;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

*/
/**
 * 项目启动，redis根据application.properties配置自动启动对应模式
 *//*

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
@ComponentScan("cn.bulaomeng.fragment.config")
public class RedisConfiguration {

    //获取redis配置信息
    @Resource
    private cn.bulaomeng.fragment.config.redis.RedisProperties redisProperties;


	@Bean
    JedisConnectionFactory jedisConnectionFactory() {

	    // 获取启动模式
        int mode = redisProperties.getMode();
        */
/**
		 *	根据不通模式，初始化不通连接工厂
		 *	1-单例模式。2-sentinel模式。3-cluster模式
		 *//*

		switch (mode) {
			case 1:
				return singletonInit();
			case 2:
				return sentinelInit();
			case 3:
				return clusterInit();
			default:
			    // 根据配置文件判断启动模式
				return commonInit();
		}

	}

	*/
/**
	 * RedisTemplate配置
	 * 
	 * @param factory
	 * @return
	 *//*

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(factory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(new StringRedisSerializer());
		template.afterPropertiesSet();
		return template;
	}

	*/
/**
	 * 初始化单例模式连接
	 * @return
	 *//*

	private JedisConnectionFactory singletonInit() {
        // 连接池设置
        JedisPoolConfig poolConfig = poolInit();
        // 初始化连接
        JedisConnectionFactory factory = new JedisConnectionFactory(poolConfig);
        // 设置连接属性
		factory.setDatabase(redisProperties.getDatabase());
		factory.setHostName(redisProperties.getHost());
		factory.setPort(redisProperties.getPort());
		factory.setPassword(redisProperties.getPassword());
		factory.setTimeout(redisProperties.getTimeout());
		return factory;
	}

	*/
/**
	 * 初始化sentinel模式连接
	 * @return
	 *//*

	private JedisConnectionFactory sentinelInit() {
        // sentinel连接配置
        RedisSentinelConfiguration sentinelConfiguration = new RedisSentinelConfiguration();
        sentinelConfiguration.setMaster(redisProperties.getSentinel().getMaster());
        String[] arrayHosts = redisProperties.getSentinel().getNodes().split(",");
        // 把配置文件中的String类型的sentinel节点转为RedisNode集合
        List<RedisNode> nodesList = getRedisNodes(arrayHosts);
        // 设置sentinel
        sentinelConfiguration.setSentinels(nodesList);

        // 连接池设置
        JedisPoolConfig poolConfig = poolInit();

        return new JedisConnectionFactory(sentinelConfiguration, poolConfig);
	}

	*/
/**
	 * 初始化集群模式连接
	 * @return
	 *//*

	private JedisConnectionFactory clusterInit() {
	    // cluster配置
        RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration();
        // 把application.properties中的cluster的配置节点写入list中
        // 把属性根据逗号分割，获取ip+port
		String[] arrayHosts = redisProperties.getCluster().getNodes().split(",");
		List<RedisNode> nodesList = getRedisNodes(arrayHosts);
        clusterConfiguration.setClusterNodes(nodesList);
        // 连接池设置
        JedisPoolConfig poolConfig = poolInit();

		return new JedisConnectionFactory(clusterConfiguration, poolConfig);
	}

    */
/**
     * 根据配置文件决定启动方式
     * 如果有host的话启动单例
     * 如果有sentinel的话启动sentinel
     * 如果有cluster的话，启动cluster
     * 根据顺序判断
     * @return
     *//*

    private JedisConnectionFactory commonInit() {
        if (null != redisProperties.getHost()) {
            return singletonInit();
        }
        if (null != redisProperties.getSentinel()) {
            return sentinelInit();
        }
        if (null != redisProperties.getCluster()) {
            return clusterInit();
        }
        // 返回默认本地的配置
        return new JedisConnectionFactory();
    }


    */
/**
     * 通用连接池设置方法
     * @return
     *//*

	private JedisPoolConfig poolInit() {
        // 连接池设置
        JedisPoolConfig poolConfig = new JedisPoolConfig();
		if (null != redisProperties.getPool() && 0 != redisProperties.getPool().getMaxIdle()) {
			poolConfig.setMaxIdle(redisProperties.getPool().getMaxIdle());
		}
		if (null != redisProperties.getPool() && 0 != redisProperties.getPool().getMinIdle()) {
			poolConfig.setMinIdle(redisProperties.getPool().getMinIdle());
		}
		if (null != redisProperties.getPool() && 0 != redisProperties.getPool().getMaxWait()) {
			poolConfig.setMaxWaitMillis(redisProperties.getPool().getMaxWait());
		}
		return poolConfig;
    }

    */
/**
     * 把数组转为redis node节点
     * @param arrayHosts
     * @return
     *//*

	private List<RedisNode> getRedisNodes(String[] arrayHosts) {
		List<RedisNode> nodesList = new ArrayList<RedisNode>();
		for (int i = 0; i < arrayHosts.length; i++) {
			String ip = arrayHosts[i];
			String host = ip.split(":")[0];
			int port = Integer.valueOf(ip.split(":")[1]);
			RedisNode redisNode = new RedisNode(host,port);
			nodesList.add(redisNode);
		}
		return nodesList;

	}

}*/
