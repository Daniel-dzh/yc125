package com.yc.staticProxy;

public class StaticProxy implements OrderBiz{

    private OrderBiz orderBiz;  //目标类的引入,利用 SetXXX 或者 构造方法 完成注入

    public StaticProxy(OrderBiz orderBiz){
        this.orderBiz = orderBiz;
    }

    public void showHello(){
        System.out.println("世界从这里开始:Hello");
    }

    public void showByb(){
        System.out.println("世界从这里结束:Byb");
    }

    @Override
    public void addOrder(int pid, int num) {
        showHello();    //前置增强
        this.orderBiz.addOrder(pid,num);
        showByb();  //后置增强
    }

    @Override
    public void findOrder() {

    }
}
