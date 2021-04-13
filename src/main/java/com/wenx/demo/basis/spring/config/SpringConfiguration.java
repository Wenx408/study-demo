package com.wenx.demo.basis.spring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

/**
 * Spring配置类
 *
 * @author Wenx
 * @date 2021/4/2
 */
@Configuration
@ComponentScan("com.wenx.demo.basis.spring")
//@EnableAspectJAutoProxy
//@EnableTransactionManagement
@PropertySource("classpath:JdbcConfig.properties")
@Import(JdbcConfig.class)
public class SpringConfiguration {
}
