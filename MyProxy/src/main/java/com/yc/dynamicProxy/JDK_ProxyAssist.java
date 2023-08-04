package com.yc.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JDK_ProxyAssist implements InvocationHandler {

    private Object target;  //目标类

    public JDK_ProxyAssist(Object target){
        this.target = target;
    }

    /*
    代理对象生成的实现
     */
    public Object CreateProxy(){
        Object proxy = Proxy.newProxyInstance(JDK_ProxyAssist.class.getClassLoader(),target.getClass().getInterfaces(),this);
        return proxy;
    }

    public void showHello(){    //前置增强
        System.out.println("世界从这里开始:Hello");
    }

    /**
     * 通过反射调用实现方法代理
     * @param proxy     需要代理的对象对应的代理对象
     * @param method    需要代理的方法
     * @param args      代理方法的参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        showHello();    //添加了前置增强
        Object retValue = method.invoke(target,args);
        return retValue;
    }
}
