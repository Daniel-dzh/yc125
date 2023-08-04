package com.yc.biz;

import org.springframework.stereotype.Service;

@Service
public class OrderBizImpl implements OrderBiz {

    @Override
    public void makeOrder(int pid, int num) {
        if ( num > 50){
            throw new RuntimeException("商品数量异常.....");
        }
        System.out.println("makeOrder的方法调用:订单编号:"+ pid +"\t商品数量:"+num);
    }

    @Override
    public int findOrderId(String pname) {
        System.out.println("findOrderId根据商品名称:"+ pname + "\t查找商品对应的订单号:"+ 8848);
        return 8848;
    }

    @Override
    public int findOrderPid(String pname) {
        System.out.println("findOrderPid根据商品编名称:"+ pname + "\t查找商品对应的订单号:");
        return (int) (Math.random()*3);
    }
}
