package com.yc.dao;

import com.yc.bean.Account;

import java.util.List;

public interface AccountDao {

    /**
     * 添加Account账户
     * @param money
     * @return
     */
    public int insert(double money);

    /**
     * 根据账号更新余额
     * @param accountid
     * @param money
     */
    public void update(int accountid,double money);

    /**
     * 删除账户
     * @param accountid
     */
    public void delete(int accountid);

    /**
     * 查询账户数量
     * @return
     */
    public int findCount();

    /**
     * 查询所有账户
     * @return
     */
    public List<Account> findAll();

    /**
     * 根据id查询账户
     * @return
     */
    Account findById(int accountid);
}
