package com.yc.biz;

import com.yc.myconfigs.MyConfig;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( classes = MyConfig.class )
public class Test01 extends TestCase {

    @Autowired
    private AccountBiz accountBiz;


    @Test
    public void testAssert(){
        int x=10,y=20;
        Assert.assertEquals(30,x+y);
        Assert.assertFalse(x==y);
    }
}
