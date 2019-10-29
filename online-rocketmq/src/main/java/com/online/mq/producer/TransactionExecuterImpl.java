package com.online.mq.producer;

import org.apache.rocketmq.client.producer.LocalTransactionExecuter;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;

import java.util.concurrent.atomic.AtomicInteger;

/***
 * LocalTransactionExecuter已被丢弃，推荐实现接口 TransactionListener
 */
@Deprecated
public class TransactionExecuterImpl implements LocalTransactionExecuter {

    private AtomicInteger transactionIndex = new AtomicInteger(1);
    public LocalTransactionState executeLocalTransactionBranch(Message message, Object o) {
        System.out.println("msg = " + new String(message.getBody()));
        System.out.println("o = " + o);
        String tag = message.getTags();
        if(tag.equals("Transaction3")) {
            //这里有一个分阶段提交任务的概念
            System.out.println("这里处理业务逻辑，比如操作数据库，失败情况下进行ROLLBACK");

            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        return LocalTransactionState.COMMIT_MESSAGE;
//        return LocalTransactionState.ROLLBACK_MESSAGE;
//        return LocalTransactionState.COMMIT_MESSAGE.UNKNOW;
    }
}
