package com.yc;

import com.yc.biz.OrderBiz;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    public static void main(String[] args) {
        ApplicationContext ac = new AnnotationConfigApplicationContext( MyConfig.class );
        OrderBiz ob = ac.getBean( OrderBiz.class);

//        ob.makeOrder(1,99);

//        ob.findOrderId( "apple" );
//        ob.findOrderId( "apple" );
//        ob.findOrderId( "pear" );
//
//        ob.findOrderPid("apple");
//        ob.findOrderPid("apple");
//        ob.findOrderPid("pear");

        ob.findOrderId("apple");
        ob.findOrderPid("apple");


    }
}
