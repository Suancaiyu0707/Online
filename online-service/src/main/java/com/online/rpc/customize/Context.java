package com.online.rpc.customize;

import com.google.common.collect.Maps;

import java.util.Map;

/***
 * 隐式上下文参数对象
 */
public class Context {
    //客户端隐式上下文
    private static ThreadLocal<Context> client = new ThreadLocal <>();
    //服务端隐式上下文
    private static ThreadLocal<Context> server = new ThreadLocal <>();
    //隐式参数容器
    private final static Map<String,String> attachments = Maps.newHashMap();

    /***
     * 获得客户端本地上下文对象
     * @return
     */
    public static Context getClientContext(){
        Context context = client.get();

        if(context==null){
            context = new Context();
            client.set(context);
        }
        return context;
    }
    /***
     * 获得服务端本地上下文对象
     * @return
     */
    public static Context getServerContext(){
        Context context = server.get();

        if(context==null){
            context = new Context();
            server.set(context);
        }
        return context;
    }

    /**
     * 获得本地线程的所有隐式参数的容器
     * @return
     */
    public static Map <String, String> getAttachments() {
        return attachments;
    }

    /**
     * 传输需要传递的参数值
     * @param key
     * @param value
     */
    public static void setAttachment(String key,String value) {
        attachments.put(key,value);
    }

    /**
     * 获得传递的参数值
     * @param key
     */
    public static void getAttachment(String key) {
        attachments.get(key);
    }

    /**
     * 结束所有的隐式参数
     * @param attachment
     */
    public static void setAttachments(Map <String, String> attachment) {
        attachments.putAll(attachment);
    }
}
