package com.yc.biz;

import com.yc.bean.Account;
import com.yc.myconfigs.MyConfig;
import com.yc.myconfigs.MyDataSourceConfig;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MyConfig.class, MyDataSourceConfig.class})
@Log4j2
public class AccountBizImplTest {

    @Autowired
    private AccountBiz accountBiz;

    @Test
    public void openAccount() {
        Account a = accountBiz.openAccount(10000);
        log.info( a );
    }

    @Test
    public void deposite() {
        Account a = accountBiz.deposite(20,5000);
        log.info( a );
    }

    @Test
    public void testDeposite() {
        Account a = accountBiz.deposite(20,5000,19);
        log.info( a );
    }

    @Test
    public void withdraw() {
        Account a = accountBiz.withdraw(20,5000);
        log.info( a );
    }

    @Test
    public void testWithdraw() {
        Account a = accountBiz.withdraw(20,5000,19);
        log.info( a );
    }

    @Test
    public void transfer() {
        Account a = accountBiz.transfer(20,5000,19);
        log.info( a );
    }

    @Test
    public void findAccount() {
        Account a = accountBiz.findAccount( 1 );
        log.info( a );
    }
}