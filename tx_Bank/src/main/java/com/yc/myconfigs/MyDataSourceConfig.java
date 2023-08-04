package com.yc.myconfigs;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:db.properties")
@Data
@Log4j2
@EnableTransactionManagement
@SuppressWarnings("all")
public class MyDataSourceConfig {

    //利用DI将db.properties中的内容注入容器
    @Value("root")
    private String username;

    @Value("a123")
    private String password;

    @Value("jdbc:mysql://localhost:3306/bank?serverTimezone=UTC")
    private String url;

    @Value("com.mysql.cj.jdbc.Driver")
    private String driverclass;
    //以上属性读取出来后，都存储在 spring 容器的 Environment 变量中(系统环境变量也在这里)

    @Value("#{ T(Runtime).getRuntime().availableProcessors()*2 }")  //使用spEL --》
    private int cpuCount;


    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName( driverclass );
        ds.setUrl( url );
        ds.setUsername( username );
        ds.setPassword( password );
        return ds;
    }
    //使用IOC注解，托管第三方的Bean
    @Bean
    public DataSource dbcpDataSource(){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName( driverclass );
        dataSource.setUrl( url );
        dataSource.setUsername( username );
        dataSource.setPassword( password );
        //还可以传入更多参数
        return dataSource;
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    @Primary
    public DruidDataSource druidDataSource(){
        //1.配置主机地址参数
        DruidDataSource dds = new DruidDataSource();
        dds.setDriverClassName( driverclass );
        dds.setUrl( url );
        dds.setUsername( username );
        dds.setPassword( password );
        //2.创建连接池，在这个类的 init() 的同时完成连接池的创建，
        log.info("配置druid的连接池的大小:"+cpuCount);
        dds.setInitialSize( cpuCount );
        dds.setMaxActive( cpuCount*2  );
        return dds;
    }

    @Bean
    public TransactionManager dataSourceTransactionManage( @Autowired @Qualifier(value = "druidDataSource") DataSource ds ){
        DataSourceTransactionManager tx = new DataSourceTransactionManager();
        tx.setDataSource( ds );
        return tx;
    }
}
