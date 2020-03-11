package cn.bulaomeng.fragment.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @Author: zeroJun
 * @Date: 2018/8/16 16:49
 * @Description: 后台数据源配置类
 */

@Configuration
@MapperScan(basePackages = BackDataBaseConfig.PACKAGE, sqlSessionFactoryRef = "backSqlSessionFactory")
public class BackDataBaseConfig {

    /**
     * dao层的包路径
     */
    static final String PACKAGE = "cn.bulaomeng.fragment.mapper.slave";

    /**
     * mapper文件的相对路径
     */
    private static final String MAPPER_LOCATION = "classpath:mybatis/mapper/slave/*.xml";

    /**
     * mybatis配置文件路径
     */
    private static final String MYBATIS_CONFIG = "mybatis/mybatis-config.xml";


    @Bean(name = "backDataSource")
    @ConfigurationProperties(prefix = "second.datasource.druid")
    public DataSource backDataSource() throws SQLException {
        DruidDataSource druid = new DruidDataSource();

        return druid;
    }

    @Bean(name = "backTransactionManager")
    public DataSourceTransactionManager backTransactionManager() throws SQLException {
        return new DataSourceTransactionManager(backDataSource());
    }

    @Bean(name = "backSqlSessionFactory")
    public SqlSessionFactory backSqlSessionFactory(@Qualifier("backDataSource") DataSource backDataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(backDataSource);
        sessionFactory.setConfigLocation(new ClassPathResource(MYBATIS_CONFIG));
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(BackDataBaseConfig.MAPPER_LOCATION));

        return sessionFactory.getObject();
    }
}