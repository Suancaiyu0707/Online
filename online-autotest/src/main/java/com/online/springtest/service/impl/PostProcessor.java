package com.online.springtest.service.impl;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;

import java.beans.PropertyDescriptor;

/**
 * 如果我们想在Spring容器中完成bean实例化、配置以及其他初始化方法前后要添加一些自己逻辑处理。
 * 我们需要定义一个或多个BeanPostProcessor接口实现类，然后注册到Spring IoC容器中。
 *
 * 1：后置处理器的postProcessorBeforeInitailization方法是在bean实例化，依赖注入之后及自定义初始化方法(例如：
 * 配置文件中bean标签添加init-method属性指定Java类中初始化方法、@PostConstruct注解指定初始化方法，Java类实现InitailztingBean接口)之前调用
 * 2：后置处理器的postProcessorAfterInitailization方法是在bean实例化、依赖注入及自定义初始化方法之后调用
 *
 * 注意：
 *      1.BeanFactory和ApplicationContext两个容器对待bean的后置处理器稍微有些不同。ApplicationContext容器会自动检测Spring配置文件中那些bean所对应的Java类实现了BeanPostProcessor
 * 接口，并自动把它们注册为后置处理器。在创建bean过程中调用它们，所以部署一个后置处理器跟普通的bean没有什么太大区别。
 *       2.BeanFactory容器注册bean后置处理器时必须通过代码显示的注册，在IoC容器继承体系中的ConfigurableBeanFactory接口中定义了注册方法
 *
 *   我们可以在Spring配置文件中添加多个BeanPostProcessor(后置处理器)接口实现类，在默认情况下Spring容器会根据后置处理器的定义顺序来依次调用。
 *   在Spring机制中可以指定后置处理器调用顺序，通过让BeanPostProcessor接口实现类实现Ordered接口getOrder方法，该方法返回一整数，默认值为 0，优先级最高，值越大优先级越低
 */
public class PostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean,
                                                  String beanName) throws BeansException {
        System.out.println("后置处理器处理bean=【"+beanName+"】开始");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean,
                                                 String beanName) throws BeansException {
        System.out.println("后置处理器处理bean=【"+beanName+"】完毕!");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return bean;
    }

}
