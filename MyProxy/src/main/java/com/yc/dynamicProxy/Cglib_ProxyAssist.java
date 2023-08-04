package com.yc.dynamicProxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class Cglib_ProxyAssist implements MethodInterceptor {

    private Object target;  //引入目标对象

    public Cglib_ProxyAssist(Object target){
        this.target = target;
    }

    //代理对象的创建
    public Object createProxy(){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass( this.target.getClass());
        enhancer.setCallback( this );
        Object proxy = enhancer.create();
        return proxy;
    }

    public void showHello(){    //前置增强
        System.out.println("世界从这里开始:Hello");
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        if ( method.getName().startsWith("add")){
            showHello();
        }
        //orderBizImpl.findOrder()
        Object returnValue = method.invoke( target,args);
        return returnValue;
    }
}
