package com.yc.staticProxy;

public class OrderBizImpl implements OrderBiz{

    @Override
    public void addOrder(int pid, int num) {
        System.out.println("添加订单,添加了:"+pid+"\t数量为:"+num);
    }

    @Override
    public void findOrder() {
        System.out.println("成功执行订单查询...");
    }
}
