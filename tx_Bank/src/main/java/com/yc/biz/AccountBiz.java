package com.yc.biz;

import com.yc.bean.Account;

public interface AccountBiz {

    /**
     * 银行开户
     * @param money
     * @return
     */
    public Account openAccount(double money);

    /**
     * 自主型取款
     * @param accountid
     * @param money
     * @return
     */
    public Account deposite(int accountid, double money);

    /**
     * 转账型存款
     * @param accountid
     * @param money
     * @param transferId
     * @return
     */
    public Account deposite(int accountid,double money, Integer transferId);

    /**
     * 自主型存款
     * @param accountid
     * @param money
     * @return
     */
    public Account withdraw(int accountid, double money);

    /**
     * 转账型存款
     * @param accountid
     * @param money
     * @param transferId
     * @return
     */
    public Account withdraw(int accountid, double money, Integer transferId);

    /**
     * 转账
     * @param accountId
     * @param money
     * @param toAccountid
     * @return
     */
    public Account transfer(int accountId, double money, int toAccountid);

    /**
     * 查看账户
     * @param accountId
     * @return
     */
    public Account findAccount(int accountId);

}
