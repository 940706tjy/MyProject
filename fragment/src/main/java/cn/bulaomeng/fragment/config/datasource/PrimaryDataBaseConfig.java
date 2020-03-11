package cn.bulaomeng.fragment.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @Author: zeroJun
 * @Date: 2018/8/16 16:49
 * @Description: 主数据源配置类
 */
@Configuration
// 前缀为primary.datasource.druid的配置信息
@MapperScan(basePackages = PrimaryDataBaseConfig.PACKAGE, sqlSessionFactoryRef = "primarySqlSessionFactory")
public class PrimaryDataBaseConfig {

    /**
     * dao层的包路径
     */
    static final String PACKAGE = "cn.bulaomeng.fragment.mapper.master";

    /**
     * mapper文件的相对路径
     */
    private static final String MAPPER_LOCATION = "classpath:mybatis/mapper/master/*.xml";

    /**
     * mybatis配置文件路径
     */
    private static final String MYBATIS_CONFIG = "mybatis/mybatis-config.xml";


    // 主数据源使用@Primary注解进行标识
    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "primary.datasource.druid")
    public DataSource dataSource() throws SQLException {
        DruidDataSource druid = new DruidDataSource();
        return druid;
    }

    // 创建该数据源的事务管理
    @Primary
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager() throws SQLException {
        return new DataSourceTransactionManager(dataSource());
    }

    // 创建Mybatis的连接会话工厂实例
    @Primary
    @Bean(name = "primarySqlSessionFactory")
    public SqlSessionFactory primarySqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);  // 设置数据源bean
        sessionFactory.setConfigLocation(new ClassPathResource(MYBATIS_CONFIG));
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(PrimaryDataBaseConfig.MAPPER_LOCATION));  // 设置mapper文件路径

        return sessionFactory.getObject();
    }
}