package com.yc.dynamicProxy;

import com.yc.staticProxy.OrderBiz;
import com.yc.staticProxy.OrderBizImpl;

public class Test {

    public static void main(String[] args) {

        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles","true");
        JDK_ProxyAssist jpa = new JDK_ProxyAssist( new OrderBizImpl() );
        OrderBiz ob = (OrderBiz) jpa.CreateProxy(); //调用创建代理对象

        ob.addOrder(1,99);
        ob.findOrder();

        Cglib_ProxyAssist cpa = new Cglib_ProxyAssist(new OrderBizImpl());
        OrderBizImpl obi = (OrderBizImpl) cpa.createProxy();
        System.out.println("Cglib生成的代理类对象:"+ obi.toString() );
        obi.addOrder(1,99);
        obi.findOrder();
    }
}
