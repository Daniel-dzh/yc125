package com.yc.biz;

import com.yc.bean.Account;
import com.yc.bean.OpRecord;
import com.yc.bean.OpType;
import com.yc.dao.AccountDao;
import com.yc.dao.OpRecordDao;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@SuppressWarnings("all")
public class AccountBizImpl implements AccountBiz{

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private OpRecordDao opRecordDao;


    @Override
    public Account openAccount(double money) {
        //开户操作，返回新账号id
        int accountid = this.accountDao.insert(money);
        //包装日志信息
        OpRecord opRecord = new OpRecord();
        opRecord.setAccountid(accountid);
        opRecord.setOpmoney(money);
        opRecord.setOptype(OpType.DEPOSITE);
        this.opRecordDao.insertOpRecord( opRecord );
        //返回新的账户信息
        Account a = new Account();
        a.setAccountid( accountid );
        a.setBalance( money );
        return a;
    }

    @Override
    public Account deposite(int accountid, double money) {
        return this.deposite(accountid,money,null);
    }

    @Override
    public Account deposite(int accountid, double money, Integer transferID) {
        Account a = null;
        try {
            a = this.accountDao.findById(accountid);
        } catch ( Exception e ) {
            log.error("找不到对方的银行账户，无法转账");
            e.printStackTrace();
        }
        //存款时，金额累加
        a.setBalance(a.getBalance()+money);
        this.accountDao.update(accountid,a.getBalance());

        OpRecord opRecord = new OpRecord();
        opRecord.setAccountid(accountid);
        opRecord.setOpmoney(money);
        if (transferID!=null){
            opRecord.setOptype( OpType.TRANSFER );
            opRecord.setTransferId( transferID );
        }else {
            opRecord.setOptype( OpType.DEPOSITE );
        }
        this.opRecordDao.insertOpRecord( opRecord );
        return a;
    }

    @Override
    public Account withdraw(int accountid, double money) {
        return this.withdraw(accountid, money,null);
    }

    @Override
    public Account withdraw(int accountid, double money, Integer transferId) {
        Account a = null;
        try {
            a = this.accountDao.findById( accountid );
        } catch ( RuntimeException re ) {
            log.error( re.getMessage() );
            throw new RuntimeException("查无此账户:"+accountid+",无法存款");
        }
        //取款,金额相减
        a.setBalance( a.getBalance()-money);
        this.accountDao.update(accountid,a.getBalance());

        OpRecord opRecord = new OpRecord();
        opRecord.setAccountid( accountid );
        opRecord.setOpmoney( money );
        if ( transferId!= null ){
            opRecord.setOptype( OpType.TRANSFER );
            opRecord.setTransferId( transferId );
        }else {
            opRecord.setOptype( OpType.WITHDRAW );
        }

        // dao --> datasource --> connection --> jdbc ,隐式事务提交 ->一条sql提交一次
        this.opRecordDao.insertOpRecord( opRecord );
        this.accountDao.update( accountid, a.getBalance() );
        return a;
    }

    @Override
    public Account transfer(int accountId, double money, int toAccountid) {
        //从accounId转money给toAccountId
        this.deposite( toAccountid, money, accountId ); //收款方
        //accountId从账户中取money
        Account a = this.withdraw(accountId,money,toAccountid);
        return null;
    }

    @Override
    public Account findAccount(int accountId) {
        return this.accountDao.findById(accountId);
    }
}
