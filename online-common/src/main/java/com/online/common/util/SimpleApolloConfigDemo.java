package com.online.common.util;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/***
 * java获取apollo配置
 */
public class SimpleApolloConfigDemo {
    private static final Logger logger = LoggerFactory.getLogger(SimpleApolloConfigDemo.class);
    private String DEFAULT_VALUE = "undefined";
    private Config config;

    public SimpleApolloConfigDemo() {
        //服务的地址，该地址为apollo服务启动的地址
        System.setProperty("apollo.meta", "http://localhost:8080");
        //配置文件对应集群，非必须
        //System.setProperty("apollo.cluster", "dev-cluster");
        //配置文件的生效环境
        System.setProperty("env", "DEV");
        //配置文件所属的应用
        System.setProperty("app.id", "apollo-dubbo-demo");
        //创建一个监听器
        ConfigChangeListener changeListener = new ConfigChangeListener() {
            @Override
            public void onChange(ConfigChangeEvent changeEvent) {
                logger.info("Changes for namespace {}", changeEvent.getNamespace());
                for (String key : changeEvent.changedKeys()) {
                    ConfigChange change = changeEvent.getChange(key);
                    logger.info("Change - key: {}, oldValue: {}, newValue: {}, changeType: {}",
                            change.getPropertyName(), change.getOldValue(), change.getNewValue(),
                            change.getChangeType());
                }
            }
        };
        //根据ConfigService来创建一个config，并注册一个监听器，用来监听服务端配置的变化
        //配置文件名称
        config = ConfigService.getConfig("online-dubbo");
        //获得指定的公共命名空间的方法
//        String somePublicNamespace = "CAT";
//        Config config2 = ConfigService.getConfig(somePublicNamespace); //config instance is singleton for each namespace and is never null
//        String someKey = "someKeyFromPublicNamespace";
//        String someDefaultValue = "someDefaultValueForTheKey";
//        String value = config2.getProperty(someKey, someDefaultValue);
        config.addChangeListener(changeListener);
    }

    private String getConfig(String key) {
        String result = config.getProperty(key, DEFAULT_VALUE);
        logger.info(String.format("Loading key : %s with value: %s", key, result));
        return result;
    }

    public static void main(String[] args) throws IOException {
        SimpleApolloConfigDemo apolloConfigDemo = new SimpleApolloConfigDemo();
        System.out.println(
                "Apollo Config Demo. Please input key to get the value. Input quit to exit.");
        while (true) {
            System.out.print("> ");
            String input = new BufferedReader(new InputStreamReader(System.in, Charsets.UTF_8)).readLine();
            if (input == null || input.length() == 0) {
                continue;
            }
            input = input.trim();
            if (input.equalsIgnoreCase("quit")) {
                System.exit(0);
            }
            apolloConfigDemo.getConfig(input);
        }
    }
}
