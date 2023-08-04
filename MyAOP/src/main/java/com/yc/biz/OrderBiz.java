package com.yc.biz;

public interface OrderBiz {

    public void makeOrder( int pid, int num );
    public int findOrderId( String pname);
    public int findOrderPid( String pname );
}
