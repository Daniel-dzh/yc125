package com.yc.dao;

import com.yc.bean.OpRecord;

import java.util.List;

public interface OpRecordDao {

    /**
     * 添加记录
     * @param opRecord
     */
    public void insertOpRecord(OpRecord opRecord );

    /**
     * 查询一个用户所有的日志，根据时间排序
     * @param accountid
     * @return
     */
    public List<OpRecord> findOpRecord(int accountid);

    /**
     * 查询账户opType类型的操作，根据时间排序
     * @param accountid
     * @param opType
     * @return
     */
    public List<OpRecord> findOpRecord(int accountid,String opType);

    /**
     * 空闲方法，为后面业务扩展
     * @param opRecord
     * @return
     */
    public List<OpRecord> findOpRecord(OpRecord opRecord);

}
