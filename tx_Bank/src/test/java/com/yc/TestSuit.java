package com.yc;

import com.yc.biz.Test01;
import com.yc.config.Test02_DataSourceConfig;
import com.yc.dao.AccountDaoJdbcTemplateImplTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)   //测试套件
@Suite.SuiteClasses({Test01.class, Test02_DataSourceConfig.class, AccountDaoJdbcTemplateImplTest.class})
public class TestSuit {

}
