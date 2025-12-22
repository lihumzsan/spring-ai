package org.spring.prophet.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

//@Configuration
//@MapperScan(basePackages = "com.feeyo.schedule.backend.mapper.source", sqlSessionFactoryRef = "sourceSqlSessionFactory")
public class DbSourceConfig {

    /**
     * 创建 source 数据源的属性配置
     * 这将绑定 spring.datasource.source 下的基本属性（url, username, password等）
     */
    @Bean(name = "sourceDataSourceProperties")
    @ConfigurationProperties("spring.datasource.source")
    public DataSourceProperties sourceDataProperties() {
        return new DataSourceProperties();
    }

    /**
     * 创建 source 数据源
     * 使用上面的属性配置初始化数据源，并绑定 hikari 特定属性
     */
    @Bean(name = "sourceDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.source.hikari")
    public HikariDataSource sourceDataSource(@Qualifier("sourceDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    /**
     * 创建 source 数据源的 SqlSessionFactory
     */
    @Bean(name = "sourceSqlSessionFactory")
    public SqlSessionFactory sourceSqlSessionFactory(@Qualifier("sourceDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:mapper/source/*.xml"));
        return bean.getObject();
    }

    /**
     * 创建 soource 数据源的 SqlSessionTemplate
     */
    @Bean(name = "sourceSqlSessionTemplate")
    public SqlSessionTemplate sourceSqlSessionTemplate(@Qualifier("sourceSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}