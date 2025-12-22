package org.spring.prophet.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "org.spring.prophet.mapper.target", sqlSessionFactoryRef = "targetSqlSessionFactory")
public class DbTargetConfig {

    /**
     * 创建 target 数据源的属性配置
     * 这将绑定 spring.datasource.target 下的基本属性（url, username, password等）
     */
    @Bean(name = "targetDataSourceProperties")
    @ConfigurationProperties("spring.datasource.target")
    public DataSourceProperties targetDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * 创建 target 数据源
     * 使用上面的属性配置初始化数据源，并绑定 hikari 特定属性
     */
    @Bean(name = "targetDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.target.hikari")
    public HikariDataSource targetDataSource(
            @Qualifier("targetDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    /**
     * 创建 target 数据源的 SqlSessionFactory
     */
    @Bean(name = "targetSqlSessionFactory")
    public SqlSessionFactory targetSqlSessionFactory(
            @Qualifier("targetDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:mapper/target/*.xml"));
        return bean.getObject();
    }

    /**
     * 创建 target 数据源的 SqlSessionTemplate
     */
    @Bean(name = "targetSqlSessionTemplate")
    public SqlSessionTemplate targetSqlSessionTemplate(
            @Qualifier("targetSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}