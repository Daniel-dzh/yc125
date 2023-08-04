package com.yc.dao;

import com.yc.bean.OpRecord;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class OpRecordDaoJdbcTemplateImpl implements OpRecordDao{
    @Override
    public void insertOpRecord(OpRecord opRecord) {

    }

    @Override
    public List<OpRecord> findOpRecord(int accountid) {
        return null;
    }

    @Override
    public List<OpRecord> findOpRecord(int accountid, String opType) {
        return null;
    }

    @Override
    public List<OpRecord> findOpRecord(OpRecord opRecord) {
        return null;
    }
}
