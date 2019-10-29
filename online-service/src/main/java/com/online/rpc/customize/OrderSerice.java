package com.online.rpc.customize;


public interface OrderSerice {
    public Order selectById(long id);

    public void selectById(long id,BeanListener beanListener);
}
