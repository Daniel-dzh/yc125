package com.yc.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.yc.myconfigs.MyConfig;
import com.yc.myconfigs.MyDataSourceConfig;
import junit.framework.TestCase;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( classes = {MyDataSourceConfig.class, MyConfig.class})
@Log4j2
@SuppressWarnings("all")
public class Test02_DataSourceConfig extends TestCase {

    @Autowired
    private MyDataSourceConfig myDataSourceConfig;

    @Autowired
    private Environment env;

    @Autowired
    @Qualifier("dataSource")
    private DataSource ds;

    @Autowired
    @Qualifier("dbcpDataSource")
    private DataSource dbcp;

    @Autowired
    @Qualifier("druidDataSource")
    private DataSource dds;

    @Autowired
    private TransactionManager tx;

    @Test
    public void testPropertySource(){
        Assert.assertEquals("root",myDataSourceConfig.getUsername() );
        log.info( myDataSourceConfig.getUsername());
    }

    @Test
    public void testEnvironment(){
        log.info( env.getProperty("jdbc.password")+ "\t"+env.getProperty("user.home"));
    }

    @Test
    public void test_dataSourceConnection() throws SQLException {
        Assert.assertNotNull( ds.getConnection() );
        Connection con = ds.getConnection();
        System.out.println( con );
        log.info( con );
    }

    @Test
    public void test_DbcpDataSourceConnection() throws SQLException {
        Assert.assertNotNull( dbcp.getConnection() );
        Connection con = dbcp.getConnection();
        System.out.println( con );
        log.info( con );
    }

    @Test
    public void testDruidDataSource() throws SQLException {
        Assert.assertNotNull( dds.getConnection() );
        Connection con = dds.getConnection();
        System.out.println( con );
        log.info( con+"\t"+( ((DruidDataSource)dds ).getInitialSize()));
    }

    @Test
    public void testTransactionManager(){
        log.info( tx );
    }

}
