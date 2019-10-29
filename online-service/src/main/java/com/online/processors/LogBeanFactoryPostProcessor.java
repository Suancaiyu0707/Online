package com.online.processors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/***
 *  允许我们在工厂里所有的bean被加载进来后但是还没初始化前，对所有bean的属性进行修改也可以add属性值。
 *
 *      （1）BeanFactoryPostProcessor允许使用者修改容器中的bean definitions,
 *      （2）BeanFactoryPostProcessor可以与bean definitions打交道，但是千万不要进行bean实例化
 *      （感觉这里应该说的是不要在BeanFactoryPostProcessor进行可能触发bean实例化的操作）。
 *          这么做可能会导致bean被提前实例化，会破坏容器造成预估不到的副作用。如果你需要hack到bean实例化过程，请考虑使用BeanPostProcessor。
 *
 */
public class LogBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("******调用BeanFactoryPostProcessor开始");
        //获取到Spring中所有的beanName
        String[] beanStr = beanFactory.getBeanDefinitionNames();
        //循环bean做出自定义的操作
        for (String beanName : beanStr) {
            System.out.println("bean name:"+beanName);
            if ("user".equals(beanName)) {
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
                System.out.println("修改user的age值");
                beanDefinition.getPropertyValues().add("age", "20");
            }
        }
        System.out.println("******调用BeanFactoryPostProcessor结束");
    }
}
