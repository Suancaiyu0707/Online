package com.online.config;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.context.annotation.Configuration;

//这个是稍微复杂一些的配置形式，指示Apollo注入application-dubbo.yml和application.yml namespace的配置到Spring环境中
//可以通过order指定顺序，默认是1
@Configuration
@EnableApolloConfig(value = {"application-dubbo.yml", "application.yml"},order = 1)
public class AnotherAppConfig {}