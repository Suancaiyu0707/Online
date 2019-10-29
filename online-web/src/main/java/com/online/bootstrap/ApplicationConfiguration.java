//package com.online.bootstrap;
//
//import com.github.kristofa.brave.Brave;
//import com.github.kristofa.brave.InheritableServerClientAndLocalSpanState;
//import com.github.kristofa.brave.Sampler;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.ImportResource;
//import zipkin.Endpoint;
//
//import java.util.Set;
//
//@Configuration
//@ImportResource({"classpath:/application-context.xml"})
//public class ApplicationConfiguration {
//
//    @Bean
//    public Brave brave() {
//        //default reporter LoggingSpanCollector
//        Brave.Builder builder = braveBuilder(Sampler.ALWAYS_SAMPLE);
//        return builder.build();
//    }
//
//    //使用springmvc拦截器形式,因为不能拦截到filter层面链路,比如shiro,所以使用filter替代
//    /*@Bean
//    public ServletHandlerInterceptor interceptor(){
//        return ServletHandlerInterceptor.create(brave());
//    }
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(interceptor());
//    }*/
//
//
////    @Bean(name = "braveFilter")
////    public HttpBraveServletFilter braveFilter(Brave brave) {
////        Set<String> paths = Sets.newHashSet("/api/**");
////        return HttpBraveServletFilter.create(brave,paths);
////    }
//
//
//    //本地服务器域名+ip
//    int ip = IPUtil.toInt(NetUtil.getLocalAddress());
//    Endpoint local = Endpoint.builder().serviceName(NetUtil.getLocalHost()).ipv4(ip).port(8080).build();
//    Brave.Builder braveBuilder(Sampler sampler) {
//        com.twitter.zipkin.gen.Endpoint localEndpoint = com.twitter.zipkin.gen.Endpoint.builder()
//                .ipv4(local.ipv4)
//                .ipv6(local.ipv6)
//                .port(local.port)
//                .serviceName(local.serviceName)
//                .build();
//        return new Brave.Builder(new InheritableServerClientAndLocalSpanState(localEndpoint))
//                .reporter(new Slf4jLogReporter("zipkin"))
//                .traceSampler(sampler);
//    }
//
//}
