package com.yc.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OpRecord {

    private int id;
    private int accountid;
    private double opmoney;
    private String optime;
    private OpType optype;
    private Integer transferId;
}
