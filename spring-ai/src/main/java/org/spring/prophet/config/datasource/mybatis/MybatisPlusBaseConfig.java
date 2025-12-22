package org.spring.prophet.config.datasource.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Deacription 数据配置
 * @Version 1.0
 **/
@Slf4j
@Configuration
public class MybatisPlusBaseConfig {

    /**
     * @description: 分页
     */
    public MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

    @Bean
    public MybatisPlusInterceptor mybatisPlusBaseInterceptor() {
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 乐观锁开启
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlInjectorPlus sqlInjectorPlus() {
        return new SqlInjectorPlus();
    }
}