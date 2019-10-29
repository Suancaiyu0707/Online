package com.online.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 两个方法均在bean实例化期间已经完成
 */
public class LogBeanPostProcessor  implements BeanPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(LogBeanPostProcessor.class);

    /***
     * 在bean初始化之前执行
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        logger.info("正在处理："+beanName);
        return bean;
    }

    /**
     * 在bean初始化之后执行
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        logger.info("已经处理完成："+beanName);
        return bean;
    }
}
