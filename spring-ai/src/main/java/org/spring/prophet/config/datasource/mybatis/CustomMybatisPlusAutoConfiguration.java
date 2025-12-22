package org.spring.prophet.config.datasource.mybatis;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * mybatis plus自动配置
 */
@ConditionalOnClass(MybatisPlusAutoConfiguration.class)
@Import({MybatisPlusBaseConfig.class})
@Configuration(proxyBeanMethods = false)
public class CustomMybatisPlusAutoConfiguration {

}
