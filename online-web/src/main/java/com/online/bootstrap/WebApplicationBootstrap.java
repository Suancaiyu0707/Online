package com.online.bootstrap;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ImportResource("classpath:/spring-servlet.xml")
public class WebApplicationBootstrap extends WebMvcConfigurerAdapter {


}
