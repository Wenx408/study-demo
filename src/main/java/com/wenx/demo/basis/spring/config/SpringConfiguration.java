package com.wenx.demo.basis.spring.config;

import org.springframework.context.annotation.*;

/**
 * Spring配置类
 *
 * @author Wenx
 * @date 2021/4/2
 */
@Configuration
@ComponentScan("com.wenx.demo.basis.spring")
//@EnableAspectJAutoProxy
@PropertySource("classpath:SpringConfig.properties")
@Import(JdbcConfig.class)
public class SpringConfiguration {
}
