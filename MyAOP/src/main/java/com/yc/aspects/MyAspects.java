package com.yc.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Aspect
public class MyAspects {
    /*
    切入点表达式 : 类似正则表达式，作用是筛选目标类中哪些方法需要加增强
    * : 表示所有
    .. : 表示任意
     */
    @Pointcut("execution(* com.yc.biz.*.make*(..))") // the pointcut expression
    private void abc() {} // 切入点

    /**
     * 1. 在makeOrder前记录下订时间.
     */
    @Before("execution(* com.yc.biz.*.*(..))")
    public void recordTime() {
        Date date = new Date();
        System.out.println("您的订单完成时间为:"+ date );
    }

    /**
     * 2. 在makeOrder下订完成后加入   发送确认邮件的功能
     */
    @AfterReturning("abc()")
    public void sendEmail() {
        System.out.println("订单完成，向用户发送邮件确认操作......");
    }

    /**
     * 3. q用日志记录  makeOrder时传递的参数信息, 为了便于对账.
     */
    @AfterReturning("abc()")
    public void recordParams(JoinPoint jp) {
        //从jp中可以取出一些信息，make*()方法的信息
        System.out.println("增强的方法为:"+ jp.getSignature() );
        System.out.println("增强的目标类为:"+ jp.getTarget() );
        System.out.println( "参数" );
        Object[] params = jp.getArgs();
        for (Object o : params) {
            System.out.println( o );
        }
    }

    /**
     * 6. 业务层抛出异常，在代理捕获异常，避免强制报错而形成强依赖
     */
    @AfterThrowing(
            pointcut="execution(* com.yc.biz.*.*(..))",
            throwing="ex")
    public void recordException( JoinPoint jp, RuntimeException ex ) {  //由Spring容器将捕获的异常传入
        System.out.println("来自代理的异常报告:"+ ex.getMessage() );
        System.out.println( jp.getArgs()[0]+"\t"+ jp.getArgs()[1]);
    }

    @Pointcut("execution(* com.yc.biz.*.findOrderId(..))") // the pointcut expression
    private void efg() {} // 切入点

    /**
     * 4. 根据商品名称调用统计调用次数
     */
    private Map<String,Long> map01 = new ConcurrentHashMap<String,Long>();    //统计每个商品被查询的次数
    @AfterReturning("efg()")
    public void recordPnameCountByName( JoinPoint jp){
        Object[] objs = jp.getArgs();
        String pname = (String) objs[0];
        Long num = 1L;
        if ( map01.containsKey( pname)){
            num = map01.get( pname );
            num++;
        }
        map01.put( pname, num );
        System.out.println("统计结果:"+ map01);
    }


    @Pointcut("execution(* com.yc.biz.*.findOrderPid(..))") // the pointcut expression
    private void hjk() {} // 切入点

    /**
     * 5. 根据商品编号调用统计调用次数
     */
    private Map<String,Long> map02 = new ConcurrentHashMap<String,Long>();    //统计每个商品被查询的次数
    @AfterReturning(pointcut = "efg()", returning = "retValue")
    public void recordPnameCountByPid( JoinPoint jp, int retValue){ //DI方式注入
        Object[] objs = jp.getArgs();
        String pname = (String) objs[0];
        Long num = 1L;
        if ( map02.containsKey( pname)){
            num = map02.get( pname );
            num++;
        }
        map02.put( pname, num );
        System.out.println("统计结果:"+ map02);
    }

    /**
     * 查询方法过慢，统计一下查询时长来了解详情
     */
    @Pointcut("execution(* com.yc.biz.*.find*(..))") // the pointcut expression
    private void poq() {} // 切入点

    @Around("poq()")
    public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable {  //pjp就是被调用的 find*()
        long start = System.currentTimeMillis();    // start stopwatch
        Object retVal = pjp.proceed();
        long end = System.currentTimeMillis();  // stop stopwatch
        System.out.println("方法执行时长为:"+ (end-start) );
        return retVal;
    }

}
