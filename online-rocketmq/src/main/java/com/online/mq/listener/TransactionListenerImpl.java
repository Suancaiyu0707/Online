/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.online.mq.listener;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/***
 * 发送事务消息的时候，需要提供一个监听器
 * @author zhifang.xu
 */
public class TransactionListenerImpl implements TransactionListener {

    private static final Logger logger = LoggerFactory.getLogger(TransactionListener.class);

    private AtomicInteger transactionIndex = new AtomicInteger(0);

    private ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>();
    private static AtomicInteger atomicInteger = new AtomicInteger();

    /***
     * 如果producer向broker发送prepaed状态事务消息成功，则会回调该方法执行本地事务
     * 该方法主要执行本地事务的逻辑
     * @param msg
     * @param arg 用来接收TransactionProducer.sendMessageInTransaction方法中的第二个参数
     * @return
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        int value = transactionIndex.getAndIncrement();
        int status = value % 5;
        localTrans.put(msg.getTransactionId(), status);
        int i  = atomicInteger.getAndIncrement();
        if(i%5==0){
            System.out.println("begin return an unknow result");
            logger.info("begin return an unknow result");
            return LocalTransactionState.UNKNOW;
        }else{
            return LocalTransactionState.COMMIT_MESSAGE;
        }

    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {

        logger.info("mq服务器调用该回调接口进行回查");
        System.out.println("mq服务器调用该回调接口进行回查");
        logger.info("消息标签是:"+new String(msg.getTags()));
        System.out.println("消息标签是:"+new String(msg.getTags()));
        logger.info("消息内容是:"+new String(msg.getBody()));
        System.out.println("消息内容是:"+new String(msg.getBody()));
        Integer status = localTrans.get(msg.getTransactionId());
        if (null != status) {
            switch (status) {
                case 0:
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                case 1:
                    return LocalTransactionState.COMMIT_MESSAGE;
                case 2:
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                default:
                    return LocalTransactionState.COMMIT_MESSAGE;
            }
        }
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
